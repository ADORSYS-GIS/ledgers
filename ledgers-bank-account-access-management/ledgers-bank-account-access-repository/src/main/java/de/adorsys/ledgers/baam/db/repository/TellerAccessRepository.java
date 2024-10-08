package de.adorsys.ledgers.baam.db.repository;


import de.adorsys.ledgers.baam.db.domain.TellerAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TellerAccessRepository extends JpaRepository<TellerAccess, String> {

}
