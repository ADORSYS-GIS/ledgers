package de.adorsys.ledgers.um.api.domain;

public enum ScaMethodTypeBO {
    EMAIL(false),
    MOBILE(false),
    CHIP_OTP(false),
    PHOTO_OTP(false),
    PUSH_OTP(false),
    SMS_OTP(false),
    APP_OTP(true);

    private final boolean decoupled;

    ScaMethodTypeBO(boolean decoupled) {
        this.decoupled = decoupled;
    }

    public boolean isDecoupled() {
        return decoupled;
    }
}
