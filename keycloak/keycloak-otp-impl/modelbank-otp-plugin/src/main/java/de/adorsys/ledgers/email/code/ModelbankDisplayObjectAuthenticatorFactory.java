package de.adorsys.ledgers.email.code;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ModelbankDisplayObjectAuthenticatorFactory implements AuthenticatorFactory, ServerInfoAwareProviderFactory {

    @Override
    public String getId() {
        return "modelbank-display-object";
    }

    /**
     * The name of label in admin console.
     */
    @Override
    public String getDisplayType() {
        return "Modelbank display business object";
    }

    @Override
    public String getHelpText() {
        return "Shows the business object that should be processed.";
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
        return Arrays.asList(
                new ProviderConfigProperty("length", "Code length", "The number of digits of the generated code.", ProviderConfigProperty.STRING_TYPE, 6),
                new ProviderConfigProperty("ttl", "Time-to-live", "The time to live in seconds for the code to be valid.", ProviderConfigProperty.STRING_TYPE, "300"),
                new ProviderConfigProperty("senderId", "SenderId", "The sender ID is displayed as the message sender on the receiving device.", ProviderConfigProperty.STRING_TYPE, "Keycloak")
        );
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return new ModelbankDisplayObjectAuthenticator();
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
        return Collections.singletonMap("Version", "demo");
    }

}
