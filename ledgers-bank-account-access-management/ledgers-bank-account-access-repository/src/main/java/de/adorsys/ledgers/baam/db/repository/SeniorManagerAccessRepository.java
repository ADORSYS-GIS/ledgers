/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.db.repository;
import de.adorsys.ledgers.baam.db.domain.SeniorManagerAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeniorManagerAccessRepository extends JpaRepository<SeniorManagerAccess, Long> {

}
