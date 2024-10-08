/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.api.service;
import de.adorsys.ledgers.baam.db.domain.AuditorAccess;
import de.adorsys.ledgers.baam.db.domain.BankAccountAccess;

import java.security.Permission;
import java.util.List;

public interface AuditorAccessService extends BankAccountAccessService<AuditorAccess>{
    void createAuditorAccess(BankAccountAccess bankAccountAccess);


}
