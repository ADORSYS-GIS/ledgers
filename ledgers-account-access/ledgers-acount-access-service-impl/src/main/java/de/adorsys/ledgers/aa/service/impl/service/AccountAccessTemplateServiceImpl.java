/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.service.impl.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.adorsys.ledgers.aa.service.api.service.AccountAccessTemplateServiceAPI;
import de.adorsys.ledgers.aa.db.domain.AccountAccessTemplateEntity;
import de.adorsys.ledgers.aa.db.repository.AccountAccessTemplateRepository;

@Service
public class AccountAccessTemplateServiceImpl implements AccountAccessTemplateServiceAPI {

    @Autowired
    private AccountAccessTemplateRepository repository;

    @Override
    public AccountAccessTemplateEntity createTemplate(AccountAccessTemplateEntity template) {
        return repository.save(template);
    }

    @Override
    public AccountAccessTemplateEntity getTemplateById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<AccountAccessTemplateEntity> getAllTemplates() {
        return repository.findAll();
    }

    @Override
    public AccountAccessTemplateEntity updateTemplate(UUID id, AccountAccessTemplateEntity template) {
        if (repository.existsById(id)) {
            template.setId(id);
            return repository.save(template);
        }
        return null;
    }

    @Override
    public void deleteTemplate(UUID id) {
        repository.deleteById(id);
    }
}