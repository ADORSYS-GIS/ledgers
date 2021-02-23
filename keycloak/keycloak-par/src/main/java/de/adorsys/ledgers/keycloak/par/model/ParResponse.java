package de.adorsys.ledgers.keycloak.par.model;

public class ParResponse {

    String grantId;
    String redirectId;

    public ParResponse(String grantId, String redirectUri) {
        this.grantId = grantId;
        this.redirectId = redirectUri;
    }

    public String getGrantId() {
        return grantId;
    }

    public void setGrantId(String grantId) {
        this.grantId = grantId;
    }

    public String getRedirectId() {
        return redirectId;
    }

    public void setRedirectId(String redirectId) {
        this.redirectId = redirectId;
    }
}
