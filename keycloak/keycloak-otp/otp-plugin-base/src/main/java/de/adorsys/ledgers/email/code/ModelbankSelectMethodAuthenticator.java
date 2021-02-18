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

import static de.adorsys.keycloak.otp.core.domain.ScaConstants.REALM;
import static de.adorsys.keycloak.otp.core.domain.ScaConstants.SELECT_METHOD;

public class ModelbankSelectMethodAuthenticator implements Authenticator {

    private final CmsConnector cmsConnector;
    private final AspspConnector aspspConnector;

    public ModelbankSelectMethodAuthenticator(KeycloakSession session) {
        RealmResourceProvider provider = session.getProvider(RealmResourceProvider.class, ModelbankConnectorHolder.PROVIDER_ID);
        //todo: exception if no provider
        this.aspspConnector = ((ModelbankConnectorHolder)provider.getResource()).getAspspConnector();
        this.cmsConnector = ((ModelbankConnectorHolder)provider.getResource()).getCmsConnector();
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        List<ScaMethod> scaMethods = aspspConnector.getMethods(context.getUser());

        ScaMethod a = getMockedScaMethod();
        scaMethods.add(a); //TODO this is another MockStub for testing purposes!!!

        context.challenge(context.form().setAttribute(REALM, context.getRealm())
                                  .setAttribute("scaMethods", scaMethods.toArray())
                                  .createForm(SELECT_METHOD));
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        KeycloakSession session = context.getSession();

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

    private ScaMethod getMockedScaMethod() {
        ScaMethod a = new ScaMethod();
        a.setId("OTP");
        a.setType("OTP");
        a.setDescription("OTP");
        a.setDecoupled(true);
        return a;
    }
}
