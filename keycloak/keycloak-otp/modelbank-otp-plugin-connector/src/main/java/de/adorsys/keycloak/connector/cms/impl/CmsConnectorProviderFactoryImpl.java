package de.adorsys.keycloak.connector.cms.impl;

import de.adorsys.keycloak.otp.core.CmsConnectorProviderFactory;
import de.adorsys.keycloak.otp.core.CmsConnector;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class CmsConnectorProviderFactoryImpl implements CmsConnectorProviderFactory {
    @Override
    public CmsConnector create(KeycloakSession keycloakSession) {
        return new CmsConnectorImpl(keycloakSession);
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "cmsConnectorImpl";
    }
}
