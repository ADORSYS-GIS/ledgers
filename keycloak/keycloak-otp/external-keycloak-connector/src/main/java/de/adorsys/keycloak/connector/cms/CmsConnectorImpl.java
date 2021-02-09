package de.adorsys.keycloak.connector.cms;

import com.fasterxml.jackson.databind.JsonNode;
import de.adorsys.keycloak.connector.cms.model.CmsTokenRequest;
import de.adorsys.keycloak.connector.cms.model.Xs2aScaStatus;
import de.adorsys.keycloak.otp.core.CmsConnector;
import de.adorsys.keycloak.otp.core.domain.ConfirmationObject;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import de.adorsys.keycloak.otp.core.domain.ScaStatus;
import org.apache.commons.lang.StringUtils;
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
        return StringUtils.equalsIgnoreCase(holder.getObjType(), "payment")
                       ? getPaymentFromCms(holder.getObjId())
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

    private ConfirmationObject getPaymentFromCms(String objId) {
        JsonNode payment;
        ConfirmationObject<Object> confirmationObject;
        try {
            payment = SimpleHttp.doGet(CMS_BASE_URL + "api/v1/pis/payment/" + objId, keycloakSession).asJson();
            //TODO Consider using CMS MODELS!!! We do not really need JsonNodes although we've got prepared mappers in OBA!
            confirmationObject = new ConfirmationObject<>(payment,
                                                          "PAYMENT",
                                                          "Please confirm incoming payment",
                                                          objId,
                                                          "ENCRYPTED ID HERE",
                                                          "Debtor: %s, account: %s, cur: %s,\nCreditor: %s account: %s cur: %s \namount: %s cur: %s \npayment type: %s \nPay Day: %s",
                                                          payment.get("debtor").get("name").asText(),
                                                          payment.get("debtorAccount").asText());
            //TODO Finish displayInfo transformation!!!
        } catch (IOException e) {
            LOG.error("Error connecting to CMS retrieving payment with ID: " + objId);
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }

        return confirmationObject;
    }

    private ConfirmationObject getConsentFromCms(String objId) {
        JsonNode consent;
        ConfirmationObject<Object> confirmationObject = new ConfirmationObject<>();
        try {
            consent = SimpleHttp.doGet(CMS_BASE_URL + "api/v1/consent/" + objId, keycloakSession).asJson();

        } catch (IOException e) {
            LOG.error("Error connecting to CMS retrieving consent with ID: " + objId);
            throw new ResteasyHttpException(CONNECTION_ERROR_MSG);
        }
        //TODO Same as for Payment!
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
