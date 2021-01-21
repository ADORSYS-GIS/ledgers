package de.adorsys.ledgers.email.code.api;

import de.adorsys.ledgers.email.code.domain.ScaContextHolder;
import de.adorsys.ledgers.email.code.domain.ScaStatus;
import org.keycloak.models.UserModel;

public interface CmsConnector {
    Object getObject(ScaContextHolder holder);

    void setAuthorizationStatus(ScaContextHolder holder, ScaStatus identified);

    void updateUserData(UserModel user);
}
