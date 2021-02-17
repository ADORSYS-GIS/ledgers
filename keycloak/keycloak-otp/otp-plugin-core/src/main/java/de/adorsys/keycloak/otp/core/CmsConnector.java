package de.adorsys.keycloak.otp.core;

import de.adorsys.keycloak.otp.core.domain.ConfirmationObject;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import de.adorsys.keycloak.otp.core.domain.ScaStatus;
import org.keycloak.models.UserModel;
import org.keycloak.provider.Provider;
import org.keycloak.representations.AccessToken;

public interface CmsConnector extends Provider {

    <T> ConfirmationObject<T> getObject(ScaContextHolder scaDataContext);

    void setAuthorizationStatus(ScaContextHolder scaDataContext, ScaStatus scaStatus);

    void updateUserData(UserModel user);

    void pushToken(String objId, AccessToken accessToken);
}
