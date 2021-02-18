package de.adorsys.keycloak.otp.core;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

public class ConnectorSpi implements Spi {
    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "connectors";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return ConnectorProvider.class;
    }

    @Override
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return null;
    }
}
