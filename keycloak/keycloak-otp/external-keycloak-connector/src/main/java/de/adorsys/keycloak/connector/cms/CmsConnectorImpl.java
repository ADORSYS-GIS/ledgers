package de.adorsys.keycloak.connector.cms;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.adorsys.keycloak.connector.cms.model.CmsTokenRequest;
import de.adorsys.keycloak.connector.cms.model.Xs2aScaStatus;
import de.adorsys.keycloak.otp.core.CmsConnector;
import de.adorsys.keycloak.otp.core.domain.ConfirmationObject;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import de.adorsys.keycloak.otp.core.domain.ScaStatus;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTO;
import de.adorsys.psd2.consent.api.ais.CmsConsent;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.exception.ResteasyHttpException;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.connections.httpclient.HttpClientProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.representations.AccessToken;

import java.io.IOException;
import java.util.Base64;

public class CmsConnectorImpl implements CmsConnector {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(PaymentTO.class, new CmsPaymentDeserializer(MAPPER));
        MAPPER.registerModule(module);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static final Logger LOG = Logger.getLogger(CmsConnectorImpl.class);

    private static final String CONNECTION_ERROR_MSG = "Could not connect to remote CMS, please try again later.";

    private KeycloakSession keycloakSession;

    public CmsConnectorImpl() {
    }

    public CmsConnectorImpl(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }

    private static final String CMS_BASE_URL = "http://localhost:38080/";

    @Override
    public ConfirmationObject getObject(ScaContextHolder holder) {
        LOG.info("Getting business object with ID: " + holder.getObjId() + " from CMS.");
        return "payment".equalsIgnoreCase(holder.getObjType())
                       ? getPaymentFromCms(holder)
                       : getConsentFromCms(holder.getObjId());
    }

    @Override
    public void setAuthorizationStatus(ScaContextHolder holder, ScaStatus identified) {
        LOG.info("Updating status for authorisation entity with ID: " + holder.getAuthId() + " in CMS.");
        updateAuthorisationStatusInCms(holder.getAuthId(), identified);
    }

    @Override
    public void updateUserData(UserModel user) {
        //TODO: needs endpoint in CMS
    }

    @Override
    public void pushToken(String objId, AccessToken accessToken) {
        LOG.info("Pushing token data for business object with ID: " + objId + " to CMS.");
        pushTokenToCms(objId, accessToken);
    }

    private void pushTokenToCms(String objId, AccessToken accessToken) {
        int responseCode = 500;

        CmsTokenRequest cmsTokenRequest = new CmsTokenRequest();
        cmsTokenRequest.setBusinessObjectId(objId);
        cmsTokenRequest.setAspspConsentDataBase64(Base64.getEncoder().encodeToString(accessToken.getAccessTokenHash().getBytes()));

        try {
            responseCode = SimpleHttp.doPut(CMS_BASE_URL + "api/v1/aspsp-consent-data/consents/" + objId, keycloakSession.getProvider(HttpClientProvider.class).getHttpClient())
                                   .json(cmsTokenRequest)
                                   .asStatus();
        } catch (IOException e) {
            LOG.error("Error connecting to CMS updating consentData for object with ID: " + objId + ", response code: " + responseCode);
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }
    }

    private ConfirmationObject getPaymentFromCms(ScaContextHolder holder) {
        PaymentTO payment;
        ConfirmationObject<Object> confirmationObject;

        try {
            String paymentJson = SimpleHttp.doGet(CMS_BASE_URL + "psu-api/v1/payment/redirect/" + holder.getAuthId(), keycloakSession).asString();
            payment = MAPPER.readValue(paymentJson, PaymentTO.class);
        } catch (IOException e) {
            LOG.error("Error connecting to CMS retrieving payment by redirect ID: " + holder.getAuthId());
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }

        confirmationObject = new ConfirmationObject<>(payment,
                                                      "PAYMENT",
                                                      "Please confirm incoming payment",
                                                      payment.getPaymentId(),
                                                      payment.getPaymentId(),
                                                      ""
        );
        //TODO Finish displayInfo transformation!!!

        return confirmationObject;
    }

    private ConfirmationObject getConsentFromCms(String objId) {
        CmsConsent cmsConsent;
        ConfirmationObject<Object> confirmationObject;
        try {
            String json = SimpleHttp.doGet(CMS_BASE_URL + "api/v1/consent/" + objId, keycloakSession).asString();
            cmsConsent = MAPPER.readValue(json, CmsConsent.class);
        } catch (IOException e) {
            LOG.error("Error connecting to CMS retrieving consent with ID: " + objId);
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }

        confirmationObject = new ConfirmationObject<>(cmsConsent,
                                                      "CONSENT",
                                                      "Please confirm incoming consent",
                                                      objId,
                                                      cmsConsent.getId(),
                                                      "Valid until: %s, frequency per day: %s, recurring indicator: %s",
                                                      String.valueOf(cmsConsent.getValidUntil()),
                                                      String.valueOf(cmsConsent.getFrequencyPerDay()),
                                                      String.valueOf(cmsConsent.isRecurringIndicator()));
        return confirmationObject;
    }

    private void updateAuthorisationStatusInCms(String authId, ScaStatus status) {
        int responseCode = 500;

        Xs2aScaStatus xs2aScaStatus = mapToXs2aScaStatus(status);

        try {
            responseCode = SimpleHttp.doPut(CMS_BASE_URL + "api/v1/authorisations/" + authId + "/" + xs2aScaStatus, keycloakSession.getProvider(HttpClientProvider.class).getHttpClient())
                                   .asStatus();
        } catch (IOException e) {
            LOG.error("Error connecting to CMS updating authorisation status, auth ID: " + authId + ", response code: " + responseCode);
        }
    }

    private Xs2aScaStatus mapToXs2aScaStatus(ScaStatus status) {
        switch (status) {
            case IDENTIFIED:
                return Xs2aScaStatus.PSUIDENTIFIED;

            case METHOD_SELECTED:
                return Xs2aScaStatus.SCAMETHODSELECTED;

            case FINALIZED:
                return Xs2aScaStatus.FINALISED;

            case EXEMPTED:
                return Xs2aScaStatus.EXEMPTED;

            default:
                throw new IllegalArgumentException("Unsupported SCA status: " + status);
        }
    }

    @Override
    public void setKeycloakSession(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }

}
