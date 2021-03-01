package de.adorsys.ledgers.email.code;

import de.adorsys.keycloak.otp.core.AspspConnector;
import de.adorsys.keycloak.otp.core.CmsConnector;
import de.adorsys.keycloak.otp.core.ModelbankConnectorHolder;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.resource.RealmResourceProvider;

import java.util.List;

public class ModelbankSelectMethodAuthenticator implements Authenticator {

    private final CmsConnector cmsConnector;
    private final AspspConnector aspspConnector;

    public ModelbankSelectMethodAuthenticator(KeycloakSession session) {
        RealmResourceProvider provider = session.getProvider(RealmResourceProvider.class, ModelbankConnectorHolder.PROVIDER_ID);
        //todo: exception if no provider
        this.aspspConnector = ((ModelbankConnectorHolder) provider.getResource()).getAspspConnector();
        this.cmsConnector = ((ModelbankConnectorHolder) provider.getResource()).getCmsConnector();
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        ScaContextHolder scaContextHolder = new ScaContextHolder(context.getHttpRequest());
        scaContextHolder.getStep().apply(scaContextHolder, context, cmsConnector, aspspConnector);
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        List<ScaMethod> methodsForUser = aspspConnector.getMethods(userModel);
        return CollectionUtils.isNotEmpty(methodsForUser);
        // TODO: think about exempted

    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void close() {

    }
}
