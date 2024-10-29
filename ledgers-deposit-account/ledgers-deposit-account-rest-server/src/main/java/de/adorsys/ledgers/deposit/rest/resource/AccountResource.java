/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.rest.resource;

import de.adorsys.ledgers.deposit.api.domain.account.*;
import de.adorsys.ledgers.deposit.api.resource.*;
import de.adorsys.ledgers.deposit.api.service.*;
import de.adorsys.ledgers.deposit.rest.annotation.*;
import de.adorsys.ledgers.util.domain.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

import static de.adorsys.ledgers.middleware.api.exception.MiddlewareErrorCode.REQUEST_VALIDATION_FAILURE;

@Slf4j
@RestController
@DepositUserResource
@RequiredArgsConstructor
@RequestMapping(AccountRestAPI.BASE_PATH)
public class AccountResource implements AccountRestAPI {

    private final DepositAccountService depositAccountService;
    //private final MiddlewareUserManagementService userManagementService;

    @Override
    public ResponseEntity<AccountDetailsTO> getAccountDetailsById(String accountId) {
        return null;
    }

    @Override
    @PreAuthorize("hasAccessToAccount(#accountId)")
    public ResponseEntity<List<AccountBalanceTO>> getBalances(String accountId) {
//        AccountDetailsTO accountDetails = depositAccountService.getAccountDetailsById(accountId, LocalDateTime.now(), true);
//        return ResponseEntity.ok(accountDetails.getBalances());
        return null;
    }

    @Override
    @PreAuthorize("hasAccessToAccount(#accountId)")
    public ResponseEntity<List<TransactionTO>> getTransactionByDates(String accountId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        dateChecker(dateFrom, dateTo);
//        List<TransactionTO> transactions = depositAccountService.getTransactionsByDates(accountId, validDate(dateFrom), validDate(dateTo));
//        return ResponseEntity.ok(transactions);
        return null;
    }

    @Override
    @PreAuthorize("hasAccessToAccount(#accountId)")
    public ResponseEntity<CustomPageImpl<TransactionTO>> getTransactionByDatesPaged(String accountId, LocalDateTime dateFrom, LocalDateTime dateTo, int page, int size) {
        dateChecker(dateFrom, dateTo);
//        CustomPageableImpl pageable = new CustomPageableImpl(page, size);
//        CustomPageImpl<TransactionTO> customPage = depositAccountService.getTransactionsByDatesPaged(accountId, dateFrom, dateTo, pageable);
//        return ResponseEntity.ok(customPage);
        return null;
    }

    @Override
    @PreAuthorize("hasAccessToAccount(#accountId)")
    public ResponseEntity<TransactionTO> getTransactionById(String accountId, String transactionId) {
//        return ResponseEntity.ok(depositAccountService.getTransactionById(accountId, transactionId));
        return null;
    }

    private void dateChecker(LocalDateTime dateFrom, LocalDateTime dateTo) {
        if (!validDate(dateFrom).isEqual(validDate(dateTo))
                && validDate(dateFrom).isAfter(validDate(dateTo))) {
            throw de.adorsys.ledgers.middleware.api.exception.MiddlewareModuleException.builder()
                    .errorCode(REQUEST_VALIDATION_FAILURE)
                    .devMsg("Illegal request dates sequence, possibly swapped 'date from' with 'date to'")
                    .build();
        }
    }

    private LocalDateTime validDate(LocalDateTime date) {
        return Optional.ofNullable(date)
                .orElseGet(LocalDateTime::now);
    }
}