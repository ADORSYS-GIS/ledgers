package de.adorsys.ledgers.email.code;

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
        // TODO:
        String enteredCode = context.getHttpRequest().getDecodedFormParameters().getFirst("code");

        AuthenticationSessionModel authSession = context.getAuthenticationSession();
        String code = authSession.getAuthNote("code");
        String ttl = authSession.getAuthNote("ttl");

        if (code == null || ttl == null) {
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
                                     context.form().createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
            return;
        }

        boolean isValid = enteredCode.equals(code);
        if (isValid) {
            if (Long.parseLong(ttl) < System.currentTimeMillis()) {
                // expired
                context.failureChallenge(AuthenticationFlowError.EXPIRED_CODE,
                                         context.form().setError("emailAuthCodeExpired").createErrorPage(Response.Status.BAD_REQUEST));
            } else {
                // valid
                context.success();
            }
        } else {
            // invalid
            AuthenticationExecutionModel execution = context.getExecution();
            if (execution.isRequired()) {
                context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS,
                                         context.form().setAttribute("realm", context.getRealm())
                                                 .setError("emailAuthCodeInvalid").createForm(TPL_CODE));
            } else if (execution.isConditional() || execution.isAlternative()) {
                context.attempted();
            }
        }
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
