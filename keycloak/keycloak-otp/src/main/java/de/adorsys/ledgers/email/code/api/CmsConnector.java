package de.adorsys.ledgers.email.code.api;

import de.adorsys.keycloak.otp.core.domain.ScaStatus;
import de.adorsys.ledgers.email.code.domain.ScaContextHolder;
import org.keycloak.models.UserModel;
import org.keycloak.representations.AccessToken;

@Deprecated
public interface CmsConnector {
    Object getObject(ScaContextHolder holder);

    void setAuthorizationStatus(ScaContextHolder holder, ScaStatus identified);

    void updateUserData(UserModel user);

    void pushToken(String objId, AccessToken accessToken);
}
