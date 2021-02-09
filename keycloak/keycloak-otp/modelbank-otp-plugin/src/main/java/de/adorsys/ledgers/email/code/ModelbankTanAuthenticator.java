package de.adorsys.ledgers.email.code;

import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.*;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.theme.Theme;

import javax.ws.rs.core.Response;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class ModelbankTanAuthenticator implements Authenticator {

    private static final String TPL_CODE = "input_tan.ftl";

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        // TODO:
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        KeycloakSession session = context.getSession();
        UserModel user = context.getUser();

        String userEmail = user.getEmail();

        int length = Integer.parseInt(config.getConfig().get("length"));
        int ttl = Integer.parseInt(config.getConfig().get("ttl"));

        String code = generateAuthCode(length);
        AuthenticationSessionModel authSession = context.getAuthenticationSession();
        authSession.setAuthNote("code", code);
        authSession.setAuthNote("ttl", Long.toString(System.currentTimeMillis() + (ttl * 1000)));

        try {
            Theme theme = session.theme().getTheme(Theme.Type.LOGIN);
            Locale locale = session.getContext().resolveLocale(user);
            String emailAuthText = theme.getMessages(locale).getProperty("emailAuthText");
            String emailText = String.format(emailAuthText, code, Math.floorDiv(ttl, 60));


            // TODO: here we send email;
            System.out.println("=================== SENDING THE CODE: " + code + "========================");


            context.challenge(context.form().setAttribute("realm", context.getRealm()).createForm(TPL_CODE));
        } catch (Exception e) {
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
                                     context.form().setError("emailAuthEmailNotSent", e.getMessage())
                                             .createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        ScaContextHolder scaContextHolder = new ScaContextHolder(context.getHttpRequest());
//        scaContextHolder.getStep().apply(scaContextHolder, context, cmsConnector, aspspConnector);
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
