package de.adorsys.ledgers.email.code.domain;

public class CmsTokenRequest {

    private String businessObjectId;
    private String aspspConsentDataBase64;

    public String getBusinessObjectId() {
        return businessObjectId;
    }

    public void setBusinessObjectId(String businessObjectId) {
        this.businessObjectId = businessObjectId;
    }

    public String getAspspConsentDataBase64() {
        return aspspConsentDataBase64;
    }

    public void setAspspConsentDataBase64(String aspspConsentDataBase64) {
        this.aspspConsentDataBase64 = aspspConsentDataBase64;
    }
}
