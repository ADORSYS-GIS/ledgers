/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.app.integration;

import de.adorsys.ledgers.app.BaseContainersTest;
import de.adorsys.ledgers.app.LedgersApplication;
import de.adorsys.ledgers.app.TestDBConfiguration;
import de.adorsys.ledgers.app.it_endpoints.ManagementStage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static de.adorsys.ledgers.app.integration.PaymentIT.PSU_LOGIN;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles({"testcontainers-it", "sandbox"})
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LedgersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = {TestDBConfiguration.class},
        initializers = { PaymentIT.Initializer.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserManagementIT extends BaseContainersTest<ManagementStage, ManagementStage, ManagementStage> {
    public static final String ADMIN = "admin";
    public static final String ADMIN_PASSWORD = "admin123";
    public static final String TPP_LOGIN_NEW = "newtpp";
    public static final String TPP_EMAIL_NEW = "newtpp@mail.de";
    public static final String TPP_PASSWORD = "11111";
    public static final String BRANCH = "UA_735297";
    public static final String CUSTOMER_LOGIN = "newcutomer";
    public static final String CUSTOMER_EMAIL = "newcutomer@mail.de";
    public static final String NEW_IBAN = "UA379326228389769852388931161";

    @Test
    void deleteUserAsAdmin() {
        given().obtainTokenFromKeycloak(ADMIN, ADMIN_PASSWORD)
                .getUserIdByLogin(PSU_LOGIN);
        when().deleteUser();
        then().getAllUsers()
                .path("login", (List<String> logins) -> assertThat(logins).doesNotContain(PSU_LOGIN));
    }

    @Test
    void addNewStaffUserAndDepositCash() {
        addNewTpp();
        var amount = "10000.00";
        given()
                .obtainTokenFromKeycloak(TPP_LOGIN_NEW, TPP_PASSWORD)
                .createNewUserAsStaff(CUSTOMER_LOGIN, CUSTOMER_EMAIL, BRANCH)
                .createNewAccountForUser("new_account.json", NEW_IBAN)
                .accountByIban(NEW_IBAN);

        when()
                .depositCash("deposit_amount.json", amount);

        then()
                .getAccountDetails()
                .path("balances.amount.amount[0]", am -> assertThat(am.toString()).isEqualTo(amount));
    }

    @Test
    void testTppUpdatesHimself() {
        addNewTpp();
        String newUserLogin = "newtpplogin";
        given()
                .obtainTokenFromKeycloak(TPP_LOGIN_NEW, TPP_PASSWORD)
                .getUserIdByLogin(TPP_LOGIN_NEW)
                .modify("update_tpp_user.json", newUserLogin, TPP_EMAIL_NEW, BRANCH);
        when()
                .obtainTokenFromKeycloak(newUserLogin, TPP_PASSWORD);
        then()
                .readUserFromDb(newUserLogin);
    }

    @Test
    void testUpdatePassword() {
        addNewTpp();
        String newPassword = "hello";
        when().obtainTokenFromKeycloak(ADMIN, ADMIN_PASSWORD);
        then().changePasswordBranch(BRANCH, newPassword)
                .obtainTokenFromKeycloak(TPP_LOGIN_NEW, newPassword);
    }

    @Test
    void testUpdateSelf() {
        String newTppLogin = "new-tpp-login";
        String newTppEmail = "new-tpp-email@mail.ua";
        addNewTpp();
        given().obtainTokenFromKeycloak(TPP_LOGIN_NEW, TPP_PASSWORD);
        when().updateSelfTpp(BRANCH, newTppLogin, newTppEmail);
        then().obtainTokenFromKeycloak(newTppLogin, TPP_PASSWORD)
                .readUserFromDb(newTppLogin)
                .verifyUserEntity(user -> {
                    assertThat(user.get("user_id")).isEqualTo(BRANCH);
                    assertThat(user.get("branch")).isEqualTo(BRANCH);
                    assertThat(user.get("email")).isEqualTo(newTppEmail);
                    assertThat(user.get("login")).isEqualTo(newTppLogin);

                });
    }



    @Test
    void testUploadData() {
        addNewTpp();
        given().obtainTokenFromKeycloak(TPP_LOGIN_NEW, "11111");
        when().uploadData("file_upload/users-accounts-balances-payments-upload.yml");
        then().listCustomersLogins()
                .body((List<String> usersLogin) -> assertThat(usersLogin).contains("newtpp","user-one","user-two"));
        



    }

    private void addNewTpp() {
        given()
                .obtainTokenFromKeycloak(ADMIN, ADMIN_PASSWORD)
                .createNewTppAsAdmin(TPP_LOGIN_NEW, TPP_EMAIL_NEW, BRANCH);
    }
    private String getUserEmail (String userLogin){
        return "newcutomer@mail.de";
    }

    @Test
    public void shouldCreateNewUserAndAdminUserSuccessfully() {
        // Arrange: Create a new Third-Party Provider (TPP) as admin
        String newTppLogin = "exampleInfinity";
        String newTppEmail = "exampple@gmail.com";

        // Act: Perform API calls to create a new TPP
        given()
                .obtainTokenFromKeycloak(ADMIN, ADMIN_PASSWORD)
                .createNewTppAsAdmin(newTppLogin, newTppEmail, BRANCH);

        // Arrange: Create a new admin user
        String newAdminLogin = "newAdminUser";
        String newAdminEmail = "admin@email.com";

        // Act: Perform API calls to create a new admin user
        given()
                .obtainTokenFromKeycloak(ADMIN, ADMIN_PASSWORD)
                .createNewUserAsAdmin(newAdminLogin, newAdminEmail, BRANCH);
        then().readUserFromDb(newAdminLogin)
                .verifyUserEntity(user ->{
                    assertThat(user.get("branch")).isNotNull();
                    assertThat(user.get("email")).isEqualTo(newAdminEmail);
                    assertThat(user.get("login")).isEqualTo(newAdminLogin);
                    assertThat(user.get("user_id")).isNotNull();
                });
    }

    @Test
// Test for checking if a user is being deleted successfully.
    public void TestdeleteUserAsTPP(){
        String newUsertppLogin = "exampleintpp";
        String newUserEmail = "examppletpp@gmail.com";
        given().obtainTokenFromKeycloak(ADMIN, ADMIN_PASSWORD).createNewUserAsAdmin(newUsertppLogin, newUserEmail, BRANCH);

        // Delete the user and verify if the user is deleted
        when().deleteUser();
        then().getAllUsers().body(conc -> assertThat(!conc.equals(newUsertppLogin)));
    }

    @Test
    public void TestdeleteUser(){
        String newUserLogin = "examplein";
        String newUserEmail = "exampple@gmail.com";
        given().obtainTokenFromKeycloak(ADMIN, ADMIN_PASSWORD).createNewUserAsAdmin(newUserLogin, newUserEmail, BRANCH);

        // Delete the user and verify if the user is deleted
        when().deleteUser();
        then().getAllUsers().body(conc -> assertThat(!conc.equals(newUserLogin)));
    }

}
