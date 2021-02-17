package de.adorsys.keycloak.connector.cms.impl;

import de.adorsys.keycloak.otp.core.CmsConnector;
import de.adorsys.keycloak.otp.core.CmsConnectorProviderFactory;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

import java.util.Map;

public class CmsConnectorProviderFactoryImpl implements CmsConnectorProviderFactory, ServerInfoAwareProviderFactory {
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
        return "cms-connector-impl";
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        return null;
    }
}
