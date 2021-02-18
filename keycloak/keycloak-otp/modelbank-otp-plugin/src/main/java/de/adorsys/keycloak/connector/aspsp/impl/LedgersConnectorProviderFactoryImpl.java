package de.adorsys.keycloak.connector.aspsp.impl;

import de.adorsys.keycloak.otp.core.AspspConnector;
import de.adorsys.keycloak.otp.core.LedgersConnectorProviderFactory;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

import java.util.Map;

public class LedgersConnectorProviderFactoryImpl implements LedgersConnectorProviderFactory, ServerInfoAwareProviderFactory {
    @Override
    public AspspConnector create(KeycloakSession keycloakSession) {
        return new LedgersConnectorImpl(keycloakSession);
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
        return "ledgers-connector-impl";
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        return null;
    }
}
