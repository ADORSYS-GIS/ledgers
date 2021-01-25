package de.adorsys.keycloak.connector.aspsp.api;

import de.adorsys.keycloak.otp.core.ScaDataContext;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import org.keycloak.models.UserModel;

import java.util.List;

public interface AspspConnector {

    List<ScaMethod> getMethods(UserModel user);

    void initObj(ScaDataContext scaDataContext, Object object, String login);

    void selectMethod(ScaDataContext scaDataContext, String methodId, String login);

    boolean validateCode(ScaDataContext scaDataContext, String code, String login);

    void execute(ScaDataContext scaDataContext, String login);
}
