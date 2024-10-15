/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.api.service.impl;

import de.adorsys.ledgers.deposit.api.domain.account.AccountDetailsTO;
import de.adorsys.ledgers.deposit.api.domain.account.FundsConfirmationRequestTO;
import de.adorsys.ledgers.deposit.api.domain.account.TransactionTO;
import de.adorsys.ledgers.deposit.api.domain.payment.AmountTO;
import de.adorsys.ledgers.deposit.api.service.DepositAccountManagementService;
import de.adorsys.ledgers.middleware.api.domain.account.AccountDetailsExtendedTO;
import de.adorsys.ledgers.middleware.api.domain.account.AccountReportTO;
import de.adorsys.ledgers.middleware.api.domain.sca.SCAConsentResponseTO;
import de.adorsys.ledgers.middleware.api.domain.sca.ScaInfoTO;
import de.adorsys.ledgers.middleware.api.domain.um.AisConsentTO;
import de.adorsys.ledgers.util.domain.CustomPageImpl;
import de.adorsys.ledgers.util.domain.CustomPageableImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class DepositAccountManagementServiceImpl implements DepositAccountManagementService {
    @Override
    public List<AccountDetailsTO> getAccountsByIbanAndCurrency(String iban, String currency) {
        return List.of();
    }

    @Override
    public boolean createDepositAccount(String userId, AccountDetailsTO depositAccount) {
        return false;
    }

    @Override
    public List<AccountDetailsTO> listDepositAccounts(String userId) {
        return List.of();
    }

    @Override
    public List<AccountDetailsTO> listDepositAccountsByBranch(String userId) {
        return List.of();
    }

    @Override
    public CustomPageImpl<AccountDetailsTO> listDepositAccountsByBranchPaged(String userId, String queryParam, boolean withBalance, CustomPageableImpl pageable) {
        return null;
    }

    @Override
    public CustomPageImpl<AccountDetailsExtendedTO> getAccountsByBranchAndMultipleParams(String countryCode, String branchId, String branchLogin, String iban, Boolean blocked, CustomPageableImpl pageable) {
        return null;
    }

    @Override
    public AccountDetailsTO getDepositAccountById(String id, LocalDateTime time, boolean withBalance) {
        return null;
    }

    @Override
    public TransactionTO getTransactionById(String accountId, String transactionId) {
        return null;
    }

    @Override
    public List<TransactionTO> getTransactionsByDates(String accountId, LocalDate dateFrom, LocalDate dateTo) {
        return List.of();
    }

    @Override
    public CustomPageImpl<TransactionTO> getTransactionsByDatesPaged(String accountId, LocalDate dateFrom, LocalDate dateTo, CustomPageableImpl pageable) {
        return null;
    }

    @Override
    public boolean confirmFundsAvailability(FundsConfirmationRequestTO request) {
        return false;
    }

    @Override
    public String iban(String id) {
        return "";
    }

    @Override
    public SCAConsentResponseTO startAisConsent(ScaInfoTO scaInfoTO, String consentId, AisConsentTO aisConsent) {
        return null;
    }

    @Override
    public SCAConsentResponseTO startPiisConsent(ScaInfoTO scaInfoTO, AisConsentTO aisConsent) {
        return null;
    }

    @Override
    public Set<String> getAccountsFromConsent(String consentId) {
        return Set.of();
    }

    @Override
    public void depositCash(String accountId, AmountTO amount) {

    }

    @Override
    public AccountReportTO getAccountReport(String accountId) {
        return null;
    }

    @Override
    public boolean changeStatus(String accountId, boolean systemBlock) {
        return false;
    }

    @Override
    public void changeCreditLimit(String accountId, BigDecimal creditLimit) {

    }
}
