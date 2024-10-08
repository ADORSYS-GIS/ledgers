/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.api.service;
import de.adorsys.ledgers.baam.db.domain.AgentAccess;
import de.adorsys.ledgers.baam.db.domain.BankAccountAccess;

public interface AgentAccessService extends BankAccountAccessService<AgentAccess> {
    void createAgentAccess(BankAccountAccess bankAccountAccess);
    AgentAccess impersonateHolder(String accessId);
    AgentAccess endImpersonation(String accessId);
}
