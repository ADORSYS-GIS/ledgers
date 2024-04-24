/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.app.it_stages;

import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import io.restassured.RestAssured;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@JGivenStage
public class StatusStage extends BaseStage<StatusStage> {

    public static final String PAYMENTS = "/payments/{paymentId}";

    @ScenarioState
    private String bearerToken;
    @ScenarioState
    private String operationObjectId;

    public StatusStage paymentStatus() {
        var resp = RestAssured.given()
                       .header(HttpHeaders.AUTHORIZATION, bearerToken)
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .when()
                       .get(PAYMENTS, operationObjectId)
                       .then()
                       .statusCode(HttpStatus.OK.value())
                       .and()
                       .extract();
        this.response = resp;
        return self();
    }
}
