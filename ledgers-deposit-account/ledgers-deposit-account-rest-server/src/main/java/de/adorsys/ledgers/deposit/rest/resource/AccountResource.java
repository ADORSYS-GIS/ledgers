/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.rest.resource;

import de.adorsys.ledgers.deposit.api.domain.account.*;

import de.adorsys.ledgers.deposit.api.service.DepositAccountManagementService;
import de.adorsys.ledgers.deposit.rest.annotation.DepositUserResource;
import de.adorsys.ledgers.deposit.api.resource.AccountRestAPI;
import de.adorsys.ledgers.middleware.api.exception.MiddlewareModuleException;
import de.adorsys.ledgers.util.domain.CustomPageImpl;
import de.adorsys.ledgers.util.domain.CustomPageableImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static de.adorsys.ledgers.middleware.api.exception.MiddlewareErrorCode.REQUEST_VALIDATION_FAILURE;

@Slf4j
@RestController
@DepositUserResource
@RequiredArgsConstructor
@RequestMapping(AccountRestAPI.BASE_PATH)
public class AccountResource implements AccountRestAPI {

    private final DepositAccountManagementService depositAccountService;
    //private final MiddlewareUserManagementService userManagementService;

    @Override
    @PreAuthorize("hasAccessToAccount(#accountId)")
    public ResponseEntity<List<AccountBalanceTO>> getBalances(String accountId) {
        AccountDetailsTO accountDetails = depositAccountService.getDepositAccountById(accountId, LocalDateTime.now(), true);
        return ResponseEntity.ok(accountDetails.getBalances());
    }

    @Override
    @PreAuthorize("hasAccessToAccount(#accountId)")
    public ResponseEntity<List<TransactionTO>> getTransactionByDates(String accountId, LocalDate dateFrom, LocalDate dateTo) {
        dateChecker(dateFrom, dateTo);
        List<TransactionTO> transactions = depositAccountService.getTransactionsByDates(accountId, validDate(dateFrom), validDate(dateTo));
        return ResponseEntity.ok(transactions);
    }

    @Override
    @PreAuthorize("hasAccessToAccount(#accountId)")
    public ResponseEntity<CustomPageImpl<TransactionTO>> getTransactionByDatesPaged(String accountId, LocalDate dateFrom, LocalDate dateTo, int page, int size) {
        dateChecker(dateFrom, dateTo);
        CustomPageableImpl pageable = new CustomPageableImpl(page, size);
        CustomPageImpl<TransactionTO> customPage = depositAccountService.getTransactionsByDatesPaged(accountId, dateFrom, dateTo, pageable);
        return ResponseEntity.ok(customPage);
    }

    @Override
    @PreAuthorize("hasAccessToAccount(#accountId)")
    public ResponseEntity<TransactionTO> getTransactionById(String accountId, String transactionId) {
        return ResponseEntity.ok(depositAccountService.getTransactionById(accountId, transactionId));
    }

    @Override
    @PreAuthorize("hasAccessToAccountWithIban(#request.psuAccount.iban)")
    public ResponseEntity<Boolean> fundsConfirmation(FundsConfirmationRequestTO request) {
        if (request.getInstructedAmount().getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw MiddlewareModuleException.builder()
                    .errorCode(REQUEST_VALIDATION_FAILURE)
                    .devMsg("Requested amount less or equal zero")
                    .build();
        }
        boolean fundsAvailable = depositAccountService.confirmFundsAvailability(request);
        return ResponseEntity.ok(fundsAvailable);
    }


    private void dateChecker(LocalDate dateFrom, LocalDate dateTo) {
        if (!validDate(dateFrom).isEqual(validDate(dateTo))
                && validDate(dateFrom).isAfter(validDate(dateTo))) {
            throw MiddlewareModuleException.builder()
                    .errorCode(REQUEST_VALIDATION_FAILURE)
                    .devMsg("Illegal request dates sequence, possibly swapped 'date from' with 'date to'")
                    .build();
        }
    }

    private LocalDate validDate(LocalDate date) {
        return Optional.ofNullable(date)
                .orElseGet(LocalDate::now);
    }
}
