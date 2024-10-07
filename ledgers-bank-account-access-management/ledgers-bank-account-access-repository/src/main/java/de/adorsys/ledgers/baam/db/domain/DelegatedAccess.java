package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class DelegatedAccess extends BankAccountAccess {

    private DelegatedAccessType delegatedType;

    public DelegatedAccess() {
       super();
    }

}


