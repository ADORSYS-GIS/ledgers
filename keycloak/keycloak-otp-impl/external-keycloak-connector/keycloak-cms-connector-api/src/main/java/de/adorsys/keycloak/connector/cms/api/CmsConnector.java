package de.adorsys.keycloak.connector.cms.api;

import de.adorsys.keycloak.otp.core.ScaDataContext;
import de.adorsys.keycloak.otp.core.domain.ScaStatus;
import org.keycloak.models.UserModel;
import org.keycloak.representations.AccessToken;

public interface CmsConnector {

    Object getObject(ScaDataContext scaDataContext);

    void setAuthorizationStatus(ScaDataContext scaDataContext, ScaStatus scaStatus);

    void updateUserData(UserModel user);

    void pushToken(String objId, AccessToken accessToken);
}
