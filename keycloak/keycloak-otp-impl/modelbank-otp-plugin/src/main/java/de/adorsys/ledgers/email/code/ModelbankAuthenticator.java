package de.adorsys.ledgers.email.code;

import com.sun.xml.bind.v2.TODO;
import de.adorsys.keycloak.connector.aspsp.LedgersConnectorImpl;
import de.adorsys.keycloak.connector.aspsp.api.AspspConnector;
import de.adorsys.keycloak.connector.cms.api.CmsConnector;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import de.adorsys.ledgers.email.code.domain.ScaContextHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.List;

import static de.adorsys.ledgers.email.code.domain.ScaConstants.DISPLAY_OBJ;
import static de.adorsys.ledgers.email.code.domain.ScaConstants.REALM;

public class ModelbankAuthenticator implements Authenticator {

    private final CmsConnector cmsConnector = null; //TODO STUB HERE!!!
    private final AspspConnector aspspConnector = null; //TODO STUB HERE!!!

    @Override
    public void authenticate(AuthenticationFlowContext context) {
//        CmsConnector cmsConnector = new CmsConnectorImpl(context.getSession());
//
//        ScaContextHolder scaContextHolder = new ScaContextHolder(context.getHttpRequest());
//        Object object = cmsConnector.getObject(scaContextHolder);
//        //TODO view add object
        context.challenge(context.form().setAttribute(REALM, context.getRealm()).createForm(DISPLAY_OBJ));
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
        LedgersConnectorImpl ledgersConnector = new LedgersConnectorImpl(keycloakSession);
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
