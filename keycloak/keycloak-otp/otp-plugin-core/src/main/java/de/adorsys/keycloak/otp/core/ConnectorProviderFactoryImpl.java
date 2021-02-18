package de.adorsys.keycloak.otp.core;

import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Collections;
import java.util.List;

public class ConnectorProviderFactoryImpl implements ConnectorProviderFactory<CmsConnectorProvider>{
    @Override
    public CmsConnectorProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        return new CmsConnectorProviderImpl(keycloakSession);
    }

    @Override
    public String getHelpText() {
        return "Provider for CMS connector";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Collections.emptyList();
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
        return "cms-connector-provider";
    }
}
