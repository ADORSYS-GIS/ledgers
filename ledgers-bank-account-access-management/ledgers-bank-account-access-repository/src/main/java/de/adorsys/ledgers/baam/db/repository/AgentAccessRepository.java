package de.adorsys.ledgers.baam.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.adorsys.ledgers.baam.db.domain.AgentAccess;

@Repository
public interface AgentAccessRepository extends JpaRepository<AgentAccess, String> {

}

