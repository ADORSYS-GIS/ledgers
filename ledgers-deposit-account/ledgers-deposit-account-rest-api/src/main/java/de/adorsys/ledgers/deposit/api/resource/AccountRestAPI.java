/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.api.resource;

import de.adorsys.ledgers.deposit.api.domain.account.*;
import de.adorsys.ledgers.util.domain.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.format.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

import static de.adorsys.ledgers.deposit.api.utils.Constants.*;


@Tag(name = "LDG??? - Accounts", description = "Provides access to a deposit account.")
public interface AccountRestAPI {
    String BASE_PATH = "/accounts";

    @GetMapping("/{accountId}")
    @Operation(summary = "Load Account by AccountId",
            description = "Returns account details information for the given account id. ")
  
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDetailsTO.class)),
                    description = "Account details.")
    })
    ResponseEntity<AccountDetailsTO> getAccountDetailsById(@Parameter(name = ACCOUNT_ID) @PathVariable(ACCOUNT_ID) String accountId);

    @GetMapping("/{accountId}/balances")
    @Operation(summary = "Read balances",
            description = "Returns balances of the deposit account with the given accountId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountBalanceTO.class)), description = "List of accounts balances for the given account.")
    })
    ResponseEntity<List<AccountBalanceTO>> getBalances(@Parameter(name = ACCOUNT_ID) @PathVariable(ACCOUNT_ID) String accountId);

    @GetMapping(path = "/{accountId}/transactions", params = {DATE_FROM_QUERY_PARAM, DATE_TO_QUERY_PARAM})
    @Operation(summary = "Find Transactions By Date", description = "Returns all transactions for the given account id")
    ResponseEntity<List<TransactionTO>> getTransactionByDates(
            @Parameter(name = ACCOUNT_ID)
            @PathVariable(ACCOUNT_ID) String accountId,
            @RequestParam(name = DATE_FROM_QUERY_PARAM, required = false) @DateTimeFormat(pattern = LOCAL_DATE_YYYY_MM_DD_FORMAT) LocalDateTime dateFrom,
            @RequestParam(name = DATE_TO_QUERY_PARAM) @DateTimeFormat(pattern = LOCAL_DATE_YYYY_MM_DD_FORMAT) LocalDateTime dateTo);

    @GetMapping(path = "/{accountId}/transactions/page", params = {DATE_FROM_QUERY_PARAM, DATE_TO_QUERY_PARAM, PAGE, SIZE})
    @Operation(summary = "Find Transactions By Date", description = "Returns transactions for the given account id for certain dates, paged view")
    ResponseEntity<CustomPageImpl<TransactionTO>> getTransactionByDatesPaged(
            @Parameter(name = ACCOUNT_ID)
            @PathVariable(name = ACCOUNT_ID) String accountId,
            @RequestParam(name = DATE_FROM_QUERY_PARAM, required = false) @DateTimeFormat(pattern = LOCAL_DATE_YYYY_MM_DD_FORMAT) LocalDateTime dateFrom,
            @RequestParam(name = DATE_TO_QUERY_PARAM) @DateTimeFormat(pattern = LOCAL_DATE_YYYY_MM_DD_FORMAT) LocalDateTime dateTo,
            @RequestParam(PAGE) int page,
            @RequestParam(SIZE) int size);

    @GetMapping("/{accountId}/transactions/{transactionId}")
    @Operation(summary = "Load Transaction", description = "Returns the transaction with the given account id and transaction id.")
    ResponseEntity<TransactionTO> getTransactionById(
            @Parameter(name = ACCOUNT_ID)
            @PathVariable(name = ACCOUNT_ID) String accountId,
            @Parameter(name = TRANSACTION_ID)
            @PathVariable(name = TRANSACTION_ID) String transactionId);
        
}
