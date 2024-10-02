package de.adorsys.ledgers.baam.db.repository;

import de.adorsys.ledgers.baam.db.domain.PoAAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoAAccessRepository extends JpaRepository<PoAAccess, String> {
    List<PoAAccess> findByAttorneyInFact(String attorneyInFact);
    List<PoAAccess> findByAccountId(String accountId);
}
