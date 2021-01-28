package de.adorsys.ledgers.email.code.rest;

import com.fasterxml.jackson.databind.JsonNode;
import de.adorsys.ledgers.email.code.domain.TokenRequest;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.models.KeycloakSession;

import java.io.IOException;

public class KeycloakRestApi {

    public static JsonNode getToken(String url, TokenRequest tokenRequest, KeycloakSession keycloakSession) {
        try {
            return SimpleHttp.doPost(url, keycloakSession)
                           .header("Content-Type", "application/x-www-form-urlencoded")
                           .param("grant_type", tokenRequest.getGrant_type())
                           .param("username", tokenRequest.getUsername())
                           .param("password", tokenRequest.getPassword())
                           .param("client_id", tokenRequest.getClient_id())
                           .asJson();
        } catch (IOException e) {
            return null; // TODO
        }
    }

}
