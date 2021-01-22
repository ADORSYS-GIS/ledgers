package de.adorsys.ledgers.middleware.api.service;

import de.adorsys.ledgers.middleware.api.domain.um.AisConsentTO;

public interface MiddlewareFapiConsentService {

    void initConsent(AisConsentTO consent, String login);
}
