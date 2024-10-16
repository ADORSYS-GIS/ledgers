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
import de.adorsys.ledgers.aa.rest.api.resource.AccountAccessTemplateRestAPI;
import de.adorsys.ledgers.aa.db.domain.AccountAccessTemplateEntity;
import de.adorsys.ledgers.aa.service.api.service.AccountAccessTemplateServiceAPI;

@RestController
public class AccountAccessTemplateRestServer implements AccountAccessTemplateRestAPI {

    @Autowired
    private AccountAccessTemplateServiceAPI service;

    @Override
    public ResponseEntity<AccountAccessTemplateEntity> createTemplate(AccountAccessTemplateEntity template) {
        return new ResponseEntity<>(service.createTemplate(template), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AccountAccessTemplateEntity> getTemplateById(UUID id) {
        return new ResponseEntity<>(service.getTemplateById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AccountAccessTemplateEntity>> getAllTemplates() {
        return new ResponseEntity<>(service.getAllTemplates(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AccountAccessTemplateEntity> updateTemplate(UUID id, AccountAccessTemplateEntity template) {
        return new ResponseEntity<>(service.updateTemplate(id, template), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteTemplate(UUID id) {
        service.deleteTemplate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
