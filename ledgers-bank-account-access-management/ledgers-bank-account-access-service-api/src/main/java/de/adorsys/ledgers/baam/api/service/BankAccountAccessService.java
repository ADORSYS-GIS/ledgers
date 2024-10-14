/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.api.service;

import de.adorsys.ledgers.baam.db.domain.*;

import java.util.List;
import java.util.Optional;

public interface BankAccountAccessService<T extends BankAccountAccess> {

    T createBankAccountAccess(String accountId, String entityId, T accessDetails);

    void modifyBankAccountAccess(T bankAccountAccess);
    void suspendBankAccountAccess(String id);
    void reactivateBankAccountAccess(String id);
    Optional<Boolean> revokeBankAccountAccess(String accessId);
    List<T> getAllBankAccountAccess();
    T getBankAccountAccessById(String id);
}

