package de.adorsys.ledgers.email.code;

import de.adorsys.keycloak.connector.aspsp.LedgersConnectorImpl;
import de.adorsys.keycloak.otp.core.ScaDataContext;
import de.adorsys.keycloak.otp.core.domain.ScaMethod;
import de.adorsys.ledgers.email.code.domain.ScaContextHolder;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.models.KeycloakContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.utils.MediaType;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

public class ModelbankRestProvider implements RealmResourceProvider {

    private final KeycloakSession session;

    public ModelbankRestProvider(KeycloakSession session) {
        this.session = session;
    }

    @POST
    @Path("/method/select")
    @NoCache
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public void selectMethod(@FormParam("methodId") String methodId,
                             @FormParam("oprId") String oprId,
                             @FormParam("externalId") String externalId,
                             @FormParam("authorisationId") String authorisationId,
                             @FormParam("opType") String opType,
                             @FormParam("login") String login) {
        LedgersConnectorImpl ledgersConnector = new LedgersConnectorImpl();
        ledgersConnector.setKeycloakSession(session);

        ScaDataContext scaDataContext = new ScaContextHolder(externalId, authorisationId, opType);
        ledgersConnector.selectMethod(scaDataContext, methodId, login);
    }

    @POST
    @Path("/object/submit")
    @NoCache
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public void submitObject(@FormParam("objId") String objId,
                             @FormParam("authId") String authId,
                             @FormParam("objType") String objType,
                             @FormParam("step") String step) {
        LedgersConnectorImpl ledgersConnector = new LedgersConnectorImpl();
        ledgersConnector.setKeycloakSession(session);

    }


    @Override
    public Object getResource() {
        return this;
    }

    @Override
    public void close() {

    }
}
