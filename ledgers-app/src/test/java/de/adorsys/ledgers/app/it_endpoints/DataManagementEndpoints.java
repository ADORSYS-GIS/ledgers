/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.app.it_endpoints;

import com.tngtech.jgiven.annotation.ScenarioState;
import io.restassured.RestAssured;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.Map;
import static de.adorsys.ledgers.app.BaseContainersTest.resource;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class DataManagementEndpoints extends BaseStage<DataManagementEndpoints> {

    public static String DELETE_USER = "/staff-access/data/user/{userId}";
    public static final String DEPOSIT_CASH_RESOURCE = "/staff-access/accounts/{accountId}/cash";

    @ScenarioState
    private String bearerToken;
    @ScenarioState
    private String userId;
    @ScenarioState
    private String accountId;

    public DataManagementEndpoints deleteUser() {
        var resp = RestAssured.given()
                           .header(HttpHeaders.AUTHORIZATION, bearerToken)
                           .when()
                           .delete(DELETE_USER, userId)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();

        this.response = resp;
        return self();
    }

    public DataManagementEndpoints depositCash(String amountResource, String amount) {
        var resp = RestAssured.given()
                           .header(AUTHORIZATION, this.bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .body(resource(amountResource).replaceAll("%AMOUNT%", amount))
                           .when()
                           .post(DEPOSIT_CASH_RESOURCE, accountId)
                           .then()
                           .statusCode(HttpStatus.ACCEPTED.value())
                           .and()
                           .extract();

        this.response = resp;
        return self();
    }
}
