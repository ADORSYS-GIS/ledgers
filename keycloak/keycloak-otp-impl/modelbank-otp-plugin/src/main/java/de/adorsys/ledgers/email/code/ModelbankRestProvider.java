package de.adorsys.ledgers.email.code;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.utils.MediaType;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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
        // TODO: receive the form data and proceed to ledgers - start sca.
        int a = 0;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @Override
    public void close() {

    }
}
