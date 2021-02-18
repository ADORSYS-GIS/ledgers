package de.adorsys.keycloak.otp.core;

public interface ModelbankConnectorHolder {

    String PROVIDER_ID = "modelbank-connector-resource";

    CmsConnector getCmsConnector();

    AspspConnector getAspspConnector();
}
