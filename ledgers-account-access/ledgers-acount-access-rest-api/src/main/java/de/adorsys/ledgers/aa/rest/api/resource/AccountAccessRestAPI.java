/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.rest.api.resource;

import de.adorsys.ledgers.aa.db.domain.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@Tag(name = "Account Access", description = "Provide endpoint for managing  account access.")
@RequestMapping(AccountAccessRestAPI.BASE_PATH)
public interface AccountAccessRestAPI {

    String BASE_PATH = "/api/accesses";

    @Operation(summary = "Create an Account Access", description = "Create a new account access entity")
    @PostMapping
    ResponseEntity<AccountAccessEntity> createAccess(@RequestBody AccountAccessEntity access);

    @Operation(summary = "Get Account Access by ID", description = "Retrieve an account access entity by its unique ID")
    @GetMapping("/{id}")
    ResponseEntity<AccountAccessEntity> getAccessById(@PathVariable UUID id);

    @Operation(summary = "Get All Account Accesses", description = "Retrieve a list of all account access entities")
    @GetMapping
    ResponseEntity<List<AccountAccessEntity>> getAllAccesses();

    @Operation(summary = "Update an Account Access", description = "Update an existing account access entity by its ID")
    @PutMapping("/{id}")
    ResponseEntity<AccountAccessEntity> updateAccess(@PathVariable UUID id, @RequestBody AccountAccessEntity access);

    @Operation(summary = "Delete an Account Access", description = "Delete an account access entity by its unique ID")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteAccess(@PathVariable UUID id);
}
