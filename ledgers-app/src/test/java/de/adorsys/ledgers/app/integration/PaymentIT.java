/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.app.integration;

import de.adorsys.ledgers.app.BaseContainersTest;
import de.adorsys.ledgers.app.LedgersApplication;
import de.adorsys.ledgers.app.TestDBConfiguration;
import de.adorsys.ledgers.app.it_endpoints.ManagementStage;
import de.adorsys.ledgers.app.it_endpoints.StatusStage;
import de.adorsys.ledgers.app.it_endpoints.OperationStage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"testcontainers-it", "sandbox"})
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LedgersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = {TestDBConfiguration.class},
        initializers = { PaymentIT.Initializer.class })
public class PaymentIT extends BaseContainersTest<ManagementStage, OperationStage, StatusStage> {
    public static final String PSU_LOGIN = "anton.brueckner";
    public static final String PSU_LOGIN_NEW = "newuser12345";
    public static final String PSU_EMAIL_NEW = "newuser12345@mail.de";
    public static final String PSU_PASSWORD = "12345";
    public static final String CHALLENGE_VALUE = "123456";
    public static final String ADMIN_LOGIN  = "admin";
    public static final String ADMIN_PASSWORD = "admin123";

    @Test
    void testCreateSinglePayment() {
        given()
                .obtainTokenFromKeycloak(PSU_LOGIN, PSU_PASSWORD);

        when()
                .createSinglePayment("payment.json", "DE80760700240271232400")
                .scaStart("sca_start_payment.json")
                .listScaMethods()
                .selectScaMethod("SMTP_OTP")
                .reportChallengeValue(CHALLENGE_VALUE)
                .getStatus().pathStr("scaStatus", stat -> assertThat(stat).isEqualTo("finalised"));

        then()
                .paymentStatus().pathStr("transactionStatus", status -> assertThat(status).isEqualTo("ACCP"));
    }

    @Test
    void testCreateNewUserAndCreateSinglePayment() {
        String newIban = "DE62500105174439235992";
        given()
                .obtainTokenFromKeycloak(ADMIN_LOGIN, ADMIN_PASSWORD)
                .createNewUser("new_user.json", PSU_LOGIN_NEW, PSU_EMAIL_NEW)
                .createNewAccountForUser("new_account.json", newIban)
                .accountByIban(newIban)
                .depositCash("deposit_amount.json", "100000")
                .obtainTokenFromKeycloak(PSU_LOGIN_NEW, PSU_PASSWORD);

        when()
                .createSinglePayment("payment.json", newIban)
                .scaStart("sca_start_payment.json")
                .listScaMethods()
                .selectScaMethod("SMTP_OTP")
                .reportChallengeValue(CHALLENGE_VALUE)
                .getStatus().pathStr("scaStatus", stat -> assertThat(stat).isEqualTo("finalised"));

        then()
                .paymentStatus().pathStr("transactionStatus", status -> assertThat(status).isEqualTo("ACCP"));
    }
}
