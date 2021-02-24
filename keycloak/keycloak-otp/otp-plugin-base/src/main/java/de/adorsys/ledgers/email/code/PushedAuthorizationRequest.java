package de.adorsys.ledgers.email.code;

public class PushedAuthorizationRequest {

    private String businessObjectId;
    private String authorizationId;
    private String objectType;

    public PushedAuthorizationRequest(String businessObjectId, String authorizationId, String objectType) {
        this.businessObjectId = businessObjectId;
        this.authorizationId = authorizationId;
        this.objectType = objectType;
    }

    public String getBusinessObjectId() {
        return businessObjectId;
    }

    public void setBusinessObjectId(String businessObjectId) {
        this.businessObjectId = businessObjectId;
    }

    public String getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(String authorizationId) {
        this.authorizationId = authorizationId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
}
