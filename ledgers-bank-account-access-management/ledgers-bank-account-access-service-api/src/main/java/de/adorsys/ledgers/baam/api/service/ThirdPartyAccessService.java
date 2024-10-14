/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.api.service;
import de.adorsys.ledgers.baam.db.domain.*;

import java.util.List;

public interface ThirdPartyAccessService extends BankAccountAccessService<ThirdPartyAccess>{
    void createThirdPartyAccess(BankAccountAccess bankAccountAccess);
    void grantThirdPartyAccess(String accountId, ConsentType consentType, AccessScope accessLevel);
    void revokeThirdPartyAccess(String accountId, String providerId);
    void modifyThirdPartyAccess(String accountId, String providerId, ConsentType consentType, AccessScope accessLevel);
    List<ThirdPartyAccess> getThirdPartyAccessesByAccountId(String accountId);
    ThirdPartyAccess getThirdPartyAccessByProviderId(String accountId, String providerId);
}

