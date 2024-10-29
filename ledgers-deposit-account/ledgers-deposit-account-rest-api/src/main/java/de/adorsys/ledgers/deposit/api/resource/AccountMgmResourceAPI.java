/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.api.resource;

import de.adorsys.ledgers.deposit.api.domain.account.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static de.adorsys.ledgers.deposit.api.utils.Constants.*;


@Tag(name = "LDG??? - Accounts (Deposit Account)", description = "Provides access to the deposit account resource management interface")
public interface AccountMgmResourceAPI {

    String BASE_PATH = "/AccountManagement";

    /**
     * Creates a new deposit account for a user specified by ID
     *
     * @param accountDetailsTO account details
     * @return Void
     */
    @Operation(summary = "Registers a new Deposit Account for the public key authenticating this request",
            description = "Registers a new Deposit Account for the public key authenticating this request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account creation successful")
    })
    @PostMapping
    ResponseEntity<AccountDetailsTO> createDepositAccount(@RequestHeader(AUTHORIZATION) String authorizationHeader,
                                                     @RequestBody AccountDetailsTO accountDetailsTO);
}
