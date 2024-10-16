/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.service.api.service;

import java.util.List;
import java.util.UUID;
import de.adorsys.ledgers.aa.db.domain.AccountAccessEntity;

public interface AccountAccessServiceAPI {

    AccountAccessEntity createAccess(AccountAccessEntity access);
    AccountAccessEntity getAccessById(UUID id);
    List<AccountAccessEntity> getAllAccesses();
    AccountAccessEntity updateAccess(UUID id, AccountAccessEntity access);
    void deleteAccess(UUID id);
}
