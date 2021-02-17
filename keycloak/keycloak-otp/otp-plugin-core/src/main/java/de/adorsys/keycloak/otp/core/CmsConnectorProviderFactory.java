package de.adorsys.keycloak.otp.core;

import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

public interface CmsConnectorProviderFactory extends ProviderFactory<CmsConnector>, ServerInfoAwareProviderFactory {
}
