/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.api.service;
import de.adorsys.ledgers.baam.db.domain.AccessScope;
import de.adorsys.ledgers.baam.db.domain.BankAccountAccess;
import de.adorsys.ledgers.baam.db.domain.HolderAccess;


public interface HolderAccessService extends BankAccountAccessService<HolderAccess>{
    void createHolderAccess(BankAccountAccess bankAccountAccess);
    void transferOwnership(String holderId, String newHolderId);
    void suspendAccess(String accessId);
    void grantAccessToRole(String accountId, String roleName, AccessScope accessScope);
    void revokeAccessToRole(String accountId, String roleName);

}
