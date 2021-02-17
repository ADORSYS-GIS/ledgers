package de.adorsys.keycloak.otp.core;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

public class CmsConnectorSpi implements Spi {
    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "cmsConnector";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return CmsConnector.class;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return CmsConnectorProviderFactory.class;
    }
}
