package de.adorsys.ledgers.email.code.domain;

public class ScaMethod {
    private String id;
    private String type;
    private String description;

    public ScaMethod() {
    }

    public ScaMethod(String id, String type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
