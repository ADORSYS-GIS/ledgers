/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.api.service;
import de.adorsys.ledgers.baam.db.domain.BankAccountAccess;
import de.adorsys.ledgers.baam.db.domain.ManagerAccess;

public interface ManagerAccessService extends BankAccountAccessService<ManagerAccess>{
    void createManagerAccess(BankAccountAccess bankAccountAccess);
    void impersonateHolder(Long holderId);
    ManagerAccess grantManagerAccess(String accountId, ManagerAccess managerAccess);
    ManagerAccess revokeManagerAccess(String accessId);
}
