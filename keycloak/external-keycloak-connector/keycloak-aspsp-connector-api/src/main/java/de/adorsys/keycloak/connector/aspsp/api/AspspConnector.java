package de.adorsys.keycloak.connector.aspsp.api;

import de.adorsys.keycloak.otp.core.ScaDataContext;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import org.keycloak.models.UserModel;

import java.util.List;

public interface AspspConnector {

    List<ScaMethod> getMethods(UserModel user);

    void initObj(ScaDataContext scaDataContext, Object object);

    void selectMethod(ScaDataContext scaDataContext, String methodId);

    boolean validateCode(ScaDataContext scaDataContext, String code);

    void execute(ScaDataContext scaDataContext);
}
