package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "holder_access")
@Data
@NoArgsConstructor(force = true)
public class HolderAccess extends BankAccountAccess {

    // Constructor for creating HolderAccess with full access
    public HolderAccess(String entityId, String accountId) {
        super();
        this.setEntityId(entityId);
        this.setAccountId(accountId);
    }
}