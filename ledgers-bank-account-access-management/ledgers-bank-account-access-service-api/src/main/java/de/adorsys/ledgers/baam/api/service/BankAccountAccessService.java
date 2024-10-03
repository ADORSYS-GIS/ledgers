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

    T modifyBankAccountAccess(String accessId, T accessDetails);

    Optional<T> suspendBankAccountAccess(String accessId);

    Optional<T> reactivateBankAccountAccess(String accessId);

    Optional<T> deleteBankAccountAccess(String accessId);
}

