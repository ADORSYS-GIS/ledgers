package de.adorsys.ledgers.app.it_endpoints;

import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import de.adorsys.ledgers.keycloak.client.config.KeycloakClientConfig;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static de.adorsys.ledgers.app.BaseContainersTest.resource;

@JGivenStage
public class ManagementEndpoints extends BaseStage<ManagementEndpoints> {

    public static final String USERS_RESOURCE = "/staff-access/users";
    public static final String KEYCLOAK_TOKEN_PATH = "/realms/ledgers/protocol/openid-connect/token";

    @Autowired
    private KeycloakClientConfig clientConfig;

    @Getter
    @ScenarioState
    private Map<String, String> createdUserIdsByLogin;

    @ScenarioState
    private String bearerToken;

    @ScenarioState
    private String userId;

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
                           .post(clientConfig.getAuthServerUrl() + KEYCLOAK_TOKEN_PATH)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();

        this.response = resp;
        this.bearerToken = getAccessToken(resp);
    }

    public void createNewUser(String resourceName, String login, String email) {
        var resp = RestAssured.given()
                           .header(AUTHORIZATION, this.bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .body(resource(resourceName, Map.of("LOGIN", login, "EMAIL", email)))
                           .when()
                           .post(USERS_RESOURCE)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();
        this.response = resp;
        this.userId = resp.path("id");

        recordUserId(login);

        this.response = resp;
    }

    @NotNull
    protected static String getAccessToken(ExtractableResponse<Response> resp) {
        return "Bearer " + resp.path("access_token");
    }

    private void recordUserId(String login) {
        if (null == createdUserIdsByLogin) {
            createdUserIdsByLogin = new ConcurrentHashMap<>();
        }
        createdUserIdsByLogin.put(login, userId);
    }
}
