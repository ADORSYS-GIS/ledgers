/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.rest.api.resource;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import de.adorsys.ledgers.aa.db.domain.AgentAccessEntity;

@RequestMapping("/api/agent-accesses")
public interface AgentAccessRestAPI {

    @PostMapping
    ResponseEntity<AgentAccessEntity> createAccess(@RequestBody AgentAccessEntity access);

    @GetMapping("/{id}")
    ResponseEntity<AgentAccessEntity> getAccessById(@PathVariable UUID id);

    @GetMapping
    ResponseEntity<List<AgentAccessEntity>> getAllAccesses();

    @PutMapping("/{id}")
    ResponseEntity<AgentAccessEntity> updateAccess(@PathVariable UUID id, @RequestBody AgentAccessEntity access);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteAccess(@PathVariable UUID id);
}
