/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.db.repository;
import de.adorsys.ledgers.aa.db.domain.AccountAccessTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AccountAccessTemplateRepository extends JpaRepository<AccountAccessTemplateEntity, UUID> {
}
