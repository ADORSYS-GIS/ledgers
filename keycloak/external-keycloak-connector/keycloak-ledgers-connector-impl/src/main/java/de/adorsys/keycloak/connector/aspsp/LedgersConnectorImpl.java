package de.adorsys.keycloak.connector.aspsp;

import de.adorsys.keycloak.connector.aspsp.api.AspspConnector;
import de.adorsys.keycloak.otp.core.ScaDataContext;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import org.keycloak.models.UserModel;

import java.util.List;

public class LedgersConnectorImpl implements AspspConnector {
    @Override
    public List<ScaMethod> getMethods(UserModel user) {
        return null;
    }

    @Override
    public void initObj(ScaDataContext scaDataContext, Object object) {

    }

    @Override
    public void selectMethod(ScaDataContext scaDataContext, String methodId) {

    }

    @Override
    public boolean validateCode(ScaDataContext scaDataContext, String code) {
        return false;
    }

    @Override
    public void execute(ScaDataContext scaDataContext) {

    }
}
