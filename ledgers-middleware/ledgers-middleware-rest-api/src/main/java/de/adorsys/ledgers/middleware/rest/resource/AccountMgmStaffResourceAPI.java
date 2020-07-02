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

package de.adorsys.ledgers.middleware.rest.resource;

import de.adorsys.ledgers.middleware.api.domain.account.AccountDetailsTO;
import de.adorsys.ledgers.middleware.api.domain.account.AccountReportTO;
import de.adorsys.ledgers.middleware.api.domain.payment.AmountTO;
import de.adorsys.ledgers.util.domain.CustomPageImpl;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "LDG011 - Accounts (STAFF access)", description = "Provides access to the deposit account resource for staff members.")
public interface AccountMgmStaffResourceAPI {
    String BASE_PATH = "/staff-access" + AccountRestAPI.BASE_PATH;
    String ACCOUNT_ID = "accountId";
    String USER_ID = "userId";
    String QUERY_PARAM = "queryParam";
    String PAGE = "page";
    String SIZE = "size";

    @ApiOperation(value = "Retrieves account by iban and Currency",
            authorizations = @Authorization(value = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account creation successful"),
            @ApiResponse(code = 404, message = "User with this ID not found"),
            @ApiResponse(code = 409, message = "Account with given IBAN already exists.")
    })
    @GetMapping("/acc/acc")
    ResponseEntity<List<AccountDetailsTO>> getAccountsByIbanAndCurrency(@RequestParam(name = "iban") String iban, @RequestParam(name = "currency", required = false, defaultValue = "") String currency);

    /**
     * Creates a new deposit account for a user specified by ID
     * Account is created for the same branch as Staff user
     *
     * @param userId           user for who account is created
     * @param accountDetailsTO account details
     * @return Void
     */
    @ApiOperation(value = "Registers a new Deposit Account for a user with specified ID",
            notes = "Registers a new deposit account and assigns account access OWNER to the current user.",
            authorizations = @Authorization(value = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account creation successful"),
            @ApiResponse(code = 404, message = "User with this ID not found"),
            @ApiResponse(code = 409, message = "Account with given IBAN already exists.")
    })
    @PostMapping
    ResponseEntity<Void> createDepositAccountForUser(@RequestParam(name = USER_ID) String userId, @RequestBody AccountDetailsTO accountDetailsTO);

    /**
     * Returns the list of accounts that belong to the same branch as STAFF user.
     *
     * @return list of accounts that belongs to the same branch as staff user.
     */
    @ApiOperation(value = "List fo Accessible Accounts", authorizations = @Authorization(value = "apiKey"),
            notes = "Returns the list of all accounts linked to the connected user. "
                            + "Call only available to role CUSTOMER.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = AccountDetailsTO[].class, message = "List of accounts accessible to the user.")
    })
    @GetMapping
    ResponseEntity<List<AccountDetailsTO>> getListOfAccounts();

    @ApiOperation(value = "List fo Accessible Accounts", authorizations = @Authorization(value = "apiKey"),
            notes = "Returns the list of all accounts linked to the connected user, paged view. "
                            + "Call only available to role CUSTOMER.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = AccountDetailsTO[].class, message = "List of accounts accessible to the user.")
    })
    @GetMapping(path = "/page")
    ResponseEntity<CustomPageImpl<AccountDetailsTO>> getListOfAccountsPaged(
            @RequestParam(value = QUERY_PARAM, defaultValue = "", required = false) String queryParam,
            @RequestParam(PAGE) int page, @RequestParam(SIZE) int size);

    @ApiOperation(value = "Load Account by AccountId",
            notes = "Returns account details information for the given account id. "
                            + "User must have access to the target account. This is also accessible to other token types like tpp token (DELEGATED_ACESS)",
            authorizations = @Authorization(value = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = AccountDetailsTO.class, message = "Account details.")
    })
    @GetMapping("/{accountId}")
    ResponseEntity<AccountDetailsTO> getAccountDetailsById(@ApiParam(ACCOUNT_ID) @PathVariable(ACCOUNT_ID) String accountId);

    /**
     * Operation deposits cash to the deposit account
     *
     * @param accountId Account ID in Ledgers
     * @param amount    Amount to be deposited
     * @return Void
     */
    @ApiOperation(value = "Deposit Cash", authorizations = @Authorization(value = "apiKey"),
            notes = "Operation for staff member to register cash in the deposit account")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Operation was successful")
    })
    @PostMapping("/{accountId}/cash")
    ResponseEntity<Void> depositCash(@PathVariable(ACCOUNT_ID) String accountId, @RequestBody AmountTO amount);

    @ApiOperation(value = "Load Extended Account Details by AccountId",
            notes = "Returns extended account details information for the given account id. "
                            + "User must have access to the target account. This is also accessible to other token types like tpp token (DELEGATED_ACESS)",
            authorizations = @Authorization(value = "apiKey"))
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = AccountReportTO.class, message = "Extended Account details.")
    })
    @GetMapping("/{accountId}/extended")
    ResponseEntity<AccountReportTO> getExtendedAccountDetailsById(@ApiParam(ACCOUNT_ID) @PathVariable(ACCOUNT_ID) String accountId);

    @ApiOperation(value = "Block/Unblock account",
            notes = "Changes block state for given account, returns status being set to the block",
            authorizations = @Authorization(value = "apiKey"))
    @PostMapping("/{accountId}/status")
    ResponseEntity<Boolean> changeStatus(@PathVariable(ACCOUNT_ID) String accountId);

}
