/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.service.api.service;

import java.util.List;
import java.util.UUID;
import de.adorsys.ledgers.aa.db.domain.AccountAccessTemplateEntity;

public interface AccountAccessTemplateServiceAPI {

    AccountAccessTemplateEntity createTemplate(AccountAccessTemplateEntity template);
    AccountAccessTemplateEntity getTemplateById(UUID id);
    List<AccountAccessTemplateEntity> getAllTemplates();
    AccountAccessTemplateEntity updateTemplate(UUID id, AccountAccessTemplateEntity template);
    void deleteTemplate(UUID id);
}
