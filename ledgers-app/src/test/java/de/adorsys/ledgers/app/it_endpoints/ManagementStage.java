package de.adorsys.ledgers.app.it_endpoints;

import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import de.adorsys.ledgers.keycloak.client.config.KeycloakClientConfig;
import io.restassured.RestAssured;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static de.adorsys.ledgers.app.BaseContainersTest.resource;

@JGivenStage
public class ManagementStage extends BaseStage<ManagementStage> {

    public static final String USERS_RESOURCE = "/admin/user";
    public static final String USERS_BY_LOGIN = "/admin/users/all";
    public static final String ACCOUNTS_RESOURCE = "/staff-access/accounts";
    public static final String ACCOUNT_BY_IBAN = "/staff-access/accounts/acc/acc";
    public static final String DEPOSIT_CASH_RESOURCE = "/staff-access/accounts/{accountId}/cash";
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

    @ScenarioState
    private String accountId;

    public ManagementStage obtainTokenFromKeycloak(String psuLogin, String psuPassword) {
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
        return self();
    }

    public ManagementStage createNewUser(String resourceName, String login, String email) {
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
        return self();
    }

    public ManagementStage getUserIdByLogin(String login) {
        var resp = RestAssured.given()
                          .header(AUTHORIZATION, this.bearerToken)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .get(USERS_BY_LOGIN)
                          .then()
                          .statusCode(HttpStatus.OK.value())
                          .and()
                          .extract();

        this.response = resp;
        this.userId = this.response.path("findAll { o -> o.login.equals(\"anton.brueckner\") }[0].id");
        return self();
    }

    public ManagementStage createNewAccountForUser(String accountBodyResource, String iban) {
        var resp = RestAssured.given()
                           .header(AUTHORIZATION, this.bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .body(resource(accountBodyResource, Map.of("IBAN", iban)))
                           .queryParams("userId", userId)
                           .when()
                           .post(ACCOUNTS_RESOURCE)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();
        this.response = resp;
        return self();
    }

    public ManagementStage accountByIban(String iban) {
        var resp = RestAssured.given()
                           .header(AUTHORIZATION, this.bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .queryParams(Map.of("ibanParam", iban))
                           .when()
                           .get(ACCOUNT_BY_IBAN)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();
        this.response = resp;
        this.accountId = resp.path("id[0]");
        return self();
    }

    public ManagementStage depositCash(String amountResource, String amount) {
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

    private void recordUserId(String login) {
        if (null == createdUserIdsByLogin) {
            createdUserIdsByLogin = new ConcurrentHashMap<>();
        }
        createdUserIdsByLogin.put(login, userId);
    }
}
