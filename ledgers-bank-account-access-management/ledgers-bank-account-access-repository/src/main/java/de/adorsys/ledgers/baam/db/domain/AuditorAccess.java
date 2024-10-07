package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "auditor_access")
public class AuditorAccess extends BankAccountAccess {

    public AuditorAccess() {
        super();
        this.setStatus(AccessStatus.ACTIVE);  // By default, AuditorAccess is active
        this.setWeight(1.0);                  // Weight is always 1 for Auditor Access
    }

}

