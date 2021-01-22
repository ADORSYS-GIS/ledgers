/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.ledgers.middleware.impl.service;

import de.adorsys.ledgers.deposit.api.domain.PaymentBO;
import de.adorsys.ledgers.deposit.api.domain.PaymentTypeBO;
import de.adorsys.ledgers.deposit.api.domain.TransactionStatusBO;
import de.adorsys.ledgers.deposit.api.service.DepositAccountPaymentService;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentCoreDataTO;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTO;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTypeTO;
import de.adorsys.ledgers.middleware.api.domain.payment.TransactionStatusTO;
import de.adorsys.ledgers.middleware.api.domain.sca.SCAPaymentResponseTO;
import de.adorsys.ledgers.middleware.api.domain.sca.ScaInfoTO;
import de.adorsys.ledgers.middleware.api.domain.sca.ScaStatusTO;
import de.adorsys.ledgers.middleware.api.domain.um.BearerTokenTO;
import de.adorsys.ledgers.middleware.api.service.MiddlewarePaymentService;
import de.adorsys.ledgers.middleware.impl.converter.PageMapper;
import de.adorsys.ledgers.middleware.impl.converter.PaymentConverter;
import de.adorsys.ledgers.middleware.impl.converter.ScaResponseResolver;
import de.adorsys.ledgers.middleware.impl.policies.PaymentCancelPolicy;
import de.adorsys.ledgers.middleware.impl.policies.PaymentCoreDataPolicy;
import de.adorsys.ledgers.sca.domain.OpTypeBO;
import de.adorsys.ledgers.sca.service.SCAOperationService;
import de.adorsys.ledgers.um.api.domain.UserBO;
import de.adorsys.ledgers.util.Ids;
import de.adorsys.ledgers.util.domain.CustomPageImpl;
import de.adorsys.ledgers.util.domain.CustomPageableImpl;
import de.adorsys.ledgers.util.exception.ScaModuleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static de.adorsys.ledgers.deposit.api.domain.TransactionStatusBO.*;
import static de.adorsys.ledgers.middleware.api.domain.Constants.SCOPE_FULL_ACCESS;
import static de.adorsys.ledgers.sca.domain.OpTypeBO.CANCEL_PAYMENT;
import static de.adorsys.ledgers.sca.domain.OpTypeBO.PAYMENT;

@Slf4j
@Service
@Transactional
@SuppressWarnings({"PMD.TooManyStaticImports", "PMD.TooManyMethods"})
@RequiredArgsConstructor
public class MiddlewarePaymentServiceImpl implements MiddlewarePaymentService {
    private final DepositAccountPaymentService paymentService;
    private final SCAOperationService scaOperationService;
    private final PaymentConverter paymentConverter;
    private final SCAUtils scaUtils;
    private final PaymentCoreDataPolicy coreDataPolicy;
    private final AccessService accessService;
    private final ScaResponseResolver scaResponseResolver;
    private final PageMapper pageMapper;
    private final PaymentValidationService validationService;

    @Value("${ledgers.sca.multilevel.enabled:false}")
    private boolean multilevelScaEnable;

    @Override
    public TransactionStatusTO getPaymentStatusById(String paymentId) {
        TransactionStatusBO paymentStatus = paymentService.getPaymentStatusById(paymentId);
        return TransactionStatusTO.valueOf(paymentStatus.name());
    }

    @Override
    public SCAPaymentResponseTO initiatePaymentCancellation(ScaInfoTO scaInfoTO, String paymentId) {
        UserBO userBO = scaUtils.userBO(scaInfoTO.getUserLogin());
        PaymentBO payment = paymentService.getPaymentById(paymentId);
        payment.setRequestedExecutionTime(LocalTime.now().plusMinutes(10));
        PaymentCancelPolicy.onCancel(paymentId, payment.getTransactionStatus());
        return prepareScaAndResolveResponse(scaInfoTO, payment, userBO, CANCEL_PAYMENT);
    }

    @Override
    public SCAPaymentResponseTO initiatePayment(ScaInfoTO scaInfoTO, PaymentTO payment, PaymentTypeTO paymentType) {
        return checkPaymentAndPrepareResponse(scaInfoTO, paymentConverter.toPaymentBO(payment, paymentType));
    }

    private SCAPaymentResponseTO checkPaymentAndPrepareResponse(ScaInfoTO scaInfoTO, PaymentBO paymentBO) {
        validationService.validatePayment(paymentBO);
        paymentBO.updateDebtorAccountCurrency(validationService.getCheckedAccount(paymentBO).getCurrency());
        UserBO user = scaUtils.userBO(scaInfoTO.getUserLogin());
        TransactionStatusBO status = user.hasSCA()
                                             ? ACCP
                                             : ACTC;
        return prepareScaAndResolveResponse(scaInfoTO, persist(paymentBO, status), user, PAYMENT);
    }

    @Override
    public SCAPaymentResponseTO executePayment(ScaInfoTO scaInfoTO, String paymentId) {
        PaymentBO payment = paymentService.getPaymentById(paymentId);
        TransactionStatusBO status = paymentService.updatePaymentStatus(paymentId, scaInfoTO.hasScope(SCOPE_FULL_ACCESS) ? ACTC : PATC);
        if (status == ACTC) {
            status = paymentService.executePayment(payment.getPaymentId(), scaInfoTO.getUserLogin());
        }
        return new SCAPaymentResponseTO(payment.getPaymentId(), status.name(), payment.getPaymentType().name(), payment.getPaymentProduct());
    }

    private SCAPaymentResponseTO prepareScaAndResolveResponse(ScaInfoTO scaInfoTO, PaymentBO payment, UserBO user, OpTypeBO opType) {
        boolean isScaRequired = user.hasSCA();
        String psuMessage = coreDataPolicy.getPaymentCoreData(opType, payment).resolveMessage(isScaRequired);
        BearerTokenTO token = accessService.exchangeTokenStartSca(isScaRequired, scaInfoTO.getAccessToken());
        ScaStatusTO scaStatus = isScaRequired
                                        ? ScaStatusTO.PSUAUTHENTICATED
                                        : ScaStatusTO.EXEMPTED;
        int scaWeight = user.resolveWeightForAccount(payment.getAccountId());
        SCAPaymentResponseTO response = new SCAPaymentResponseTO();
        scaResponseResolver.updateScaResponseFields(user, response, null, psuMessage, token, scaStatus, scaWeight);
        return scaResponseResolver.updatePaymentRelatedResponseFields(response, payment);
    }

    private PaymentBO persist(PaymentBO paymentBO, TransactionStatusBO status) {
        if (paymentBO.getPaymentId() == null) {
            paymentBO.setPaymentId(Ids.id());
        }
        return paymentService.initiatePayment(paymentBO, status);
    }

    @Override
    public PaymentTO getPaymentById(String paymentId) {
        PaymentBO paymentResult = paymentService.getPaymentById(paymentId);
        return paymentConverter.toPaymentTO(paymentResult);
    }

    @Override
    public String iban(String paymentId) { //TODO Consider removing!
        return paymentService.readIbanByPaymentId(paymentId);
    }

    /*
     * Authorizes a payment. Schedule the payment for execution if no further authorization is required.
     *
     */

    @Override
    @Transactional(noRollbackFor = ScaModuleException.class)
    public SCAPaymentResponseTO authorizePayment(ScaInfoTO scaInfoTO, String paymentId) {
        return authorizeOperation(scaInfoTO, paymentId, PAYMENT);
    }

    @Override
    @Transactional(noRollbackFor = ScaModuleException.class)
    public SCAPaymentResponseTO authorizeCancelPayment(ScaInfoTO scaInfoTO, String paymentId) {
        return authorizeOperation(scaInfoTO, paymentId, CANCEL_PAYMENT);
    }

    @Override
    public List<PaymentTO> getPendingPeriodicPayments(ScaInfoTO scaInfoTO) {
        Set<String> accountIds = scaUtils.userBO(scaInfoTO.getUserLogin()).getAccountIds();
        List<PaymentBO> payments = paymentService.getPaymentsByTypeStatusAndDebtor(PaymentTypeBO.PERIODIC, ACSP, accountIds);
        return paymentConverter.toPaymentTOList(payments);
    }

    @Override
    public CustomPageImpl<PaymentTO> getPendingPeriodicPaymentsPaged(ScaInfoTO scaInfo, CustomPageableImpl pageable) {
        Set<String> accountIds = scaUtils.userBO(scaInfo.getUserLogin()).getAccountIds();
        return pageMapper.toCustomPageImpl(
                paymentService.getPaymentsByTypeStatusAndDebtorPaged(PaymentTypeBO.PERIODIC, ACSP, accountIds, PageRequest.of(pageable.getPage(), pageable.getSize()))
                        .map(paymentConverter::toPaymentTO));
    }

    private SCAPaymentResponseTO authorizeOperation(ScaInfoTO scaInfoTO, String paymentId, OpTypeBO opType) {
        PaymentBO payment = paymentService.getPaymentById(paymentId);
        PaymentCoreDataTO paymentKeyData = coreDataPolicy.getPaymentCoreData(opType, payment);
        if (scaOperationService.authenticationCompleted(paymentId, opType)) {
            performExecuteOrCancelOperation(scaInfoTO, paymentId, opType, payment);
        } else if (multilevelScaEnable) {
            payment.setTransactionStatus(paymentService.updatePaymentStatus(paymentId, PATC));
        }
        UserBO user = scaUtils.userBO(scaInfoTO.getUserLogin());
        SCAPaymentResponseTO response = new SCAPaymentResponseTO();
        int scaWeight = user.resolveWeightForAccount(payment.getAccountId());
        scaResponseResolver.updateScaResponseFields(user, response, null, paymentKeyData.getTanTemplate(), null, ScaStatusTO.FINALISED, scaWeight);
        return scaResponseResolver.updatePaymentRelatedResponseFields(response, payment);
    }

    private void performExecuteOrCancelOperation(ScaInfoTO scaInfoTO, String paymentId, OpTypeBO opType, PaymentBO payment) {
        if (opType == PAYMENT) {
            paymentService.updatePaymentStatus(paymentId, ACTC);
            payment.setTransactionStatus(paymentService.executePayment(paymentId, scaInfoTO.getUserLogin()));
        } else {
            payment.setTransactionStatus(paymentService.cancelPayment(paymentId));
        }
    }
}
