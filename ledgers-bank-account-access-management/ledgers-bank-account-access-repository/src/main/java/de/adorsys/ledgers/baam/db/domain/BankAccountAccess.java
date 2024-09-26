/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

@Entity
public class BankAccountAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String accountId;

    @NotNull
    private String entityId;

    @Enumerated(EnumType.STRING)
    private AccessScope scope; // Allowed actions

    private double weight; // Level of access (0 to 1)

    @Enumerated(EnumType.STRING)
    private AccessCondition conditions; // Conditions on access

    @Enumerated(EnumType.STRING)
    private AccessStatus status; // Active, restricted, or suspended

    private String policies; // Policies associated with the access

}
