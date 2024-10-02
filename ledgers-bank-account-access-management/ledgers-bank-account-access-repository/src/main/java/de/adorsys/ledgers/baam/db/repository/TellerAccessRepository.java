package de.adorsys.ledgers.baam.db.repository;


import de.adorsys.ledgers.baam.db.domain.TellerAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TellerAccessRepository extends JpaRepository<TellerAccess, UUID> {

}
