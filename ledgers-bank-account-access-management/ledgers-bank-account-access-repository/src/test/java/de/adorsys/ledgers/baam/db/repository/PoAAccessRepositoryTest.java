package de.adorsys.ledgers.baam.db.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.adorsys.ledgers.baam.db.domain.PoAAccess;
import de.adorsys.ledgers.baam.db.domain.PoAType;
import de.adorsys.ledgers.baam.db.test.BaamRepositoryApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BaamRepositoryApplication.class)

@ExtendWith(SpringExtension.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
class PoAAccessRepositoryTest {

    @Autowired
    private PoAAccessRepository poaAccessRepository;

    private PoAAccess poaAccess;

    @BeforeEach
    void setUp() {
        // Create a valid PoAAccess instance
        poaAccess = new PoAAccess();
        poaAccess.setId("1");
        poaAccess.setAccountId("1");
        poaAccess.setAttorneyInFact("John Doe");
        poaAccess.setPoaType(PoAType.GENERAL);
        poaAccess.setAccountId("accountId123");
        poaAccess.setValidFrom(LocalDateTime.now());
        poaAccess.setValidUntil(LocalDateTime.now().plusDays(1));

    }

    @Test
    void testSavePoAAccess() {
        // Save PoAAccess and verify it was saved
        PoAAccess savedPoAAccess = poaAccessRepository.save(poaAccess);
        assertNotNull(savedPoAAccess.getId(), "Saved PoAAccess should have an ID");
        assertEquals("John Doe", savedPoAAccess.getAttorneyInFact(), "Attorney-in-fact should be John Doe");
        assertEquals(PoAType.GENERAL, savedPoAAccess.getPoaType(), "PoA type should be GENERAL");
    }

    @Test
    void testFindPoAAccessById() {
        // Save PoAAccess and retrieve it by ID
        PoAAccess savedPoAAccess = poaAccessRepository.save(poaAccess);
        Optional<PoAAccess> foundPoAAccess = poaAccessRepository.findById(savedPoAAccess.getId());

        assertTrue(foundPoAAccess.isPresent(), "PoAAccess should be found by ID");
        assertEquals(savedPoAAccess.getAttorneyInFact(), foundPoAAccess.get().getAttorneyInFact(), "Attorney-in-fact should match");
    }

    @Test
    void testFindPoAAccessByAttorneyInFact() {
        // Save PoAAccess and retrieve by attorney-in-fact
        poaAccessRepository.save(poaAccess);
        List<PoAAccess> foundPoAs = poaAccessRepository.findByAttorneyInFact("John Doe");

        assertEquals(1, foundPoAs.size(), "There should be one PoAAccess for 'John Doe'");
        assertEquals("accountId123", foundPoAs.get(0).getAccountId(), "Account ID should match");
    }

    @Test
    void testIsActivePoAAccess() {
        // Test if PoAAccess is active based on the date range
        poaAccessRepository.save(poaAccess);
        assertTrue(poaAccess.isActive(), "PoA should be active within the valid period");
    }

    @Test
    void testExpiredPoAAccess() {
        // Create and save an expired PoAAccess
        poaAccess.setValidUntil(LocalDateTime.now());
        poaAccessRepository.save(poaAccess);

        assertFalse(poaAccess.isActive(), "PoA should be inactive after expiration date");
    }

    @Test
    void testFindPoAAccessByAccountId() {
        // Save PoAAccess and retrieve by account ID
        poaAccessRepository.save(poaAccess);
        List<PoAAccess> foundPoAs = poaAccessRepository.findByAccountId("accountId123");

        assertEquals(1, foundPoAs.size(), "There should be one PoAAccess for account 'accountId123'");
        assertEquals("John Doe", foundPoAs.get(0).getAttorneyInFact(), "Attorney-in-fact should be John Doe");
    }
}
