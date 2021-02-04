package de.adorsys.keycloak.connector.cms.api;

public class ConfirmationObject<T> {
    private T rawBusinessObject;
    private String objType;
    private String description;
    private String id;
    private String additionalInfo;
    private String displayInfo;

    public ConfirmationObject() {
    }

    public ConfirmationObject(T rawBusinessObject, String objType, String description, String id, String additionalInfo, String displayInfoPattern, String... displayInfoParams) {
        this.rawBusinessObject = rawBusinessObject;
        this.objType = objType;
        this.description = description;
        this.id = id;
        this.additionalInfo = additionalInfo;
        this.displayInfo = String.format(displayInfoPattern, (Object[]) displayInfoParams);
    }

    public T getRawBusinessObject() {
        return rawBusinessObject;
    }

    public void setRawBusinessObject(T rawBusinessObject) {
        this.rawBusinessObject = rawBusinessObject;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getDisplayInfo() {
        return displayInfo;
    }

    public void setDisplayInfo(String displayInfo) {
        this.displayInfo = displayInfo;
    }
}
