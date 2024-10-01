package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "third_party_access")
public class ThirdPartyAccess extends BankAccountAccess {

    @Getter
    @Setter
    @Column(name = "consent_type")
    @Enumerated(EnumType.STRING)
    private ConsentType consentType;

    @Getter
    @Setter
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Setter
    @Getter
    @Column(name = "max_transaction_amount")
    private Double maxTransactionAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccessStatus status;

    @Override
    public AccessStatus getStatus() {
        return status;
    }
    @Override
    public void setStatus(AccessStatus status) {
        this.status = status;
    }
}

