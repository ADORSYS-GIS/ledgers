package de.adorsys.keycloak.otp.core;

import org.keycloak.component.ComponentFactory;

public interface ConnectorProviderFactory<T extends ConnectorProvider> extends ComponentFactory<T, ConnectorProvider> {
}
