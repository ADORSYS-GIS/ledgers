/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@MappedSuperclass
@Data
@NoArgsConstructor(force = true)
public abstract class BankAccountAccess {

    @Id
    @Column(name = "bank_account_access_id")
    private String id;

    @NotNull
    private String accountId;

    @NotNull
    private String entityId;

    public BankAccountAccess(String accountId, String entityId) {
        this.accountId = accountId;
        this.entityId = entityId;
    }

    @Enumerated(EnumType.STRING)
    private AccessScope scope; // Allowed actions

    private double weight; // Level of access (0 to 1)

    @Enumerated(EnumType.STRING)
    private AccessCondition conditions; // Conditions on access

    @Enumerated(EnumType.STRING)
    private AccessStatus status; // Active, restricted, or suspended

    private String policies; // Policies associated with the access

}
