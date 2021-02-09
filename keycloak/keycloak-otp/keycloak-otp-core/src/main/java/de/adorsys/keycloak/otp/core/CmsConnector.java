package de.adorsys.keycloak.otp.core;

import de.adorsys.keycloak.otp.core.domain.ConfirmationObject;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import de.adorsys.keycloak.otp.core.domain.ScaStatus;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.representations.AccessToken;

public interface CmsConnector {

    <T> ConfirmationObject<T> getObject(ScaContextHolder scaDataContext);

    void setAuthorizationStatus(ScaContextHolder scaDataContext, ScaStatus scaStatus);

    void updateUserData(UserModel user);

    void pushToken(String objId, AccessToken accessToken);

    void setKeycloakSession(KeycloakSession keycloakSession);
}
