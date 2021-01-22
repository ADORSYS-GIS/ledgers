package de.adorsys.ledgers.email.code;

import com.fasterxml.jackson.databind.JsonNode;
import de.adorsys.ledgers.email.code.domain.EmailRequest;
import de.adorsys.ledgers.email.code.domain.TokenRequest;
import de.adorsys.ledgers.email.code.rest.KeycloakRestApi;
import de.adorsys.ledgers.email.code.rest.LedgersRestApi;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.*;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.protocol.oidc.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.services.util.DefaultClientSessionContext;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.theme.Theme;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This implementation allows us to send one-time password via email. To make it possible, you have to configure
 * the Keycloak admin console UI:
 * <p>
 * - Add email address to the necessary user.
 * - "Authentication" - create copy of "Browser" - "Actions" - "Add executions" - "Email OTP" - "Save".
 * - "Bindings" - "Browser flow" - set the flow you have created before - "Save".
 * - "Authentication" - "Required actions" - "Configure OTP" - set as a default action.
 */
@SuppressWarnings("PMD")
public class EmailCodeAuthenticator implements Authenticator {

    private static final Logger LOG = Logger.getLogger(EmailCodeAuthenticator.class);

    private static final String TPL_CODE = "login-email.ftl";

    private final TokenManager tokenManager = new TokenManager();

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        UserModel user = context.getUser();
        String userEmail = user.getEmail();

        int length = Integer.parseInt(config.getConfig().get("length"));
        int ttl = Integer.parseInt(config.getConfig().get("ttl"));

        String code = generateAuthCode(length);
        AuthenticationSessionModel authSession = context.getAuthenticationSession();
        authSession.setAuthNote("code", code);
        authSession.setAuthNote("ttl", Long.toString(System.currentTimeMillis() + (ttl * 1000L)));

        try {
            LOG.debug("Sending OTP: " + code + " to user with email: " + userEmail);

            EmailRequest message = getMessage(context, userEmail, ttl, code);
            KeycloakSession session = context.getSession();

            LedgersRestApi.sendEmail(message, "http://localhost:8088/fapi/email", session);

            JsonNode userRetrieved = LedgersRestApi.getUser("http://localhost:8088/users/me", session);

            context.challenge(context.form().setAttribute("realm", context.getRealm()).createForm(TPL_CODE));
        } catch (Exception e) {
            LOG.error("Sending OTP failed.");
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
                                     context.form().setError("emailAuthEmailNotSent", e.getMessage())
                                             .createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
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
        // TODO:
        // We must check - "if this user has SCA methods?" To do this, call ledgers with token to "/me" endpoint.
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

    private EmailRequest getMessage(AuthenticationFlowContext context, String userEmail, int ttl, String code) throws IOException {
        KeycloakSession session = context.getSession();
        UserModel user = context.getUser();

        Theme theme = session.theme().getTheme(Theme.Type.LOGIN);
        Locale locale = session.getContext().resolveLocale(user);
        String emailAuthText = theme.getMessages(locale).getProperty("emailAuthText");
        String emailText = String.format(emailAuthText, code, Math.floorDiv(ttl, 60));

        EmailRequest message = new EmailRequest();
        message.setFrom("robot");
        message.setBody(emailText);
        message.setSubject("Your OTP for authentication");
        message.setTo(userEmail);
        return message;
    }

}
