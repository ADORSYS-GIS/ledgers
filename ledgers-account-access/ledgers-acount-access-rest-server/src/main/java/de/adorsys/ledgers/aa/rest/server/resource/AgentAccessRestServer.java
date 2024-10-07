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
import de.adorsys.ledgers.aa.rest.api.resource.AgentAccessRestAPI;
import de.adorsys.ledgers.aa.db.domain.AgentAccessEntity;
import de.adorsys.ledgers.aa.service.api.service.AgentAccessServiceAPI;

@RestController
public class AgentAccessRestServer implements AgentAccessRestAPI {

    @Autowired
    private AgentAccessServiceAPI service;

    @Override
    public ResponseEntity<AgentAccessEntity> createAccess(AgentAccessEntity access) {
        return new ResponseEntity<>(service.createAccess(access), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AgentAccessEntity> getAccessById(UUID id) {
        return new ResponseEntity<>(service.getAccessById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AgentAccessEntity>> getAllAccesses() {
        return new ResponseEntity<>(service.getAllAccesses(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AgentAccessEntity> updateAccess(UUID id, AgentAccessEntity access) {
        return new ResponseEntity<>(service.updateAccess(id, access), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteAccess(UUID id) {
        service.deleteAccess(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
