package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class PoAAccess extends BankAccountAccess {

    private String attorneyInFact;

    private LocalDateTime validFrom;

    private LocalDateTime validUntil;

    private PoAType poaType;
    private double weight;

    public PoAAccess() {

    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(validFrom) && now.isBefore(validUntil);
    }
}


