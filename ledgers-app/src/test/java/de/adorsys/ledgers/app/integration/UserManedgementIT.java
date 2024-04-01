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
import java.util.List;
import static de.adorsys.ledgers.app.integration.PaymentIT.PSU_LOGIN;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"testcontainers-it", "sandbox"})
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LedgersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = {TestDBConfiguration.class},
        initializers = { PaymentIT.Initializer.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserManedgementIT extends BaseContainersTest<ManagementStage, ManagementStage, ManagementStage> {
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

    private void addNewTpp() {
        given()
                .obtainTokenFromKeycloak(ADMIN, ADMIN_PASSWORD)
                .createNewTppAsAdmin(TPP_LOGIN_NEW, TPP_EMAIL_NEW, BRANCH);
    }

}
