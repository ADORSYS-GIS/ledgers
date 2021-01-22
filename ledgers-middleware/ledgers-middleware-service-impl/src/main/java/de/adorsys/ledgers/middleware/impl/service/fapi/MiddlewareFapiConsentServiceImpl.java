package de.adorsys.ledgers.middleware.impl.service.fapi;

import de.adorsys.ledgers.middleware.api.domain.um.AisConsentTO;
import de.adorsys.ledgers.middleware.api.exception.MiddlewareErrorCode;
import de.adorsys.ledgers.middleware.api.exception.MiddlewareModuleException;
import de.adorsys.ledgers.middleware.api.service.MiddlewareFapiConsentService;
import de.adorsys.ledgers.middleware.impl.converter.AisConsentBOMapper;
import de.adorsys.ledgers.middleware.impl.service.SCAUtils;
import de.adorsys.ledgers.um.api.domain.AisConsentBO;
import de.adorsys.ledgers.um.api.domain.UserBO;
import de.adorsys.ledgers.um.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MiddlewareFapiConsentServiceImpl implements MiddlewareFapiConsentService {
    private final UserService userService;
    private final SCAUtils scaUtils;
    private final AisConsentBOMapper aisConsentMapper;

    @Override
    public void initConsent(AisConsentTO consent, String login) {
        UserBO user = scaUtils.userBO(login);
        AisConsentBO consentBO = aisConsentMapper.toAisConsentBO(consent);
        checkAccess(consentBO, user);
        userService.storeConsent(consentBO);
    }

    private void checkAccess(AisConsentBO consent, UserBO user) {
        if (!scaUtils.user(user).hasAccessToAccountsWithIbans(consent.getUniqueIbans())) {
            throw MiddlewareModuleException.builder()
                          .devMsg("You do not have access to requested accounts")
                          .errorCode(MiddlewareErrorCode.INSUFFICIENT_PERMISSION)
                          .build();
        }
    }
}
