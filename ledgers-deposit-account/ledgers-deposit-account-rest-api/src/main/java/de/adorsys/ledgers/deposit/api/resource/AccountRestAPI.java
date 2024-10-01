/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.api.resource;

import de.adorsys.ledgers.deposit.api.domain.account.AccountBalanceTO;
import de.adorsys.ledgers.deposit.api.domain.account.AccountIdentifierTypeTO;
import de.adorsys.ledgers.deposit.api.domain.account.AdditionalAccountInformationTO;
import de.adorsys.ledgers.deposit.api.domain.account.FundsConfirmationRequestTO;
import de.adorsys.ledgers.deposit.api.domain.account.TransactionTO;
import de.adorsys.ledgers.util.domain.CustomPageImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import static de.adorsys.ledgers.deposit.api.utils.Constants.*;

@Tag(name = "LDG003 - Accounts", description = "Provides access to a deposit account. This interface does not provide any endpoint to list all accounts.")
public interface AccountRestAPI {
    String BASE_PATH = "/accounts";

    @GetMapping("/{accountId}/balances")
    @Operation(summary = "Read balances",
            description = "Returns balances of the deposit account with the given accountId. "
                    + "User must have access to the target account. This is also accessible to other token types like tpp token (DELEGATED_ACESS)")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountBalanceTO.class)), description = "List of accounts balances for the given account.")
    })
    ResponseEntity<List<AccountBalanceTO>> getBalances(@Parameter(name = ACCOUNT_ID) @PathVariable(ACCOUNT_ID) String accountId);

    @GetMapping(path = "/{accountId}/transactions", params = {DATE_FROM_QUERY_PARAM, DATE_TO_QUERY_PARAM})
    @Operation(summary = "Find Transactions By Date", description = "Returns all transactions for the given account id")

    ResponseEntity<List<TransactionTO>> getTransactionByDates(
            @Parameter(name = ACCOUNT_ID)
            @PathVariable(ACCOUNT_ID) String accountId,
            @RequestParam(name = DATE_FROM_QUERY_PARAM, required = false) @DateTimeFormat(pattern = LOCAL_DATE_YYYY_MM_DD_FORMAT) LocalDate dateFrom,
            @RequestParam(name = DATE_TO_QUERY_PARAM) @DateTimeFormat(pattern = LOCAL_DATE_YYYY_MM_DD_FORMAT) LocalDate dateTo);

    @GetMapping(path = "/{accountId}/transactions/page", params = {DATE_FROM_QUERY_PARAM, DATE_TO_QUERY_PARAM, PAGE, SIZE})
    @Operation(summary = "Find Transactions By Date", description = "Returns transactions for the given account id for certain dates, paged view")

    ResponseEntity<CustomPageImpl<TransactionTO>> getTransactionByDatesPaged(
            @Parameter(name = ACCOUNT_ID)
            @PathVariable(name = ACCOUNT_ID) String accountId,
            @RequestParam(name = DATE_FROM_QUERY_PARAM, required = false) @DateTimeFormat(pattern = LOCAL_DATE_YYYY_MM_DD_FORMAT) LocalDate dateFrom,
            @RequestParam(name = DATE_TO_QUERY_PARAM) @DateTimeFormat(pattern = LOCAL_DATE_YYYY_MM_DD_FORMAT) LocalDate dateTo,
            @RequestParam(PAGE) int page,
            @RequestParam(SIZE) int size);

    @GetMapping("/{accountId}/transactions/{transactionId}")
    @Operation(summary = "Load Transaction", description = "Returns the transaction with the given account id and transaction id.")

    ResponseEntity<TransactionTO> getTransactionById(
            @Parameter(name = ACCOUNT_ID)
            @PathVariable(name = ACCOUNT_ID) String accountId,
            @Parameter(name = TRANSACTION_ID)
            @PathVariable(name = TRANSACTION_ID) String transactionId);

    @Operation(summary = "Fund Confirmation", description = "Returns account details information given the account IBAN")

    @PostMapping(value = "/funds-confirmation")
    ResponseEntity<Boolean> fundsConfirmation(
            @RequestBody FundsConfirmationRequestTO request);

    @GetMapping(path = "/info/{accountIdentifierType}/{accountIdentifier}")
    @Operation(summary = "Load Account Owner Additional information", description = "Returns Additional Account Information by Account Identifier")

    ResponseEntity<List<AdditionalAccountInformationTO>> getAdditionalAccountInfo(
            @Parameter(description = "Account identifier type i.e. ACCOUNT_ID / IBAN")
            @PathVariable(name = ACCOUNT_IDENTIFIER_TYPE) AccountIdentifierTypeTO accountIdentifierType,
            @Parameter(description = "The IBAN of the requested account: e.g.: DE69760700240340283600", example = "DE69760700240340283600")
            @PathVariable(name = ACCOUNT_IDENTIFIER) String accountIdentifier);
}