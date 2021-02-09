package de.adorsys.ledgers.email.code;

import de.adorsys.keycloak.connector.aspsp.LedgersConnectorImpl;
import de.adorsys.keycloak.connector.cms.CmsConnectorImpl;
import de.adorsys.keycloak.otp.core.AspspConnector;
import de.adorsys.keycloak.otp.core.CmsConnector;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.List;

import static de.adorsys.keycloak.otp.core.domain.ScaConstants.REALM;
import static de.adorsys.keycloak.otp.core.domain.ScaConstants.SELECT_METHOD;

public class ModelbankSelectMethodAuthenticator implements Authenticator {

    public ModelbankSelectMethodAuthenticator() {
        this.ledgersConnector = new LedgersConnectorImpl();
        this.cmsConnector = new CmsConnectorImpl();
    }

    private AspspConnector ledgersConnector;
    private CmsConnector cmsConnector;

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        ledgersConnector.setKeycloakSession(context.getSession());
        List<ScaMethod> scaMethods = ledgersConnector.getMethods(context.getUser());

        ScaMethod a = new ScaMethod();
        a.setId("OTP");
        a.setType("OTP");
        a.setDescription("OTP");
        a.setDecoupled(true);

        scaMethods.add(a);

        context.challenge(context.form().setAttribute(REALM, context.getRealm())
                                  .setAttribute("scaMethods", scaMethods.toArray())
                                  .createForm(SELECT_METHOD));
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        KeycloakSession session = context.getSession();
        cmsConnector.setKeycloakSession(session);
        ledgersConnector.setKeycloakSession(session);

        ScaContextHolder scaContextHolder = new ScaContextHolder(context.getHttpRequest());
        scaContextHolder.getStep().apply(scaContextHolder, context, cmsConnector, ledgersConnector);
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        ledgersConnector.setKeycloakSession(keycloakSession);
        List<ScaMethod> methodsForUser = ledgersConnector.getMethods(userModel);
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
