package de.adorsys.keycloak.otp.core.domain;

public class CodeValidationResult {
    private boolean valid;
    private boolean multilevelScaRequired;

    public CodeValidationResult() {
    }

    public CodeValidationResult(boolean valid, boolean multilevelScaRequired) {
        this.valid = valid;
        this.multilevelScaRequired = multilevelScaRequired;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isMultilevelScaRequired() {
        return multilevelScaRequired;
    }

    public void setMultilevelScaRequired(boolean multilevelScaRequired) {
        this.multilevelScaRequired = multilevelScaRequired;
    }
}
