/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.api.service;
import de.adorsys.ledgers.baam.db.domain.BankAccountAccess;
import de.adorsys.ledgers.baam.db.domain.DelegatedAccess;

import java.util.List;

public interface DelegatedAccessService extends BankAccountAccessService<DelegatedAccess> {
    DelegatedAccess createDelegatedAccess(BankAccountAccess bankAccountAccess);
    void manageDelegatedAccess(String delegateId, String action, DelegatedAccess delegatedAccess);
    void revokeDelegatedAccess(String accessId);
    List<DelegatedAccess> listDelegatedAccessByAccountId(String accountId);
}
