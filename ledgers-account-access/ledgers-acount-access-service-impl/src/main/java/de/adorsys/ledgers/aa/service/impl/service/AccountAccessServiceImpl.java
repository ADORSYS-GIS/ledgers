/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.service.impl.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.adorsys.ledgers.aa.service.api.service.AccountAccessServiceAPI;
import de.adorsys.ledgers.aa.db.domain.AccountAccessEntity;
import de.adorsys.ledgers.aa.db.repository.AccountAccessRepository;
@Service
public class AccountAccessServiceImpl implements AccountAccessServiceAPI {

    @Autowired
    private AccountAccessRepository repository;

    @Override
    public AccountAccessEntity createAccess(AccountAccessEntity access) {
        return repository.save(access);
    }

    @Override
    public AccountAccessEntity getAccessById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<AccountAccessEntity> getAllAccesses() {
        return repository.findAll();
    }

    @Override
    public AccountAccessEntity updateAccess(UUID id, AccountAccessEntity access) {
        if (repository.existsById(id)) {
            access.setId(id);
            return repository.save(access);
        }
        return null;
    }

    @Override
    public void deleteAccess(UUID id) {
        repository.deleteById(id);
    }
}
