package de.adorsys.ledgers.baam.db.repository;


import de.adorsys.ledgers.baam.db.domain.ManagerAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ManagerAccessRepository extends JpaRepository<ManagerAccess, String> {

}