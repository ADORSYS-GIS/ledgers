package de.adorsys.ledgers.baam.db.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.adorsys.ledgers.baam.db.domain.DelegatedAccess;
import de.adorsys.ledgers.baam.db.domain.DelegatedAccessType;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BaamRepositoryApplication.class)

@ExtendWith(SpringExtension.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
class DelegatedAccessRepositoryTest {

    @Autowired
    private DelegatedAccessRepository delegatedAccessRepository;

    private DelegatedAccess delegatedAccess;

    @BeforeEach
    void setUp() {
        // Create a valid DelegatedAccess instance
        delegatedAccess = new DelegatedAccess();
        delegatedAccess.setId("1");
        delegatedAccess.setAccountId("1");
        delegatedAccess.setDelegatedType(DelegatedAccessType.GENERAL);
        delegatedAccess.setAccountId("accountId123");
    }

    @Test
    void testSaveDelegatedAccess() {
        // Save DelegatedAccess and verify it was saved
        DelegatedAccess savedDelegatedAccess = delegatedAccessRepository.save(delegatedAccess);
        assertNotNull(savedDelegatedAccess.getId(), "Saved DelegatedAccess should have an ID");
        assertEquals(DelegatedAccessType.GENERAL, savedDelegatedAccess.getDelegatedType(), "DelegatedAccess type should be GENERAL");
    }

    @Test
    void testFindDelegatedAccessById() {
        // Save DelegatedAccess and retrieve it by ID
        DelegatedAccess savedDelegatedAccess = delegatedAccessRepository.save(delegatedAccess);
        Optional<DelegatedAccess> foundDelegatedAccess = delegatedAccessRepository.findById(savedDelegatedAccess.getId());

        assertTrue(foundDelegatedAccess.isPresent(), "DelegatedAccess should be found by ID");
    }

    @Test
    void testFindDelegatedAccessByAccountId() {
        // Save DelegatedAccess and retrieve by account ID
        delegatedAccessRepository.save(delegatedAccess);
        List<DelegatedAccess> foundDelegatedAccess = delegatedAccessRepository.findByAccountId("accountId123");

        assertEquals(1, foundDelegatedAccess.size(), "There should be one DelegatedAccess for account 'accountId123'");
    }
}
