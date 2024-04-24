/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.app.it_stages;

import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import io.restassured.RestAssured;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.UUID;
import static de.adorsys.ledgers.app.BaseContainersTest.resource;

@JGivenStage
public class OperationStage extends BaseStage<OperationStage> {
    private static final String OP_PAYMENT = "/operation/payment";
    private static final String SCA_AUTHORISATIONS_METHOD = "/sca/authorisations/{authorisationId}/scaMethods/{scaMethodId}";
    private static final String SCA_START = "/sca/start";
    private static final String SCA_AUTHORISATIONS = "/sca/authorisations/{authorisationId}";
    private static final String SCA_AUTHORISATION_VALUE = "/sca/authorisations/{authorisationId}/authCode";

    @ScenarioState
    private String operationId; //NOPMD
    @ScenarioState
    private String authorisationId;
    @ScenarioState
    private String operationObjectId;
    @ScenarioState
    private String bearerToken;

    @SneakyThrows
    public OperationStage createSinglePayment(String paymentBodyRes, String ibanFrom) {
        var resp = RestAssured.given()
                           .header(HttpHeaders.AUTHORIZATION, bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .body(resource(paymentBodyRes, Map.of("ID", UUID.randomUUID().toString(), "PAYMENT_IBAN", ibanFrom)))
                           .queryParams("paymentType", "SINGLE")
                           .when()
                           .post(OP_PAYMENT)
                           .then()
                           .statusCode(HttpStatus.CREATED.value())
                           .and()
                           .extract();

        this.bearerToken = getBearerToken(resp);
        this.operationObjectId = resp.path("operationObjectId");
        this.response = resp;
        return self();
    }

    //My updates on bulk payments.
    @SneakyThrows
    public OperationStage createBulkPayment(String paymentBodyRes, String ibanFrom) {
        var resp = RestAssured.given()
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(resource(paymentBodyRes, Map.of("ID", UUID.randomUUID().toString(), "PAYMENT_IBAN", ibanFrom)))
                .queryParams("paymentType", "BULK")
                .when()
                .post(OP_PAYMENT)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .and()
                .extract();

        this.bearerToken = getBearerToken(resp);
        this.operationObjectId = resp.path("operationObjectId");
        this.response = resp;
        return self();
    }

// check if bulk payment failed.
    @SneakyThrows
    public OperationStage failedBulkPayment(String paymentBodyRes, String ibanFrom) {
        var resp = RestAssured.given()
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(resource(paymentBodyRes, Map.of("ID", UUID.randomUUID().toString(), "PAYMENT_IBAN", ibanFrom)))
                .queryParams("paymentType", "BULK")
                .when()
                .post(OP_PAYMENT)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .and()
                .extract();

        this.response = resp;
        return self();
    }


    @SneakyThrows
    public OperationStage failedSinglePayment(String paymentBodyRes, String ibanFrom) {
        this.response = RestAssured.given()
                           .header(HttpHeaders.AUTHORIZATION, bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .body(resource(paymentBodyRes, Map.of("ID", UUID.randomUUID().toString(), "PAYMENT_IBAN", ibanFrom)))
                           .queryParams("paymentType", "SINGLE")
                           .when()
                           .post(OP_PAYMENT)
                           .then()
                           .statusCode(HttpStatus.UNAUTHORIZED.value())
                           .and()
                           .extract();
        return self();
    }

    public OperationStage scaStart(String scaBodyRes) {
        this.authorisationId = UUID.randomUUID().toString();
        var resp = RestAssured.given()
                           .header(HttpHeaders.AUTHORIZATION, bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .body(resource(scaBodyRes, Map.of("AUTHORISATION_ID", this.authorisationId, "OPERATION_OBJECT_ID", this.operationObjectId)))
                           .when()
                           .post(SCA_START)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();
        this.response = resp;
        return self();
    }

    public OperationStage listScaMethods() {
        var resp = RestAssured.given()
                           .header(HttpHeaders.AUTHORIZATION, bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .when()
                           .get(SCA_AUTHORISATIONS, this.authorisationId)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();
        this.response = resp;
        return self();
    }

    public OperationStage selectScaMethod(String methodName) {
        var methodId = response.path("scaMethods.findAll { o -> o.scaMethod == '" + methodName + "' }[0].id");
        var resp = RestAssured.given()
                           .header(HttpHeaders.AUTHORIZATION, bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .when()
                           .put(SCA_AUTHORISATIONS_METHOD, this.authorisationId, methodId)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();
        this.response = resp;
        return self();
    }

    public OperationStage reportChallengeValue(String challengeValue) {
        var resp = RestAssured.given()
                           .header(HttpHeaders.AUTHORIZATION, bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .queryParams("authCode", challengeValue)
                           .when()
                           .put(SCA_AUTHORISATION_VALUE, this.authorisationId)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();

        this.bearerToken = getBearerToken(resp);
        this.response = resp;
        return self();
    }

    public OperationStage getStatus() {
        var resp = RestAssured.given()
                           .header(HttpHeaders.AUTHORIZATION, bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .when()
                           .get(SCA_AUTHORISATIONS, this.authorisationId)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();
        this.response = resp;
        this.operationId = resp.path("operationObjectId");
        return self();
    }
}
