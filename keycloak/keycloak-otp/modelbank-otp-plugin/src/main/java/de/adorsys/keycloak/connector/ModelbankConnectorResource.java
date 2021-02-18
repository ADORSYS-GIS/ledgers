package de.adorsys.keycloak.connector;

import de.adorsys.keycloak.connector.aspsp.LedgersConnectorImpl;
import de.adorsys.keycloak.connector.cms.CmsConnectorImpl;
import de.adorsys.keycloak.otp.core.AspspConnector;
import de.adorsys.keycloak.otp.core.CmsConnector;
import de.adorsys.keycloak.otp.core.ModelbankConnectorHolder;
import org.keycloak.models.KeycloakSession;

public class ModelbankConnectorResource implements ModelbankConnectorHolder {

    private final CmsConnector cmsConnector;
    private final AspspConnector aspspConnector;

    public ModelbankConnectorResource(KeycloakSession session) {
        this.cmsConnector = new CmsConnectorImpl(session);
        this.aspspConnector = new LedgersConnectorImpl(session);
    }

    public CmsConnector getCmsConnector() {
        return cmsConnector;
    }

    public AspspConnector getAspspConnector() {
        return aspspConnector;
    }
}
