/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.rest.resource;

import de.adorsys.ledgers.deposit.api.domain.account.*;
import de.adorsys.ledgers.deposit.api.resource.*;
import de.adorsys.ledgers.deposit.api.service.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

 @RestController
 @RequiredArgsConstructor
 @Service
 @RequestMapping("/AccountManagement" )
 public class AccountMgmResource implements AccountMgmResourceAPI {
     private final DepositAccountService depositAccountService;

     @Override
     public ResponseEntity<AccountDetailsTO> createDepositAccount(String authorizationHeader, AccountDetailsTO accountDetailsTO) {
         return null;
     }
 }