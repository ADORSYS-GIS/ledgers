package de.adorsys.ledgers.app.it_endpoints;

import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import de.adorsys.ledgers.keycloak.client.config.KeycloakClientConfig;
import io.restassured.RestAssured;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static de.adorsys.ledgers.app.BaseContainersTest.resource;

@JGivenStage
public class ManagementStage extends BaseStage<ManagementStage> {

    public static final String USERS_RESOURCE_ADMIN = "/admin/user";
    public static final String USERS_RESOURCE_STAFF = "staff-access/users";
    public static final String USERS_BY_LOGIN = "/admin/users/all";
    public static final String ACCOUNTS_RESOURCE = "/staff-access/accounts";
    public static final String ACCOUNT_BY_IBAN = "/staff-access/accounts/acc/acc";
    public static final String ACCOUNT_DETAIL = "/staff-access/accounts/{accountId}";
    public static final String DEPOSIT_CASH_RESOURCE = "/staff-access/accounts/{accountId}/cash";
    public static final String KEYCLOAK_TOKEN_PATH = "/realms/ledgers/protocol/openid-connect/token";
    public static String ALL_USERS = "/admin/users/all";
    public static String CHANGE_STATUS = "admin/status";

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

    public ManagementStage getAllUsers() {
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


    public ManagementStage createNewUserAsAdmin(String login, String email, String branch) {
        return createNewUser(USERS_RESOURCE_ADMIN, login, email, branch);
    }

    public ManagementStage createNewUserAsStaff(String login, String email, String branch) {
        return createNewUser(USERS_RESOURCE_STAFF, login, email, branch);
    }

    public ManagementStage createNewTppAsAdmin(String login, String email, String branch) {
        var resp = RestAssured.given()
                           .header(AUTHORIZATION, this.bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .body(resource("new_staff.json", Map.of("LOGIN", login, "EMAIL", email, "ID", branch)))
                           .when()
                           .post(USERS_RESOURCE_ADMIN)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();

        this.response = resp;
        this.userId = resp.path("id");
        recordUserId(login);
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

    public ManagementStage changeStatusUser() {
        var resp = RestAssured.given()
                           .header(AUTHORIZATION, this.bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .queryParams("userId", userId)
                           .when()
                           .post(CHANGE_STATUS)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();

        this.response = resp;
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

    public ManagementStage getAccountDetails() {
        var resp = RestAssured.given()
                           .header(AUTHORIZATION, this.bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .when()
                           .get(ACCOUNT_DETAIL, accountId)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();
        this.response = resp;
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

    private ManagementStage createNewUser(String endpoint, String login, String email, String branch) {
        var resp = RestAssured.given()
                           .header(AUTHORIZATION, this.bearerToken)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .body(resource("new_user.json", Map.of("LOGIN", login, "EMAIL", email, "BRANCH", branch, "SCA_ID", UUID.randomUUID().toString())))
                           .when()
                           .post(endpoint)
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .and()
                           .extract();

        this.response = resp;
        this.userId = resp.path("id");
        recordUserId(login);
        return self();
    }

    private void recordUserId(String login) {
        if (null == createdUserIdsByLogin) {
            createdUserIdsByLogin = new ConcurrentHashMap<>();
        }
        createdUserIdsByLogin.put(login, userId);
    }
}
