/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.db.domain;
import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import lombok.Data;
@Entity
@Data
public class AccountAccessEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID entityId;
    private UUID accountId;
    private String name;
    private UUID template; // Reference to AccountAccessTemplateEntity

    // Constructors, getters and setters
    public AccountAccessEntity() {
    }

    public AccountAccessEntity(UUID entityId, UUID accountId, String name, UUID template) {
        this.entityId = entityId;
        this.accountId = accountId;
        this.name = name;
        this.template = template;
    }

}
