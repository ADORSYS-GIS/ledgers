package de.adorsys.keycloak.connector.aspsp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.adorsys.keycloak.otp.core.AspspConnector;
import de.adorsys.keycloak.otp.core.domain.CodeValidationResult;
import de.adorsys.keycloak.otp.core.domain.ConfirmationObject;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTO;
import de.adorsys.ledgers.middleware.api.domain.sca.GlobalScaResponseTO;
import de.adorsys.ledgers.middleware.api.domain.sca.OpTypeTO;
import de.adorsys.ledgers.middleware.api.domain.sca.ScaStatusTO;
import de.adorsys.ledgers.middleware.api.domain.sca.StartScaOprTO;
import de.adorsys.ledgers.middleware.api.domain.um.ScaUserDataTO;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.exception.ResteasyHttpException;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;

//TODO in case of errors consider returning a DEV message to display something on UI!
public class LedgersConnectorImpl implements AspspConnector {

    private static final Logger LOG = Logger.getLogger(LedgersConnectorImpl.class);
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final String CONNECTION_ERROR_MSG = "Error occurred during connection to CoreBanking Service";
    private static final String TEMPLATE_INIT_ERROR_MSG = "Error connecting to Ledgers initiating %s for user: %s";

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
    public <T> void initObj(ScaContextHolder scaDataContext, ConfirmationObject<T> object, String login) {
        if (equalsIgnoreCase(scaDataContext.getObjType(), "cancel_payment")) {
            ConfirmationObject<PaymentTO> payment = (ConfirmationObject<PaymentTO>) object;
            initiatePaymentCancellationInLedgers(payment, login);
        } else {
            initiatePmtConsent(object, login);
        }
    }

    @Override
    public void selectMethod(ScaContextHolder scaDataContext, String methodId, String login) {
        selectMethodInLedgers(scaDataContext, methodId, login);
    }

    @Override
    public CodeValidationResult validateCode(ScaContextHolder scaDataContext, String code, String login) {
        return validateCodeInLedgers(scaDataContext, code, login);
    }

    @Override
    public void execute(ScaContextHolder scaDataContext, String login) {
        executePaymentInLedgers(scaDataContext, login);
    }

    private List<ScaMethod> getMethodsFromLedgers(String login) {
        try {
            List<ScaUserDataTO> list = SimpleHttp.doGet(LEDGERS_BASE_URL + "fapi/methods/" + login, keycloakSession)
                                               .asJson(AspspMapper.SCA_DATA_LIST_TYPE);
            return AspspMapper.mapToScaMethodList(list);
        } catch (IOException e) {
            LOG.error("Error connecting to Ledgers retrieving SCA methods for user: " + login);
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }
    }

    private <T> void initiatePmtConsent(ConfirmationObject<T> object, String login) {
        OpTypeTO type = AspspMapper.mapOperationType(object.getObjType());
        String path = type == OpTypeTO.CONSENT ? "consent" : "payment";
        try {
            SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/initiate/" + path + "/" + login, keycloakSession)
                    .json(object.getRawBusinessObject())
                    .asStatus();
        } catch (IOException e) {
            LOG.error(String.format(TEMPLATE_INIT_ERROR_MSG, path, login));
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }
    }

    private void initiatePaymentCancellationInLedgers(ConfirmationObject<PaymentTO> object, String login) {
        try {
            SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/initiate/cancellation/" + login, keycloakSession)
                    .param("paymentId", object.getId())
                    .asStatus();
        } catch (IOException e) {
            LOG.error(String.format(TEMPLATE_INIT_ERROR_MSG, "cancellation", login));
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }
    }

    private void selectMethodInLedgers(ScaContextHolder scaDataContext, String methodId, String login) {
        StartScaOprTO scaOprTO = AspspMapper.mapStartScaOpr(scaDataContext);

        try {
            String json = SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/sca/select?login=" + login + "&methodId=" + methodId, keycloakSession)
                                  .json(scaOprTO)
                                  .asString();
            GlobalScaResponseTO response = MAPPER.readValue(json, GlobalScaResponseTO.class);
            response.getPsuMessage(); //TODO Could return PSU message.
        } catch (IOException e) {
            LOG.error("Error connecting to Ledgers selecting SCA method for user: " + login);
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }
    }

    private CodeValidationResult validateCodeInLedgers(ScaContextHolder scaDataContext, String code, String login) {
        try {
            String json = SimpleHttp.doPost(LEDGERS_BASE_URL + "fapi/validate", keycloakSession)
                                  .param("authId", scaDataContext.getAuthId())
                                  .param("code", code)
                                  .param("login", login)
                                  .asString();

            GlobalScaResponseTO response = MAPPER.readValue(json, GlobalScaResponseTO.class);

            boolean isValid = EnumSet.of(ScaStatusTO.FINALISED, ScaStatusTO.UNCONFIRMED).contains(response.getScaStatus());
            boolean mlScaRequired = response.isMultilevelScaRequired();

            return new CodeValidationResult(isValid, mlScaRequired);
        } catch (IOException e) {
            LOG.error("Error connecting to Ledgers validating auth code for user: " + login);
            return new CodeValidationResult(); //This is a failure case all fields default to false
        }

    }

    private void executePaymentInLedgers(ScaContextHolder scaDataContext, String login) {
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

    @Override
    public void setKeycloakSession(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }
}
