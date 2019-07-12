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
import de.adorsys.ledgers.middleware.api.domain.payment.AmountTO;
import de.adorsys.ledgers.middleware.api.service.MiddlewareAccountManagementService;
import de.adorsys.ledgers.middleware.rest.annotation.MiddlewareUserResource;
import de.adorsys.ledgers.middleware.rest.security.ScaInfoHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@MiddlewareUserResource
@RequiredArgsConstructor
@RequestMapping("/staff-access" + AccountRestAPI.BASE_PATH)
public class AccountMgmStaffResource implements AccountMgmStaffResourceAPI {
    private final MiddlewareAccountManagementService middlewareAccountService;
    private final ScaInfoHolder scaInfoHolder;

    @Override
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Void> createDepositAccountForUser(String userId, AccountDetailsTO accountDetailsTO) {
        middlewareAccountService.createDepositAccount(userId, scaInfoHolder.getScaInfo(), accountDetailsTO);

        // TODO: change to created after Account Middleware service refactoring
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<AccountDetailsTO>> getListOfAccounts() {
        return ResponseEntity.ok(middlewareAccountService.listDepositAccountsByBranch(scaInfoHolder.getUserId()));
    }

    @Override
    @PreAuthorize("accountInfoById(#accountId)")
    public ResponseEntity<AccountDetailsTO> getAccountDetailsById(String accountId) {
        return ResponseEntity.ok(middlewareAccountService.getDepositAccountById(accountId, LocalDateTime.now(), true));
    }

    @Override
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Void> depositCash(String accountId, AmountTO amount) {
        middlewareAccountService.depositCash(scaInfoHolder.getScaInfo(), accountId, amount);
        return ResponseEntity.accepted().build();
    }
}
