/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.db.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import de.adorsys.ledgers.aa.db.domain.AccountAccessEntity;
import java.util.UUID;

public interface AccountAccessRepository extends JpaRepository<AccountAccessEntity, UUID> {
}
