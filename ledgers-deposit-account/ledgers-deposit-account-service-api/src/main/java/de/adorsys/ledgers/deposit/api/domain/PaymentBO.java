package de.adorsys.ledgers.deposit.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentBO {
    private String paymentId;
    private Boolean batchBookingPreferred;
    private LocalDate requestedExecutionDate;
    private LocalTime requestedExecutionTime;
    private PaymentTypeBO paymentType;
    private String paymentProduct;
    private LocalDate startDate;
    private LocalDate endDate;
    private String executionRule;
    private FrequencyCodeBO frequency;
    private Integer dayOfExecution; //Day here max 31
    private AccountReferenceBO debtorAccount;
    private String debtorName;
    private String debtorAgent;
    private TransactionStatusBO transactionStatus;
    private List<PaymentTargetBO> targets = new ArrayList<>();
    private String accountId;

    public boolean isValidAmount() {
        return targets.stream()
                       .map(PaymentTargetBO::getInstructedAmount)
                       .allMatch(a -> a.getAmount().compareTo(BigDecimal.ZERO) > 0
                                              && a.getAmount().scale() < 3);
    }

    public void updateDebtorAccountCurrency(Currency currency) {
        debtorAccount.setCurrency(currency);
    }
}
