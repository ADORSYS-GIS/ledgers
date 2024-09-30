/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table (name = "bank_account_access")
public class ExampleBankAccountAcess  extends BankAccountAccess {
}
