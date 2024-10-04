package de.adorsys.ledgers.baam.db.repository;

import de.adorsys.ledgers.baam.db.domain.DelegatedAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DelegatedAccessRepository extends JpaRepository<DelegatedAccess, String> {

    List<DelegatedAccess> findByAccountId(String accountId);
}
