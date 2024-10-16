/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.service.api.service;

import java.util.List;
import java.util.UUID;
import   de.adorsys.ledgers.aa.db.domain.AgentAccessEntity;
public interface AgentAccessServiceAPI {

    AgentAccessEntity createAccess(AgentAccessEntity access);
    AgentAccessEntity getAccessById(UUID id);
    List<AgentAccessEntity> getAllAccesses();
    AgentAccessEntity updateAccess(UUID id, AgentAccessEntity access);
    void deleteAccess(UUID id);
}
