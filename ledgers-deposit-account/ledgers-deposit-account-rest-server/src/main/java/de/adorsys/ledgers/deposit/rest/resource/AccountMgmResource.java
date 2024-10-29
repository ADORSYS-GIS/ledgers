/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.rest.resource;

import de.adorsys.ledgers.deposit.api.domain.account.AccountDetailsTO;
import de.adorsys.ledgers.deposit.api.resource.AccountMgmResourceAPI;
import de.adorsys.ledgers.deposit.api.service.DepositAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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