/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.app.it_endpoints;

import com.tngtech.jgiven.annotation.ScenarioState;
import io.restassured.RestAssured;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import java.util.List;

public class DataManagementEndpoints extends BaseStage<DataManagementEndpoints> {

    public static String DELETE_USER = "/staff-access/data/user/{userId}";
    public static String ALL_USERS = "/admin/users/all";
    @ScenarioState
    private String bearerToken;
    @ScenarioState
    private String userId;

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
    } public DataManagementEndpoints getAllUsers() {
        var resp = RestAssured.given()
                           .header(HttpHeaders.AUTHORIZATION, bearerToken)
                           .when()
                           .get(ALL_USERS)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();

        this.response = resp;
        return self();
    }

}
