/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */
package de.adorsys.ledgers.baam.server.resource;

import de.adorsys.ledgers.baam.api.resource.*;
import de.adorsys.ledgers.baam.api.service.*;
import de.adorsys.ledgers.baam.db.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class BankAccountAccessResource implements BankAccountAccessAPI {

    private final BankAccountAccessService<BankAccountAccess> bankAccountAccessService;

    @Autowired
    public BankAccountAccessResource(BankAccountAccessService<BankAccountAccess> bankAccountAccessService) {
        this.bankAccountAccessService = bankAccountAccessService;
    }


    @Override
    public ResponseEntity<BankAccountAccess> createAccountAccess(BankAccountAccess bankAccountAccess) {
        return new ResponseEntity<>(bankAccountAccessService.createBankAccountAccess(bankAccountAccess), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BankAccountAccess>> getAllAccesses() {
        return ResponseEntity.ok(bankAccountAccessService.getAllBankAccountAccess());
    }

    @Override
    public ResponseEntity<BankAccountAccess> getAccessById(String Id) {
        return ResponseEntity.ok(bankAccountAccessService.getBankAccountAccessById(Id));
    }

    @Override
    public ResponseEntity<BankAccountAccess> updateAccess(String Id, BankAccountAccess bankAccountAccess) {
        bankAccountAccessService.modifyBankAccountAccess(bankAccountAccess);
        return ResponseEntity.ok(bankAccountAccess);
    }

    @Override
    public ResponseEntity<Void> deleteAccess(String Id) {
        bankAccountAccessService.revokeBankAccountAccess(Id);
        return ResponseEntity.ok().build();
    }
}
