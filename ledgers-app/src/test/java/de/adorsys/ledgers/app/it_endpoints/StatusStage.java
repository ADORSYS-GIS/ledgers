package de.adorsys.ledgers.app.it_endpoints;

import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import io.restassured.RestAssured;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@JGivenStage
public class StatusStage extends BaseStage<StatusStage> {

    public static final String PAYMENTS = "/payments/{paymentId}";
    public static final String AUTHORISATION_OPERATION = "/sca/authorisations/{authorisationId}/authCode";
    private static final String FINALISED_STATUS = "FINALISED";


    @Autowired
    private NamedParameterJdbcOperations jdbcOperations;

    @ScenarioState
    private String bearerToken;

    @Getter
    @ScenarioState
    private Map<String, Object> paymentEntity;

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
    public StatusStage paymentId() { //TODO change method name
        var query = "SELECT * FROM public.payment WHERE payment_id = :paymentId";

        this.paymentEntity = jdbcOperations.queryForObject(
                query,
                Map.of("paymentId", this.operationObjectId),
                new ColumnMapRowMapper()
        );
        assertThat(paymentEntity).isNotNull();
        return self();
    }

}
