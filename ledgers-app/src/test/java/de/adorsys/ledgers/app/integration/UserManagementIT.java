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
import java.util.Map;

import static de.adorsys.ledgers.app.Const.*;//NOPMD
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
    private static final List<String> NEW_IBANS = List.of("UA379326228389769852388931161",
                                                          "DE70500105176137412326",
                                                          "DE47500105172662655363");
    @Test
    void deleteUserAsAdmin() {
        given().obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD)
                .getUserIdByLogin(PSU_LOGIN);
        when().deleteUser();
        then().getAllUsers()
                .path("login", (List<String> logins) -> assertThat(logins).doesNotContain(PSU_LOGIN));
    }

    @Test
    void updateUserScaDataTest() {
        String newSmtpMethodValue = "test-email@gmail.com";
        given()
                .obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD)
                .createNewUserAsAdmin(PSU_LOGIN_NEW, PSU_EMAIL_NEW, "")
                .getUserIdByLogin(PSU_LOGIN_NEW);

        when()
                .modifyScaUser("new_sca.json", newSmtpMethodValue);

        then().
                getUserIdByLogin(PSU_LOGIN_NEW)
                .path("findAll { o -> o.login.equals(\""+ PSU_LOGIN_NEW +"\") }[0].scaUserData", (List<Map<String, String>> sca) -> {
                    assertThat(sca.size()).isEqualTo(1);
                    assertThat(sca.get(0).get("scaMethod")).isEqualTo("SMTP_OTP");
                    assertThat(sca.get(0).get("methodValue")).isEqualTo(newSmtpMethodValue);
                });
    }

    @Test
    void addNewStaffUserAndDepositCash() {
        addNewTpp();
        var amount = "10000.00";
        given()
                .obtainTokenFromKeycloak(TPP_LOGIN_NEW, TPP_PASSWORD)
                .createNewUserAsStaff(CUSTOMER_LOGIN, CUSTOMER_EMAIL, BRANCH)
                .createNewAccountForUser("new_account.json", NEW_IBANS.get(0))
                .accountByIban(NEW_IBANS.get(0));
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

    @Test
    void deleteTPPWithUsers() {
        addNewTpp();
        String[] credUser1 = new String[] {"addUser1", "additional1@gmail.com"};
        String[] credUser2 = new String[] {"addUser2", "additional2@gmail.com"};
        String[] credUser3 = new String[] {"addUser3", "additional3@gmail.com"};
        given()
                .obtainTokenFromKeycloak(TPP_LOGIN_NEW, TPP_PASSWORD)
                .createNewUserAsStaff(credUser1[0], credUser1[1], BRANCH)
                .createNewUserAsStaff(credUser2[0], credUser2[1], BRANCH)
                .createNewUserAsStaff(credUser3[0], credUser3[1], BRANCH);

        then().getUserIdByLogin(credUser1[0])
                .pathStr("findAll { o -> o.login.equals(\""+ credUser1[0] +"\") }[0].id", userId -> assertThat(userId).isNotNull())
                .pathStr("findAll { o -> o.login.equals(\""+ credUser2[0] +"\") }[0].id", userId -> assertThat(userId).isNotNull())
                .pathStr("findAll { o -> o.login.equals(\""+ credUser3[0] +"\") }[0].id", userId -> assertThat(userId).isNotNull());

        when().deleteTppWithAllRelatedData(BRANCH);

        then().getUserIdByLogin(credUser1[0])
                .pathStr("findAll { o -> o.login.equals(\""+ credUser1[0] +"\") }[0].id", userId -> assertThat(userId).isNull())
                .pathStr("findAll { o -> o.login.equals(\""+ credUser2[0] +"\") }[0].id", userId -> assertThat(userId).isNull())
                .pathStr("findAll { o -> o.login.equals(\""+ credUser3[0] +"\") }[0].id", userId -> assertThat(userId).isNull());
    }

    @Test
    void testGetListOfAccounts() {
        addNewTpp();
        String[] credUser = new String[]{"addUser1", "additional1@gmail.com"};
        var amount = "10000.00";
        given()
                .obtainTokenFromKeycloak(TPP_LOGIN_NEW, TPP_PASSWORD);

        when().createNewUserAsStaff(CUSTOMER_LOGIN, CUSTOMER_EMAIL, BRANCH)
                .createNewAccountForUser("new_account.json", NEW_IBANS.get(0))
                .depositCash("deposit_amount.json", amount)
                .createNewAccountForUser("new_account.json", NEW_IBANS.get(1))
                .depositCash("deposit_amount.json", amount)
                .createNewUserAsStaff(credUser[0], credUser[1], BRANCH)
                .createNewAccountForUser("new_account.json", NEW_IBANS.get(2));

        then().getAccountForUser()
                .path("", (List<Map<String, String>> accounts) -> {
                    assertThat(accounts.size()).isEqualTo(3);
                    Map<String, String> acc1 = accounts.get(0);
                    Map<String, String> acc2 = accounts.get(1);
                    Map<String, String> acc3 = accounts.get(2);
                    accounts.forEach(acc -> assertThat(acc.get("branch")).isEqualTo(BRANCH));
                    assertThat(acc1.get("name")).isEqualTo(credUser[0]);
                    assertThat(acc1.get("iban")).isEqualTo(NEW_IBANS.get(2));
                    List.of(acc2, acc3).forEach(acc -> {
                        assertThat(acc.get("name")).isEqualTo(CUSTOMER_LOGIN);
                        assertThat(NEW_IBANS.subList(0, 2)).contains(acc.get("iban"));
                    });
                });
    }

    @Test
    public void testCreateNewTPP() {
        addNewTpp();
        then().readUserFromDb(TPP_LOGIN_NEW)
                .verifyUserEntity(user ->{
                    assertThat(user.get("branch")).isNotNull();
                    assertThat(user.get("email")).isEqualTo(TPP_EMAIL_NEW);
                    assertThat(user.get("login")).isEqualTo(TPP_LOGIN_NEW);
                });
    }

    @Test
    public void testDeleteUserAsTPP(){
        String newUserTppLogin = "exampleintpp";
        String newUserEmail = "examppletpp@gmail.com";
        addNewTpp();
        given().obtainTokenFromKeycloak(TPP_LOGIN_NEW, TPP_PASSWORD).createNewUserAsStaff(newUserTppLogin, newUserEmail, BRANCH);
        then().readUserFromDb(newUserTppLogin)
                .verifyUserEntity(user ->{
                    assertThat(user.get("branch")).isNotNull();
                    assertThat(user.get("email")).isEqualTo(newUserEmail);
                    assertThat(user.get("login")).isEqualTo(newUserTppLogin);
                    assertThat(user.get("user_id")).isNotNull();
                });
        when().deleteUser();
        then().listCustomerLogins().body(login -> assertThat(!login.equals(newUserTppLogin)));
    }

    @Test
    public void testDeleteUser(){
        String newUserLogin = "examplein";
        String newUserEmail = "exampple@gmail.com";
        given().obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD).createNewUserAsAdmin(newUserLogin, newUserEmail, BRANCH);
        when().deleteUser();
        then().getAllUsers().body(conc -> assertThat(!conc.equals(newUserLogin)));
    }

    @Test
    public void testCreateNewAdmin() {
        String newAdminLogin = "admin2";
        String newAdminEmail = "newadmin@gmail.com";
        given().obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD).createNewUserAsAdmin(newAdminLogin, newAdminEmail, BRANCH);
        then().readUserFromDb(newAdminLogin)
                .verifyUserEntity(user ->{
                    assertThat(user.get("email")).isEqualTo(newAdminEmail);
                    assertThat(user.get("login")).isEqualTo(newAdminLogin);
                });
    }

    private void addNewTpp() {
        given()
                .obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD)
                .createNewTppAsAdmin(TPP_LOGIN_NEW, TPP_EMAIL_NEW, BRANCH);
    }
}
