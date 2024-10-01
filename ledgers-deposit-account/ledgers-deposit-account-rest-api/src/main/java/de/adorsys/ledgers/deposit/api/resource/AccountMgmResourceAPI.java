/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.api.resource;

import de.adorsys.ledgers.deposit.api.domain.account.AccountDetailsTO;
import de.adorsys.ledgers.deposit.api.domain.payment.AmountTO;
import de.adorsys.ledgers.util.domain.CustomPageImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static de.adorsys.ledgers.deposit.api.utils.Constants.*;

@Tag(name = "LDG022 - Accounts (Deposit Account)", description = "Provides access to the deposit account resource for  members.")
public interface AccountMgmResourceAPI {

    String BASE_PATH = "/AccountManagement";



    /**
     * Creates a new deposit account for a user specified by ID
     * Account is created for the same branch as user
     *
     * @param userId           user for who account is created
     * @param accountDetailsTO account details
     * @return Void
     */
    @Operation(summary = "Registers a new Deposit Account for a user with specified ID",
            description = "Registers a new deposit account and assigns account access OWNER to the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account creation successful"),
            @ApiResponse(responseCode = "404", description = "User with this ID not found"),
            @ApiResponse(responseCode = "409", description = "Account with given IBAN already exists.")
    })
    @PostMapping
    ResponseEntity<Boolean> createDepositAccountForUser(@RequestParam(name = USER_ID) String userId,
                                                     @RequestBody AccountDetailsTO accountDetailsTO);

    /**
     * Returns the list of accounts that belong to the same branch as user.
     *
     * @return list of accounts that belongs to the same branch as  user.
     */
    @Operation(summary = "List fo Accessible Accounts",
            description = "Returns the list of all accounts linked to the connected user. "
                                  + "Call only available to role CUSTOMER.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDetailsTO[].class)), description = "List of accounts accessible to the user.")
    })
    @GetMapping
    ResponseEntity<List<AccountDetailsTO>> getListOfAccounts();

    @Operation(summary = "List fo Accessible Accounts",
            description = "Returns the list of all accounts linked to the connected user, paged view. "
                    + "Query param represents full or partial IBAN")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDetailsTO[].class)), description = "List of accounts accessible to the user.")
    })
    @GetMapping(path = "/page")
    ResponseEntity<CustomPageImpl<AccountDetailsTO>> getListOfAccountsPaged(
            @RequestParam(value = QUERY_PARAM, defaultValue = "", required = false) String queryParam,
            @RequestParam(PAGE) int page, @RequestParam(SIZE) int size, @RequestParam(WITH_BALANCE_QUERY_PARAM) boolean withBalance);

    @Operation(summary = "Load Account by AccountId",
            description = "Returns account details information for the given account id. "
                                  + "User must have access to the target account. This is also accessible to other token types like tpp token (DELEGATED_ACESS)")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDetailsTO.class)), description = "Account details.")
    })
    @GetMapping("/{accountId}")
    ResponseEntity<AccountDetailsTO> getAccountDetailsById(@Parameter(name = ACCOUNT_ID) @PathVariable(ACCOUNT_ID) String accountId);

    /**
     * Operation deposits cash to the deposit account
     *
     * @param accountId Account ID in Ledgers
     * @param amount    Amount to be deposited
     * @return Void
     */
    @Operation(summary = "Deposit Cash",
            description = "Operation for a member to register cash in the deposit account")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Operation was successful")
    })
    @PostMapping("/{accountId}/cash")
    ResponseEntity<Void> depositCash(@PathVariable(ACCOUNT_ID) String accountId, @RequestBody AmountTO amount);

    @Operation(summary = "Load Extended Account Details by AccountId",
            description = "Returns extended account details information for the given account id. "
                                  + "User must have access to the target account. This is also accessible to other token types like tpp token (DELEGATED_ACESS)")

    @PostMapping("/{accountId}/status")
    ResponseEntity<Boolean> changeStatus(@PathVariable(ACCOUNT_ID) String accountId);}
