package de.adorsys.ledgers.baam.db.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.adorsys.ledgers.baam.db.domain.AccessStatus;
import de.adorsys.ledgers.baam.db.domain.AuditorAccess;
import de.adorsys.ledgers.baam.db.test.BaamRepositoryApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = BaamRepositoryApplication.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
public class AuditorAccessRepositoryIT {

    @Autowired
    private AuditorAccessRepository auditorAccessRepository;

    @Test
    void test_createAuditorAccess_ok() {
        // Given
        auditorAccessRepository.deleteAll(); // Clean up any existing records

        AuditorAccess auditorAccess = new AuditorAccess();
        auditorAccess.setId("1");
        auditorAccess.setAccountId("100L");
        auditorAccess.setEntityId("200L");
        auditorAccess.setStatus(AccessStatus.ACTIVE);  // Setting status explicitly

        // When
        AuditorAccess savedAccess = auditorAccessRepository.save(auditorAccess);

        // Retrieve the saved object
        AuditorAccess result = auditorAccessRepository.findById(savedAccess.getId()).orElse(null);

        // Then
        assertNotNull(result);
        assertEquals("100L", result.getAccountId());
        assertEquals(AccessStatus.ACTIVE, result.getStatus());
    }
}
