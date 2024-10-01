/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.api.domain.account;

import de.adorsys.ledgers.deposit.api.domain.payment.AmountTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundsConfirmationRequestTO {
    private String psuId;
    private AccountReferenceTO psuAccount;
    private AmountTO instructedAmount;
    private String cardNumber;
    private String payee;
}
