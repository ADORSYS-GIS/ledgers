package de.adorsys.ledgers.email.code;

import org.keycloak.credential.PasswordCredentialProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;

public class PassProvider extends PasswordCredentialProvider {
    public PassProvider(KeycloakSession session) {
        super(session);
    }

    public PasswordCredentialModel getPassword(RealmModel realmModel, UserModel userModel) {

        return super.getPassword(realmModel, userModel);

    }
}
