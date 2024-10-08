/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.api.service;
import de.adorsys.ledgers.baam.db.domain.BankAccountAccess;
import de.adorsys.ledgers.baam.db.domain.ManagerAccess;
import de.adorsys.ledgers.baam.db.domain.SeniorManagerAccess;
import de.adorsys.ledgers.baam.db.domain.TypeOfManagedAccess;

import java.util.List;

public interface SeniorManagerAccessService extends BankAccountAccessService<SeniorManagerAccess>{
    void createSeniorManagerAccess(BankAccountAccess bankAccountAccess);
    void manageManagerAccess(String managerId, String action, TypeOfManagedAccess typeOfManagedAccess);
    void modifyManagerAccess(String accountId, String managerId);
    void suspendManagerAccess(String accountId, String managerId);
    void reactivateManagerAccess(String accountId, String managerId);
    List<ManagerAccess> getManagerAccessesByAccountId(String accountId);
    ManagerAccess getManagerAccessById(String accountId, String managerId);
}
