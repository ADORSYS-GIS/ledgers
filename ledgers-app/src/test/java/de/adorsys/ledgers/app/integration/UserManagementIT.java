/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.app.integration;

import de.adorsys.ledgers.app.BaseContainersTest;
import de.adorsys.ledgers.app.LedgersApplication;
import de.adorsys.ledgers.app.TestDBConfiguration;
import de.adorsys.ledgers.app.it_stages.ManagementStage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;

import static de.adorsys.ledgers.app.Const.ADMIN_LOGIN;
import static de.adorsys.ledgers.app.Const.ADMIN_PASSWORD;
import static de.adorsys.ledgers.app.Const.PSU_LOGIN;
import static de.adorsys.ledgers.app.Const.TPP_EMAIL_NEW;
import static de.adorsys.ledgers.app.Const.TPP_LOGIN_NEW;
import static de.adorsys.ledgers.app.Const.TPP_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"testcontainers-it", "sandbox"})
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LedgersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = {TestDBConfiguration.class},
        initializers = { PaymentIT.Initializer.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserManagementIT extends BaseContainersTest<ManagementStage, ManagementStage, ManagementStage> {
    private static final String BRANCH = "UA_735297";
    private static final String CUSTOMER_LOGIN = "newcutomer";
    private static final String CUSTOMER_EMAIL = "newcutomer@mail.de";
    private static final String NEW_IBAN = "UA379326228389769852388931161";
    @Test
    void deleteUserAsAdmin() {
        given().obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD)
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
                .modifyUser("update_tpp_user.json", newUserLogin, TPP_EMAIL_NEW, BRANCH);
        when()
                .obtainTokenFromKeycloak(newUserLogin, TPP_PASSWORD);
        then()
                .readUserFromDb(newUserLogin);
    }
    @Test
    void testUpdatePassword() {
        addNewTpp();
        String newPassword = "hello";
        when().obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD);
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
        then().listCustomerLogins()
                .body((List<String> usersLogin) -> assertThat(usersLogin).contains("newtpp","user-one","user-two"));
    }
    private void addNewTpp() {
        given()
                .obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD)
                .createNewTppAsAdmin(TPP_LOGIN_NEW, TPP_EMAIL_NEW, BRANCH);
    }
    @Test
    public void testShouldCreateNewUserAndAdminUserSuccessfully() {
        //  Create a new Third-Party Provider (TPP) as admin
        String newTppLogin = "exampleInfinity";
        String newTppEmail = "exampple@gmail.com";
        //  Perform API calls to create a new TPP
        given()
                .obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD)
                .createNewTppAsAdmin(newTppLogin, newTppEmail, BRANCH);
        // Create a new admin user
        String newAdminLogin = "newAdminUser";
        String newAdminEmail = "admin@email.com";
        // Perform API calls to create a new admin user
        given()
                .obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD)
                .createNewAdminAsAdmin(newAdminLogin, newAdminEmail);
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
    public void testDeleteUserAsTPP(){
        String newUserTppLogin = "exampleintpp";
        String newUserEmail = "examppletpp@gmail.com";
        given().obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD).createNewUserAsAdmin(newUserTppLogin, newUserEmail, BRANCH);
        // Delete the user and verify if the user is deleted
        when().deleteUser();
        then().getAllUsers().body(conc -> assertThat(!conc.equals(newUserTppLogin)));
    }

    @Test
    public void testDeleteUser(){
        String newUserLogin = "examplein";
        String newUserEmail = "exampple@gmail.com";
        given().obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD).createNewUserAsAdmin(newUserLogin, newUserEmail, BRANCH);
        // Delete the user and verify if the user is deleted
        when().deleteUser();
        then().getAllUsers().body(conc -> assertThat(!conc.equals(newUserLogin)));
    }
}
