/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.rest.server.resource;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import de.adorsys.ledgers.aa.rest.api.resource.AccountAccessRestAPI;
import de.adorsys.ledgers.aa.db.domain.AccountAccessEntity;
import de.adorsys.ledgers.aa.service.api.service.AccountAccessServiceAPI;

@RestController
public class AccountAccessRestServer implements AccountAccessRestAPI {

    @Autowired
    private AccountAccessServiceAPI service;

    @Override
    public ResponseEntity<AccountAccessEntity> createAccess(AccountAccessEntity access) {
        return new ResponseEntity<>(service.createAccess(access), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AccountAccessEntity> getAccessById(UUID id) {
        return new ResponseEntity<>(service.getAccessById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AccountAccessEntity>> getAllAccesses() {
        return new ResponseEntity<>(service.getAllAccesses(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AccountAccessEntity> updateAccess(UUID id, AccountAccessEntity access) {
        return new ResponseEntity<>(service.updateAccess(id, access), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteAccess(UUID id) {
        service.deleteAccess(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
