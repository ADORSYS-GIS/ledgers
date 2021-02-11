package de.adorsys.ledgers.email.code;

import de.adorsys.keycloak.connector.aspsp.LedgersConnectorImpl;
import de.adorsys.keycloak.connector.cms.CmsConnectorImpl;
import de.adorsys.keycloak.otp.core.AspspConnector;
import de.adorsys.keycloak.otp.core.CmsConnector;
import de.adorsys.keycloak.otp.core.domain.ConfirmationObject;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.List;

import static de.adorsys.keycloak.otp.core.domain.ScaConstants.DISPLAY_OBJ;
import static de.adorsys.keycloak.otp.core.domain.ScaConstants.REALM;

public class ModelbankDisplayObjectAuthenticator implements Authenticator {

    public ModelbankDisplayObjectAuthenticator() {
        this.aspspConnector = new LedgersConnectorImpl();
        this.cmsConnector = new CmsConnectorImpl();
    }

    private final CmsConnector cmsConnector; //TODO STUB HERE!!!
    private final AspspConnector aspspConnector; //TODO STUB HERE!!!

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        cmsConnector.setKeycloakSession(context.getSession());

//        ScaContextHolder scaContextHolder = new ScaContextHolder(context.getHttpRequest());
//        ConfirmationObject<Object> object = cmsConnector.getObject(scaContextHolder);
        ScaContextHolder scaContextHolder = new ScaContextHolder("5XGuEzgXzGjlJqGZSpOXSjVQ4Rs8KCG0UKMZWUzwJrup9iTe8TR-Oeh2M2OQgXJRaVMIP1gFmFZSPqbs-YLHD9WFnjze07vwpAgFM45MlQk=_=_psGLvQpt9Q", "auth_id", "consent");
        ConfirmationObject<Object> object = getMockConfirmationObject();
        cmsConnector.getObject(scaContextHolder);
//TODO -----> Mocked values here until we figure how to pass PAR/RAR token requests <-----

        context.challenge(context.form().setAttribute(REALM, context.getRealm())
                                  .setAttribute("object", object)
                                  .setAttribute("context", scaContextHolder)
                                  .createForm(DISPLAY_OBJ));
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // Just display the form.
        context.success();
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        aspspConnector.setKeycloakSession(keycloakSession);
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

    private ConfirmationObject<Object> getMockConfirmationObject() {
        ConfirmationObject<Object> object = new ConfirmationObject<>();
        object.setObjType("test payment");
        object.setDisplayInfo("payment");
        object.setId("1234567890");
        object.setDescription("description of the payment");
        return object;
    }
}
