package de.adorsys.ledgers.email.code;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ModelbankSelectMethodAuthenticatorFactory implements AuthenticatorFactory, ServerInfoAwareProviderFactory {

    @Override
    public String getId() {
        return "modelbank-select-method";
    }

    /**
     * The name of label in admin console.
     */
    @Override
    public String getDisplayType() {
        return "Modelbank select SCA method";
    }

    @Override
    public String getHelpText() {
        return "Allows the user to choose appropriate SCA method.";
    }

    @Override
    public String getReferenceCategory() {
        return "otp";
    } // TODO:

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[]{
                AuthenticationExecutionModel.Requirement.REQUIRED,
                AuthenticationExecutionModel.Requirement.ALTERNATIVE,
                AuthenticationExecutionModel.Requirement.DISABLED,
        };
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Collections.emptyList();
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return new ModelbankSelectMethodAuthenticator(session);
    }

    @Override
    public void init(Config.Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        return null;
    }

}
