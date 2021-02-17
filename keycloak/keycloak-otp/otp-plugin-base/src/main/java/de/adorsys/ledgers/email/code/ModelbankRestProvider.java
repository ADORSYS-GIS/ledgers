package de.adorsys.ledgers.email.code;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class ModelbankRestProvider implements RealmResourceProvider {

    private final KeycloakSession session;

    public ModelbankRestProvider(KeycloakSession session) {
        this.session = session;
    }


    // TODO: delete this class????



//    @POST
//    @Path("/method/select")
//    @NoCache
//    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
//    public void selectMethod(@FormParam("methodId") String methodId,
//                             @FormParam("oprId") String oprId,
//                             @FormParam("externalId") String externalId,
//                             @FormParam("authorisationId") String authorisationId,
//                             @FormParam("opType") String opType,
//                             @FormParam("login") String login) {
//        LedgersConnectorImpl ledgersConnector = new LedgersConnectorImpl();
//        ledgersConnector.setKeycloakSession(session);
//
//        ScaContextHolder scaDataContext = new ScaContextHolder(externalId, authorisationId, opType);
//        ledgersConnector.selectMethod(scaDataContext, methodId, login);
//    }

    @Override
    public Object getResource() {
        return this;
    }

    @Override
    public void close() {

    }
}
