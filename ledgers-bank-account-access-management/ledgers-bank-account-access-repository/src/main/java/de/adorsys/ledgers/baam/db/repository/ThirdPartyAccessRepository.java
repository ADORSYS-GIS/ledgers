package de.adorsys.ledgers.baam.db.repository;

import de.adorsys.ledgers.baam.db.domain.ThirdPartyAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyAccessRepository extends JpaRepository<ThirdPartyAccess, String> {

}

