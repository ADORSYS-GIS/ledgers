package de.adorsys.ledgers.baam.db.repository;

import de.adorsys.ledgers.baam.db.domain.AgentAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentAccessRepository extends JpaRepository<AgentAccess, String> {

}

