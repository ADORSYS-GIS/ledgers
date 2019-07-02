package de.adorsys.ledgers.deposit.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDetailsBO {
    private String transactionId;
    private String entryReference;
    private String endToEndId;
    private String mandateId;
    private String checkId;
    private String creditorId;
    private LocalDate bookingDate;
    private LocalDate valueDate;
    private AmountBO transactionAmount;
    private List<ExchangeRateBO> exchangeRate;
    private String creditorName;
    private AccountReferenceBO creditorAccount;
    private String ultimateCreditor;
    private String debtorName;
    private AccountReferenceBO debtorAccount;
    private String ultimateDebtor;
    private String remittanceInformationStructured;
    private String remittanceInformationUnstructured;
    private PurposeCodeBO purposeCode;
    private String bankTransactionCode;
    private String proprietaryBankTransactionCode;
}
