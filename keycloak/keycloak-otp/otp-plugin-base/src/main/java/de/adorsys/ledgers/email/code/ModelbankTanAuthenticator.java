package de.adorsys.ledgers.email.code;

import de.adorsys.keycloak.otp.core.AspspConnector;
import de.adorsys.keycloak.otp.core.CmsConnector;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.concurrent.ThreadLocalRandom;

public class ModelbankTanAuthenticator implements Authenticator {

    private final CmsConnector cmsConnector;
    private final AspspConnector aspspConnector;

    public ModelbankTanAuthenticator(KeycloakSession session) {
        this.aspspConnector = session.getProvider(AspspConnector.class);
        this.cmsConnector = session.getProvider(CmsConnector.class);
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
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return user.getEmail() != null;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
    }

    @Override
    public void close() {
    }

    private String generateAuthCode(int length) {
        double maxValue = Math.pow(10.0, length);
        int randomNumber = ThreadLocalRandom.current().nextInt((int) maxValue);
        return String.format("%0" + length + "d", randomNumber);
    }

}
