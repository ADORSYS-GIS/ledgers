package de.adorsys.ledgers.keycloak.par;

import de.adorsys.ledgers.keycloak.par.model.ParResponse;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Base64;

public class PushedAuthorizationRequestResourceProvider implements RealmResourceProvider {

    private static final Logger LOG = Logger.getLogger(PushedAuthorizationRequestResourceProvider.class);

    private final KeycloakSession session;

    public PushedAuthorizationRequestResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @POST
    @Path("/par")
    @NoCache
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Response handlePar(@FormParam("objectId") String objectId,
                              @FormParam("redirectId") String redirectId,
                              @FormParam("objectType") String objectType) {
        LOG.info("Received PAR request, object ID: " + objectId + ", redirect ID: " + redirectId
                         + ", object type: " + objectType);
        String grantId = Base64.getEncoder().encodeToString(String.format("%s,%s,%s", objectId, redirectId, objectType).getBytes());

        // Temporary solution - storing encoded custom parameters inside the realm attribute,
        session.getContext().getRealm().setAttribute("grantId", grantId);

        String redirectUri = session.getContext().getAuthServerUrl().toASCIIString();
        ParResponse response = new ParResponse(grantId, redirectUri);

        return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
    }


    @Override
    public Object getResource() {
        return this;
    }

    @Override
    public void close() {

    }
}
