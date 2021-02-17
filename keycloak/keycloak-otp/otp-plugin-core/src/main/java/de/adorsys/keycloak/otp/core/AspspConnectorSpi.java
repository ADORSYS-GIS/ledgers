package de.adorsys.keycloak.otp.core;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

public class AspspConnectorSpi implements Spi {
    @Override
    public boolean isInternal() {
        return true;
    }

    @Override
    public String getName() {
        return "aspsp-connector";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return AspspConnector.class;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return LedgersConnectorProviderFactory.class;
    }
}
