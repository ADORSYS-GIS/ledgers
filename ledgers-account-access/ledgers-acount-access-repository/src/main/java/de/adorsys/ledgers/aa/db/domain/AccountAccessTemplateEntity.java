/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.db.domain;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ElementCollection;

@Entity
@Data
public class AccountAccessTemplateEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String templateName;
    @ElementCollection
    private List<String> permissions;
    @ElementCollection
    private List<String> parameters;

    // Constructors, getters and setters
    public AccountAccessTemplateEntity() {
    }

    public AccountAccessTemplateEntity(String templateName, List<String> permissions, List<String> parameters) {
        this.templateName = templateName;
        this.permissions = permissions;
        this.parameters = parameters;
    }
}
