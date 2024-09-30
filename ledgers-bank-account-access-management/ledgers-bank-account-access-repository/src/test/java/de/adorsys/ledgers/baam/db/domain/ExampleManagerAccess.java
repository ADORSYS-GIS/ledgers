/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table (name = "manager_access")
public class ExampleManagerAccess  extends ManagerAccess {
}
