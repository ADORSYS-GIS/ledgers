package de.adorsys.ledgers.baam.db.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.adorsys.ledgers.baam.db.domain.TellerAccess;
import de.adorsys.ledgers.baam.db.test.BaamRepositoryApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BaamRepositoryApplication.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
public class TellerAccessRepositoryTest {

    @Autowired
    private TellerAccessRepository tellerAccessRepository;

    @Test
    void testTellerAccessCreation() {

         TellerAccess tellerAccess = new TellerAccess();

        tellerAccess.setId("123");

        // Save to repository (persist)
        TellerAccess savedTellerAccess = tellerAccessRepository.save(tellerAccess);
        savedTellerAccess.setId("456");
        assertNotEquals(savedTellerAccess.getId(), tellerAccess.getId());

    }


    @Test
    void testSetDailyLimitInDatabase() {
        // Create and save an entity
        TellerAccess tellerAccess = new TellerAccess();
        tellerAccess.setDailyLimit(3000.0);
        tellerAccess.setId("123");

        TellerAccess savedTellerAccess = tellerAccessRepository.save(tellerAccess);

        // Update daily limit
        savedTellerAccess.setDailyLimit(3000.0);

        // Retrieve from DB and verify the daily limit change
        Optional<TellerAccess> retrievedTellerAccess = tellerAccessRepository.findById("123");
        assertTrue(retrievedTellerAccess.isPresent());
        assertEquals(3000.0, retrievedTellerAccess.get().getDailyLimit());
    }
}
