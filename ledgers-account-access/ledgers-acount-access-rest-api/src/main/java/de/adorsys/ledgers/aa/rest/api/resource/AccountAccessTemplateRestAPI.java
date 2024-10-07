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
import de.adorsys.ledgers.aa.db.domain.AccountAccessTemplateEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/templates")
public interface AccountAccessTemplateRestAPI {

    @PostMapping
    ResponseEntity<AccountAccessTemplateEntity> createTemplate(@RequestBody AccountAccessTemplateEntity template);

    @GetMapping("/{id}")
    ResponseEntity<AccountAccessTemplateEntity> getTemplateById(@PathVariable UUID id);

    @GetMapping
    ResponseEntity<List<AccountAccessTemplateEntity>> getAllTemplates();

    @PutMapping("/{id}")
    ResponseEntity<AccountAccessTemplateEntity> updateTemplate(@PathVariable UUID id, @RequestBody AccountAccessTemplateEntity template);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTemplate(@PathVariable UUID id);
}
