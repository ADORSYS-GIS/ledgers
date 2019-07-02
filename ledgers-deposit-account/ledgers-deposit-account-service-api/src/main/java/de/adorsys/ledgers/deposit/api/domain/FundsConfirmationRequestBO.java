package de.adorsys.ledgers.deposit.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundsConfirmationRequestBO {
    private String psuId;
    private AccountReferenceBO psuAccount;
    private AmountBO instructedAmount;
    private String cardNumber;
    private String payee;
}
