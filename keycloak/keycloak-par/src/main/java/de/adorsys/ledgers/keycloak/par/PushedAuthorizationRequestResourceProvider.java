package de.adorsys.ledgers.keycloak.par;

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
    public Response selectMethod(@FormParam("objectId") String objectId,
                                 @FormParam("redirectId") String redirectId) {
        LOG.info("Received PAR request, object ID: " + objectId + ", redirect ID: " + redirectId);
        int a = 0;

        return Response.ok("111", MediaType.APPLICATION_JSON_TYPE).build();
    }


    @Override
    public Object getResource() {
        return this;
    }

    @Override
    public void close() {

    }
}
