package de.adorsys.ledgers.app.it_endpoints;

import com.tngtech.jgiven.integration.spring.JGivenStage;
import de.adorsys.ledgers.keycloak.client.config.KeycloakClientConfig;
import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.Map;

@JGivenStage
public class ManagementEndpoints extends BaseStage<ManagementEndpoints> {

    @Autowired
    private KeycloakClientConfig clientConfig;
    public void obtainTokenFromKeycloak(String psuLogin, String psuPassword) {
        var resp = RestAssured.given()
                           .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                           .formParams(Map.of(
                                   "username", psuLogin,
                                   "password", psuPassword,
                                   "client_id", clientConfig.getExternalClientId(),
                                   "client_secret", clientConfig.getClientSecret(),
                                   "grant_type", "password")
                           )
                           .when()
                           .post(clientConfig.getAuthServerUrl())
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();

        this.response = resp;
    }
}
