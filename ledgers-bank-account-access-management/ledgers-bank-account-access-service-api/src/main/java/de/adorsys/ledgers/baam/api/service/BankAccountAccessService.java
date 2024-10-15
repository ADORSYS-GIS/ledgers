/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.api.service;

import de.adorsys.ledgers.baam.db.domain.*;

import java.util.*;

public interface BankAccountAccessService<T extends BankAccountAccess> {

    T createBankAccountAccess(T accessDetails);
    void modifyBankAccountAccess(T bankAccountAccess);
    void suspendBankAccountAccess(String id);
    void reactivateBankAccountAccess(String id);
    void revokeBankAccountAccess(String accessId);
    List<T> getAllBankAccountAccess();
    T getBankAccountAccessById(String id);
}

