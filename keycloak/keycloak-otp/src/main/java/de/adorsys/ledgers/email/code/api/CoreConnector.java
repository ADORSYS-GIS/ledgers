package de.adorsys.ledgers.email.code.api;

import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import de.adorsys.ledgers.email.code.domain.ScaContextHolder;
import org.keycloak.models.UserModel;

import java.util.List;

@Deprecated
public interface CoreConnector {
    List<ScaMethod> getMethods(UserModel user);

    void initObj(ScaContextHolder holder, Object object);

    void selectMethod(ScaContextHolder holder, String methodId);

    boolean validateCode(ScaContextHolder holder, String code);

    void execute(ScaContextHolder holder);
}
