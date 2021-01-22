package de.adorsys.ledgers.middleware.impl.service.fapi;

import de.adorsys.ledgers.deposit.api.domain.PaymentBO;
import de.adorsys.ledgers.deposit.api.domain.TransactionStatusBO;
import de.adorsys.ledgers.deposit.api.service.DepositAccountPaymentService;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTO;
import de.adorsys.ledgers.middleware.api.exception.MiddlewareErrorCode;
import de.adorsys.ledgers.middleware.api.exception.MiddlewareModuleException;
import de.adorsys.ledgers.middleware.api.service.MiddlewareFapiPaymentService;
import de.adorsys.ledgers.middleware.impl.converter.PaymentConverter;
import de.adorsys.ledgers.middleware.impl.policies.PaymentCancelPolicy;
import de.adorsys.ledgers.middleware.impl.service.PaymentValidationService;
import de.adorsys.ledgers.middleware.impl.service.SCAUtils;
import de.adorsys.ledgers.um.api.domain.UserBO;
import de.adorsys.ledgers.util.Ids;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

import static de.adorsys.ledgers.deposit.api.domain.TransactionStatusBO.ACCP;
import static de.adorsys.ledgers.deposit.api.domain.TransactionStatusBO.ACTC;

@Slf4j
@Service
@RequiredArgsConstructor
public class MiddlewareFapiPaymentServiceImpl implements MiddlewareFapiPaymentService {
    private final DepositAccountPaymentService paymentService;
    private final PaymentValidationService validationService;
    private final PaymentConverter paymentConverter;
    private final SCAUtils scaUtils;

    @Override
    public void initiatePayment(PaymentTO payment, String login) {
        PaymentBO paymentBO = paymentConverter.toPaymentBO(payment);
        validationService.validatePayment(paymentBO);
        paymentBO.updateDebtorAccountCurrency(validationService.getCheckedAccount(paymentBO).getCurrency());
        UserBO user = scaUtils.userBO(login);
        checkAccess(paymentBO.getAccountId(), user);
        TransactionStatusBO status = user.hasSCA()
                                             ? ACCP
                                             : ACTC;
        if (paymentBO.getPaymentId() == null) {
            paymentBO.setPaymentId(Ids.id());
        }
        paymentService.initiatePayment(paymentBO, status);
    }

    @Override
    public void initiateCancellation(String paymentId, String login) {
        UserBO userBO = scaUtils.userBO(login);
        PaymentBO payment = paymentService.getPaymentById(paymentId);
        checkAccess(payment.getAccountId(), userBO);
        payment.setRequestedExecutionTime(LocalTime.now().plusMinutes(10));
        PaymentCancelPolicy.onCancel(paymentId, payment.getTransactionStatus());
    }

    private void checkAccess(String accountId, UserBO user) {
        if (!user.hasAccessToAccountWithId(accountId)) {
            throw MiddlewareModuleException.builder()
                          .errorCode(MiddlewareErrorCode.INSUFFICIENT_PERMISSION)
                          .devMsg("You do not have access to respective account")
                          .build();
        }
    }
}
