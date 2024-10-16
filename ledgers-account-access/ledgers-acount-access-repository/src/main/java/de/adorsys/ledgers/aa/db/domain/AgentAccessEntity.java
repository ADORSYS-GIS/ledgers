/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.db.domain;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class AgentAccessEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID agentId;
    private UUID accountAccessId;
    private UUID subTemplate; // Reference to AccountAccessTemplateEntity

    // Constructors, getters and setters
    public AgentAccessEntity() {
    }

    public AgentAccessEntity(UUID agentId, UUID accountAccessId, UUID subTemplate) {
        this.agentId = agentId;
        this.accountAccessId = accountAccessId;
        this.subTemplate = subTemplate;
    }
}
