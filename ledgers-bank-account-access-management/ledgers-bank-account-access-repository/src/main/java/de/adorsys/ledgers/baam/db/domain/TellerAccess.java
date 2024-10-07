package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TellerAccess extends BankAccountAccess  {

    private double dailyLimit;

}
