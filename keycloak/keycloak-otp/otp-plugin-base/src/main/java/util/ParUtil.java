package util;

import de.adorsys.ledgers.email.code.PushedAuthorizationRequest;
import org.keycloak.authentication.AuthenticationFlowContext;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ParUtil {

    public static PushedAuthorizationRequest getParFromContext(AuthenticationFlowContext context) {
        // Loading encoded custom parameters as "grand ID" from realm:
        String grantId = context.getRealm().getAttribute("grantId");

        String decodedGrantId = new String(Base64.getDecoder().decode(grantId));
        List<String> parameters = Arrays.asList(decodedGrantId.split(","));

        if (parameters.isEmpty()) {
            throw new RuntimeException("No parameters were added during PAR processing, flow is broken.");
        }

        String businessObjectId = parameters.get(0);
        String authId = parameters.get(1);
        String objectType = parameters.get(2);

        return new PushedAuthorizationRequest(businessObjectId, authId, objectType);
    }

}
