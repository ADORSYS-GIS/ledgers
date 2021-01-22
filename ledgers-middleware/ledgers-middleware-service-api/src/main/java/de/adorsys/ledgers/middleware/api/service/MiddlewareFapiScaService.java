package de.adorsys.ledgers.middleware.api.service;

import de.adorsys.ledgers.middleware.api.domain.sca.GlobalScaResponseTO;
import de.adorsys.ledgers.middleware.api.domain.sca.StartScaOprTO;

public interface MiddlewareFapiScaService {
    GlobalScaResponseTO startSca(StartScaOprTO scaOpr, String methodId, String login);

    GlobalScaResponseTO validateCode(String authId, String code, String login);
}
