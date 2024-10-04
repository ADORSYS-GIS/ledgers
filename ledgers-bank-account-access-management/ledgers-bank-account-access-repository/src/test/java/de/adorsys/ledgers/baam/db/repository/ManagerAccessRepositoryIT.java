package de.adorsys.ledgers.baam.db.repository;

import de.adorsys.ledgers.baam.db.domain.AccessScope;
import de.adorsys.ledgers.baam.db.domain.AccessStatus;
import de.adorsys.ledgers.baam.db.domain.ManagerAccess;
import de.adorsys.ledgers.baam.db.domain.TypeOfManagedAccess;
import de.adorsys.ledgers.baam.db.test.BaamRepositoryApplication;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BaamRepositoryApplication.class)
@Transactional // Ensures that each test is executed within a transaction and rolled back afterwards
@Rollback // Reverts the changes made to the database after each test
public class ManagerAccessRepositoryIT {


    @Autowired
    private ManagerAccessRepository managerAccessRepository;

    // Variable to store a ManagerAccess object
    private ManagerAccess managerAccess;

    @BeforeEach
    public void setup() {
        // Initialize a ManagerAccess object for testing
        managerAccess = new ManagerAccess();
        managerAccess.setId("1");
        managerAccess.setWeight(0.8);
        managerAccess.setStatus(AccessStatus.ACTIVE);
        managerAccess.setManagedAccessTypes(new HashSet<>(Set.of(TypeOfManagedAccess.AGENT_ACCESS)));
        managerAccess.setScopeOfAccess(new HashSet<>(Set.of(AccessScope.READ)));
    }

    @Test
    public void testCreateManagerAccess() {
        // Save to the database via the injected repository
        ManagerAccess savedManager = managerAccessRepository.save(managerAccess);

        // Verify that the object is properly saved and has a generated ID
        assertThat(savedManager.getId()).isNotNull();
    }

    @Test
    public void testFindManagerAccessById() {
        // Save the ManagerAccess entity to the database
        managerAccessRepository.save(managerAccess);
        String managerId = managerAccess.getId();

        // Retrieve the ManagerAccess object from the database using the ID
        ManagerAccess foundManager = managerAccessRepository.findById(managerId).orElse(null);

        // Verify that the retrieved object matches the one that was saved
        assertThat(foundManager).isNotNull();
        assertThat(foundManager.getStatus()).isEqualTo(AccessStatus.ACTIVE);
        assertThat(foundManager.getManagedAccessTypes()).contains(TypeOfManagedAccess.AGENT_ACCESS);
        assertThat(foundManager.getScopeOfAccess()).contains(AccessScope.READ);
    }

    @Test
    public void testUpdateManagerAccess() {
        // Initially save a ManagerAccess
        managerAccessRepository.save(managerAccess);

        // Modify the details of the ManagerAccess

        managerAccess.setStatus(AccessStatus.RESTRICTED);

        Set<AccessScope> newScopes = new HashSet<>();
        newScopes.add(AccessScope.EXECUTE);
        managerAccess.setScopeOfAccess(newScopes);
        // Update the record
        ManagerAccess updatedManager = managerAccessRepository.save(managerAccess);

        // Verify that the information has been updated correctly

        assertThat(updatedManager.getStatus()).isEqualTo(AccessStatus.RESTRICTED);
        assertThat(updatedManager.getScopeOfAccess()).contains(AccessScope.EXECUTE);
    }

    @Test
    public void testDeleteManagerAccess() {
        // Save a ManagerAccess to the database
        managerAccessRepository.save(managerAccess);
        String managerId = managerAccess.getId();

        // Delete the record
        managerAccessRepository.deleteById(managerId);

        // Verify that the entity has been successfully deleted
        ManagerAccess deletedManager = managerAccessRepository.findById(managerId).orElse(null);
        assertThat(deletedManager).isNull();
    }
}