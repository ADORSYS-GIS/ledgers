/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.api.service;
import de.adorsys.ledgers.baam.db.domain.BankAccountAccess;
import de.adorsys.ledgers.baam.db.domain.TellerAccess;

public interface TellerAccessService extends BankAccountAccessService<TellerAccess>{
    void createTellerAccess(BankAccountAccess bankAccountAccess);

}
