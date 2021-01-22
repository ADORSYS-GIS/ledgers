package de.adorsys.ledgers.middleware.api.service;

import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTO;

public interface MiddlewareFapiPaymentService {
    void initiatePayment(PaymentTO payment, String login);

    void initiateCancellation(String paymentId, String login);
}
