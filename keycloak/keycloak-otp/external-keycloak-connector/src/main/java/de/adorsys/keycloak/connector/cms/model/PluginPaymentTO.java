package de.adorsys.keycloak.connector.cms.model;

import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTO;
import de.adorsys.psd2.xs2a.core.profile.PaymentType;

public class PluginPaymentTO extends PaymentTO {

    private PaymentType paymentType;

    public PluginPaymentTO() {
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
}
