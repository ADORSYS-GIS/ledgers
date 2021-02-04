package de.adorsys.keycloak.connector.aspsp;

import com.fasterxml.jackson.databind.JsonNode;
import de.adorsys.keycloak.connector.aspsp.api.AspspConnector;
import de.adorsys.keycloak.otp.core.ScaDataContext;
import de.adorsys.keycloak.otp.core.domain.CodeValidationResult;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import de.adorsys.ledgers.middleware.api.domain.sca.OpTypeTO;
import de.adorsys.ledgers.middleware.api.domain.sca.ScaStatusTO;
import de.adorsys.ledgers.middleware.api.domain.sca.StartScaOprTO;
import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.exception.ResteasyHttpException;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class LedgersConnectorImpl implements AspspConnector {

    private static final Logger LOG = Logger.getLogger(LedgersConnectorImpl.class);
    private static final String CONNECTION_ERROR_MSG = "Error occurred during connection to CoreBanking Service";

    private KeycloakSession keycloakSession;

//    public LedgersConnectorImpl(KeycloakSession keycloakSession) {
//        this.keycloakSession = keycloakSession;
//    }

    private static final String LEDGERS_BASE_URL = "http://localhost:8088/";

    @Override
    public List<ScaMethod> getMethods(UserModel user) {
        return getMethodsFromLedgers(user.getUsername());
    }

    @Override
    public void initObj(ScaDataContext scaDataContext, Object object, String login) {
        if (StringUtils.equalsIgnoreCase(scaDataContext.getObjType(), "payment")) {
            initiatePaymentInLedgers(object, login);
            return;
        }
        if (StringUtils.equalsIgnoreCase(scaDataContext.getObjType(), "cancel_payment")) {
            initiatePaymentCancellationInLedgers(object, login);
            return;
        }
        initiateConsentInLedgers(object, login);
    }

    @Override
    public void selectMethod(ScaDataContext scaDataContext, String methodId, String login) {
        selectMethodInLedgers(scaDataContext, methodId, login);
    }

    @Override
    public CodeValidationResult validateCode(ScaDataContext scaDataContext, String code, String login) {
        return validateCodeInLedgers(scaDataContext, code, login);
    }

    @Override
    public void execute(ScaDataContext scaDataContext, String login) {
        executePaymentInLedgers(scaDataContext, login);
    }

    private List<ScaMethod> getMethodsFromLedgers(String login) {
        List<ScaMethod> methods = new ArrayList<>();
        JsonNode response;

        try {
            response = SimpleHttp.doGet(LEDGERS_BASE_URL + "fapi/methods/" + login, keycloakSession).asJson();
        } catch (IOException e) {
            LOG.error("Error connecting to Ledgers retrieving SCA methods for user: " + login);
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }

        for (JsonNode jsonNode : response) {
            ScaMethod scaMethod = new ScaMethod();
            scaMethod.setId(jsonNode.get("id").asText());
            scaMethod.setDecoupled(jsonNode.get("decoupled").asBoolean());
            scaMethod.setType(jsonNode.get("scaMethod").asText());
            scaMethod.setDescription(jsonNode.get("methodValue").asText());

            methods.add(scaMethod);
        }

        return methods;
    }

    private void initiatePaymentInLedgers(Object object, String login) {
        try {
            SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/initiate/payment/" + login, keycloakSession)
                    .json(object)
                    .asStatus();
        } catch (IOException e) {
            LOG.error("Error connecting to Ledgers initiating payment for user: " + login);
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }
    }

    private void initiatePaymentCancellationInLedgers(Object object, String login) {
        try {
            SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/initiate/cancellation/" + login, keycloakSession)
                    .json(object)
                    .asStatus();
        } catch (IOException e) {
            LOG.error("Error connecting to Ledgers initiating payment cancellation for user: " + login);
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }
    }

    private void initiateConsentInLedgers(Object object, String login) {
        try {
            SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/initiate/consent/" + login, keycloakSession)
                    .json(object)
                    .asStatus();
        } catch (IOException e) {
            LOG.error("Error connecting to Ledgers initiating consent for user: " + login);
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }
    }

    private void selectMethodInLedgers(ScaDataContext scaDataContext, String methodId, String login) {
        StartScaOprTO scaOprTO = new StartScaOprTO(scaDataContext.getAuthId(), mapOperationType(scaDataContext.getObjType()));

        try {
            SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/sca/select?methodId=" + methodId + "&login=" + login, keycloakSession)
                    .json(scaOprTO)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .asStatus();
        } catch (IOException e) {
            LOG.error("Error connecting to Ledgers selecting SCA method for user: " + login);
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }
    }

    private CodeValidationResult validateCodeInLedgers(ScaDataContext scaDataContext, String code, String login) {
        JsonNode response;

        try {
            response = SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/validate", keycloakSession)
                               .param("authId", scaDataContext.getAuthId())
                               .param("code", code)
                               .param("login", login)
                               .asJson();
        } catch (IOException e) {
            LOG.error("Error connecting to Ledgers validating auth code for user: " + login);
            return new CodeValidationResult(); //This is a failure case all fileds default to false
        }

        boolean isValid = EnumSet.of(ScaStatusTO.FINALISED, ScaStatusTO.UNCONFIRMED).contains(ScaStatusTO.valueOf(response.get("scaStatus").asText()));
        boolean mlScaRequired = response.get("multilevelScaRequired").asBoolean();

        return new CodeValidationResult(isValid, mlScaRequired);
    }

    private void executePaymentInLedgers(ScaDataContext scaDataContext, String login) {
        try {
            SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/execute", keycloakSession)
                    .param("opId", scaDataContext.getObjId())
                    .param("opType", scaDataContext.getObjType())
                    .asStatus();
        } catch (IOException e) {
            LOG.error("Error connecting to Ledgers executing payment for user: " + login);
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }
    }

    private OpTypeTO mapOperationType(String objectType) {
        if (objectType.equalsIgnoreCase("payment")) {
            return OpTypeTO.PAYMENT;
        }
        if (objectType.equalsIgnoreCase("cancel_payment")) {
            return OpTypeTO.CANCEL_PAYMENT;
        }
        if (objectType.equalsIgnoreCase("consent")) {
            return OpTypeTO.CONSENT;
        }
        throw new IllegalArgumentException("Unsupported object type: " + objectType);
    }

    public void setKeycloakSession(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }
}
