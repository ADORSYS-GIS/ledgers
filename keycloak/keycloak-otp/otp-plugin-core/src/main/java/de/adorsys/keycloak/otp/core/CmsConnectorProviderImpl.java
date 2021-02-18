package de.adorsys.keycloak.otp.core;

import org.keycloak.models.KeycloakSession;

public class CmsConnectorProviderImpl implements CmsConnectorProvider{


    private KeycloakSession keycloakSession;

    public CmsConnectorProviderImpl(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }

    @Override
    public void close() {

    }
}
