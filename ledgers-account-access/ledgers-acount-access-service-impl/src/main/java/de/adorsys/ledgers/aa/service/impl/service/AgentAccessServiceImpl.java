/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.service.impl.service;

import java.util.List;
import java.util.UUID;
import de.adorsys.ledgers.aa.service.api.service.AgentAccessServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.adorsys.ledgers.aa.db.domain.AgentAccessEntity;
import  de.adorsys.ledgers.aa.db.repository.AgentAccessRepository;

@Service
public class AgentAccessServiceImpl implements AgentAccessServiceAPI {

    @Autowired
    private AgentAccessRepository repository;

    @Override
    public AgentAccessEntity createAccess(AgentAccessEntity access) {
        return repository.save(access);
    }

    @Override
    public AgentAccessEntity getAccessById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<AgentAccessEntity> getAllAccesses() {
        return repository.findAll();
    }

    @Override
    public AgentAccessEntity updateAccess(UUID id, AgentAccessEntity access) {
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
