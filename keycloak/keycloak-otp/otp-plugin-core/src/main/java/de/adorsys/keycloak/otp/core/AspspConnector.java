package de.adorsys.keycloak.otp.core;

import de.adorsys.keycloak.otp.core.domain.CodeValidationResult;
import de.adorsys.keycloak.otp.core.domain.ConfirmationObject;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import org.keycloak.models.UserModel;
import org.keycloak.provider.Provider;

import java.util.List;

public interface AspspConnector extends Provider {

    List<ScaMethod> getMethods(UserModel user);

    <T> void initObj(ScaContextHolder scaDataContext, ConfirmationObject<T> object, String login);

    String selectMethod(ScaContextHolder scaDataContext, String methodId, String login);

    CodeValidationResult validateCode(ScaContextHolder scaDataContext, String code, String login);

    void execute(ScaContextHolder scaDataContext, String login);
}
