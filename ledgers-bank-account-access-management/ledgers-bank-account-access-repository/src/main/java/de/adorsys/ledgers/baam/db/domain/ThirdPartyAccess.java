package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "third_party_access")
public class ThirdPartyAccess extends BankAccountAccess {

    @Column(name = "consent_type")
    @Enumerated(EnumType.STRING)
    private ConsentType consentType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "max_transaction_amount")
    private Double maxTransactionAmount;

}

