package de.adorsys.ledgers.email.code;

import de.adorsys.keycloak.otp.core.AspspConnector;
import de.adorsys.keycloak.otp.core.CmsConnector;
import de.adorsys.keycloak.otp.core.ModelbankConnectorHolder;
import de.adorsys.keycloak.otp.core.domain.ConfirmationObject;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.resource.RealmResourceProvider;
import util.ParUtil;

import java.util.List;

import static de.adorsys.keycloak.otp.core.domain.ScaConstants.DISPLAY_OBJ;
import static de.adorsys.keycloak.otp.core.domain.ScaConstants.REALM;

public class ModelbankDisplayObjectAuthenticator implements Authenticator {

    private final CmsConnector cmsConnector;
    private final AspspConnector aspspConnector;

    public ModelbankDisplayObjectAuthenticator(KeycloakSession session) {
        RealmResourceProvider provider = session.getProvider(RealmResourceProvider.class, ModelbankConnectorHolder.PROVIDER_ID);
        //todo: exception if no provider
        this.aspspConnector = ((ModelbankConnectorHolder) provider.getResource()).getAspspConnector();
        this.cmsConnector = ((ModelbankConnectorHolder) provider.getResource()).getCmsConnector();
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        PushedAuthorizationRequest par = ParUtil.getParFromContext(context);

        ScaContextHolder scaContextHolder = new ScaContextHolder(par.getBusinessObjectId(), par.getAuthorizationId(), par.getObjectType());
        ConfirmationObject<Object> object = cmsConnector.getObject(scaContextHolder);

        context.challenge(context.form().setAttribute(REALM, context.getRealm())
                                  .setAttribute("object", object)
                                  .setAttribute("context", scaContextHolder)
                                  .createForm(DISPLAY_OBJ));
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

    private ConfirmationObject<Object> getMockConfirmationObject() {
        ConfirmationObject<Object> object = new ConfirmationObject<>();
        object.setObjType("test payment");
        object.setDisplayInfo("payment");
        object.setId("1234567890");
        object.setDescription("description of the payment");
        return object;
    }
}
