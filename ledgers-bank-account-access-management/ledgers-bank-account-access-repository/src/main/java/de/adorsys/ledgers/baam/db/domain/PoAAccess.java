package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class PoAAccess extends BankAccountAccess {

    // Getters and Setters
    @Getter
    @Setter
    private String attorneyInFact;
    @Getter
    @Setter
    private LocalDateTime validFrom;
    @Getter
    @Setter
    private LocalDateTime validUntil;
    @Getter
    @Setter
    private PoAType poaType;
    private double weight;

    public PoAAccess() {

    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(validFrom) && now.isBefore(validUntil);
    }
}


