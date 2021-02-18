package de.adorsys.keycloak.connector.aspsp;

import com.fasterxml.jackson.core.type.TypeReference;
import de.adorsys.keycloak.otp.core.domain.ScaContextHolder;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import de.adorsys.ledgers.middleware.api.domain.sca.OpTypeTO;
import de.adorsys.ledgers.middleware.api.domain.sca.StartScaOprTO;
import de.adorsys.ledgers.middleware.api.domain.um.ScaUserDataTO;

import java.util.List;
import java.util.stream.Collectors;

public class AspspMapper {
    public static final TypeReference<List<ScaUserDataTO>> SCA_DATA_LIST_TYPE = new TypeReference<>() {
    };

    public static OpTypeTO mapOperationType(String objectType) {
        if (objectType.equalsIgnoreCase("payment")) {
            return OpTypeTO.PAYMENT;
        }
        if (objectType.equalsIgnoreCase("cancel_payment")) {
            return OpTypeTO.CANCEL_PAYMENT;
        }
        if (objectType.equalsIgnoreCase("consent")) {
            return OpTypeTO.CONSENT;
        }
        throw new IllegalArgumentException("Unsupported object type: " + objectType);
    }

    public static List<ScaMethod> mapToScaMethodList(List<ScaUserDataTO> list) {
        return list.stream()
                       .map(i -> new ScaMethod(i.getId(), i.getScaMethod().name(), i.getMethodValue(), i.isDecoupled()))
                       .collect(Collectors.toList());
    }

    private AspspMapper() { //stub
    }

    public static StartScaOprTO mapStartScaOpr(ScaContextHolder scaDataContext) {
        return new StartScaOprTO(scaDataContext.getObjId(), null, scaDataContext.getAuthId(), mapOperationType(scaDataContext.getObjType()));
    }
}
