/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.middleware.rest.resource;

import de.adorsys.ledgers.middleware.api.domain.oauth.OauthCodeResponseTO;
import de.adorsys.ledgers.middleware.api.domain.oauth.OauthServerInfoTO;
import de.adorsys.ledgers.middleware.api.domain.um.BearerTokenTO;
import de.adorsys.ledgers.middleware.api.service.MiddlewareOauthService;
import de.adorsys.ledgers.middleware.rest.annotation.MiddlewareResetResource;
import de.adorsys.ledgers.middleware.rest.security.ScaInfoHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@MiddlewareResetResource
@RequiredArgsConstructor
@RequestMapping(OauthRestApi.BASE_PATH)
public class OauthResources implements OauthRestApi { //TODO matter of removal
    private final MiddlewareOauthService middlewareOauthService;
    private final ScaInfoHolder scaInfoHolder;

    @Override
    public ResponseEntity<OauthCodeResponseTO> oauthCode(String login, String pin, String redirectUri) {
        return ResponseEntity.ok(middlewareOauthService.oauthCode(login, pin, redirectUri));
    }

    @Override
    public ResponseEntity<OauthCodeResponseTO> oauthCode(String redirectUri) {
        return ResponseEntity.ok(middlewareOauthService.oauthCode(scaInfoHolder.getScaInfo(), redirectUri));
    }

    @Override
    public ResponseEntity<BearerTokenTO> oauthToken(String code) {
        return ResponseEntity.ok(middlewareOauthService.oauthToken(code));
    }

    @Override
    public ResponseEntity<OauthServerInfoTO> oauthServerInfo() {
        return ResponseEntity.ok(middlewareOauthService.oauthServerInfo());
    }
}
