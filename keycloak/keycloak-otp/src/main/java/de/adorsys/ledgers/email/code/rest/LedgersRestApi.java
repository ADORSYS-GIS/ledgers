package de.adorsys.ledgers.email.code.rest;

import de.adorsys.ledgers.email.code.domain.EmailRequest;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.models.KeycloakSession;

import java.io.IOException;

public class LedgersRestApi {

    public static int sendEmail(EmailRequest message, String url, KeycloakSession keycloakSession) {
        try {
            return SimpleHttp.doPost(url, keycloakSession)
                    .json(message).asStatus();
        } catch (IOException e) {
            return 500; // TODO
        }
    }

    public static Object getUser(String url, KeycloakSession keycloakSession) {
        try {
            return SimpleHttp.doGet(url, keycloakSession).asJson(Object.class);
        } catch (IOException e) {
            return null; // TODO
        }
    }


}
