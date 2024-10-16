/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.api.resource;

import de.adorsys.ledgers.baam.db.domain.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "Bank Account Access", description = "Provide endpoint for managing bank account access.")

public interface BankAccountAccessAPI {
    String BASE_PATH = "/bank-account-access";

    @PostMapping
    @Operation(summary = "Create Bank Account Access", description = "Creates a new bank account access entry")
    ResponseEntity<BankAccountAccess> createAccountAccess(@RequestBody BankAccountAccess bankAccountAccess);

    @GetMapping("/{Id}")
    @Operation(summary = "Get Bank Account Access", description = "Retrieves a bank account access entry by ID")
    ResponseEntity<BankAccountAccess> getAccessById(@PathVariable String Id);

    @GetMapping
    @Operation(summary = "Get All Bank Account Accesses details", description = "Retrieves all bank account access entries")
    ResponseEntity<List<BankAccountAccess>> getAllAccesses();

    @PutMapping("/{Id}")
    @Operation(summary = "Update Bank Account Access", description = "Updates an existing bank account access entry")
    ResponseEntity<BankAccountAccess> updateAccess(@PathVariable String Id, @RequestBody BankAccountAccess bankAccountAccess);

    @DeleteMapping("/{Id}")
    @Operation(summary = "Delete Bank Account Access", description = "Deletes a bank account access entry by ID")
    ResponseEntity<Void> deleteAccess(@PathVariable String Id);
}
