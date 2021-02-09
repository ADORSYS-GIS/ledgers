package de.adorsys.keycloak.otp.core.domain;

import org.jboss.resteasy.spi.HttpRequest;

public class ScaContextHolder {
    private String objId;
    private String authId;
    private String objType;
    private Step step;

    public ScaContextHolder(HttpRequest request) {
        this.objId = request.getDecodedFormParameters().getFirst("objId");
        this.authId = request.getDecodedFormParameters().getFirst("authId");
        this.objType = request.getDecodedFormParameters().getFirst("objType");
        this.step = Step.valueOf(request.getDecodedFormParameters().getFirst("step"));
    }

    public ScaContextHolder(String objId, String authId, String objType) {
        this.objId = objId;
        this.authId = authId;
        this.objType = objType;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }
}
