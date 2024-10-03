/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.api.domain.payment;

import de.adorsys.ledgers.deposit.api.domain.account.AccountReferenceTO;
import de.adorsys.ledgers.deposit.api.domain.general.AddressTO;
import lombok.Data;

import java.util.Currency;
import java.util.List;

@Data
public class PaymentTargetTO {
    private String paymentId;
    private String endToEndIdentification;
    private AmountTO instructedAmount;
    private Currency currencyOfTransfer;
    private AccountReferenceTO creditorAccount;
    private String creditorAgent;
    private String creditorName;
    private AddressTO creditorAddress;
    private PurposeCodeTO purposeCode;
    private List<String> remittanceInformationUnstructuredArray;
    private List<RemittanceInformationStructuredTO> remittanceInformationStructuredArray;
    private ChargeBearerTO chargeBearerTO;
}
