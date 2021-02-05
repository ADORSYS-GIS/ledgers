package de.adorsys.keycloak.connector.aspsp.api;

import de.adorsys.keycloak.otp.core.ScaDataContext;
import de.adorsys.keycloak.otp.core.domain.CodeValidationResult;
import de.adorsys.keycloak.otp.core.domain.ConfirmationObject;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import org.keycloak.models.UserModel;

import java.util.List;

public interface AspspConnector {

    List<ScaMethod> getMethods(UserModel user);

    <T> void initObj(ScaDataContext scaDataContext, ConfirmationObject<T> object, String login);

    void selectMethod(ScaDataContext scaDataContext, String methodId, String login);

    CodeValidationResult validateCode(ScaDataContext scaDataContext, String code, String login);

    void execute(ScaDataContext scaDataContext, String login);
}
