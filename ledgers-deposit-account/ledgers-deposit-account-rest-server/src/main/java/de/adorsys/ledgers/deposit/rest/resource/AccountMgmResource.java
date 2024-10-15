/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.rest.resource;

import de.adorsys.ledgers.deposit.api.resource.AccountMgmResourceAPI;
import de.adorsys.ledgers.deposit.api.service.DepositAccountManagementService;
import de.adorsys.ledgers.deposit.rest.annotation.DepositResetResource;
import de.adorsys.ledgers.deposit.api.domain.account.AccountDetailsTO;
import de.adorsys.ledgers.deposit.api.domain.payment.AmountTO;
//import de.adorsys.ledgers.middleware.rest.security.ScaInfoHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;


@Slf4j
@RestController
@ComponentScan(basePackageClasses = DepositAccountManagementService.class)
@DepositResetResource
@RequiredArgsConstructor
@Service
@RequestMapping("/AccountManagement" )
public class AccountMgmResource implements AccountMgmResourceAPI {
    private final DepositAccountManagementService depositAccountService;
//    private final ScaInfoHolder scaInfoHolder;

    @Override
    @PreAuthorize("hasManagerAccessToUser(#userId)")
    public ResponseEntity<Boolean> createDepositAccountForUser(String userId, AccountDetailsTO accountDetailsTO) {
        boolean created = depositAccountService.createDepositAccount(userId,   accountDetailsTO);
        return ResponseEntity.ok(created);
    }


    @Override
    @PreAuthorize("hasManagerAccessToAccountId(#accountId)")
    public ResponseEntity<AccountDetailsTO> getAccountDetailsById(String accountId) {
        return ResponseEntity.ok(depositAccountService.getDepositAccountById(accountId, LocalDateTime.now(), true));
    }

    @Override
    @PreAuthorize("hasManagerAccessToAccountId(#accountId) && isEnabledAccount(#accountId)")
    public ResponseEntity<Void> depositCash(String accountId, AmountTO amount) {
        depositAccountService.depositCash(  accountId, amount);
        return ResponseEntity.accepted().build();
    }


    @Override
    @PreAuthorize("hasManagerAccessToAccountId(#accountId)")
    public ResponseEntity<Boolean> changeStatus(String accountId) {
        return ResponseEntity.ok(depositAccountService.changeStatus(accountId, false));
    }


}