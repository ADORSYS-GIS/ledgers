package de.adorsys.keycloak.connector.cms;

import de.adorsys.keycloak.connector.cms.api.CmsConnector;
import de.adorsys.keycloak.otp.core.ScaDataContext;
import de.adorsys.keycloak.otp.core.domain.ScaStatus;
import org.keycloak.models.UserModel;
import org.keycloak.representations.AccessToken;

public class CmsConnectorImpl implements CmsConnector {
    @Override
    public Object getObject(ScaDataContext scaDataContext) {
        return null;
    }

    @Override
    public void setAuthorizationStatus(ScaDataContext scaDataContext, ScaStatus scaStatus) {

    }

    @Override
    public void updateUserData(UserModel user) {

    }

    @Override
    public void pushToken(AccessToken accessToken) {

    }
}
