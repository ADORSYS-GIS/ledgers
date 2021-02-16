package de.adorsys.ledgers.email.code;

import de.adorsys.keycloak.connector.aspsp.LedgersConnectorImpl;
import de.adorsys.keycloak.connector.cms.CmsConnectorImpl;
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

    public ModelbankTanAuthenticator() {
        this.ledgersConnector = new LedgersConnectorImpl();
        this.cmsConnector = new CmsConnectorImpl();
    }

    private AspspConnector ledgersConnector;
    private CmsConnector cmsConnector;

    @Override
    public void authenticate(AuthenticationFlowContext context) {
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        ScaContextHolder scaContextHolder = new ScaContextHolder(context.getHttpRequest());
        scaContextHolder.getStep().apply(scaContextHolder, context, cmsConnector, ledgersConnector);
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
