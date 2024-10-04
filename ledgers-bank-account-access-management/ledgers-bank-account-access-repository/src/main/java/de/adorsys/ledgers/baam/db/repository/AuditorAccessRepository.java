package de.adorsys.ledgers.baam.db.repository;

import de.adorsys.ledgers.baam.db.domain.AuditorAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditorAccessRepository extends JpaRepository<AuditorAccess, String> {

}
