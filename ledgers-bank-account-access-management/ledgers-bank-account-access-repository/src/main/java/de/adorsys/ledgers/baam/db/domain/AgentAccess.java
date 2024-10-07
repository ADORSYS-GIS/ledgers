package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "agent_access")
public class AgentAccess extends BankAccountAccess {

    public AgentAccess() {
        super();
    }
}
