package de.adorsys.ledgers.baam.db.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.adorsys.ledgers.baam.db.domain.AccessStatus;
import de.adorsys.ledgers.baam.db.domain.ConsentType;
import de.adorsys.ledgers.baam.db.domain.ThirdPartyAccess;
import de.adorsys.ledgers.baam.db.test.BaamRepositoryApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = BaamRepositoryApplication.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
public class ThirdPartyAccessRepositoryIT {

    @Autowired
    private ThirdPartyAccessRepository thirdPartyAccessRepository;

    @Test
    void test_createThirdPartyAccess_ok() {
        // Given
        thirdPartyAccessRepository.deleteAll(); // Clean up any existing records

        ThirdPartyAccess thirdPartyAccess = new ThirdPartyAccess();
        thirdPartyAccess.setId("1");
        thirdPartyAccess.setAccountId("100L");
        thirdPartyAccess.setEntityId("300L");
        thirdPartyAccess.setConsentType(ConsentType.PAYMENT_INITIATION_CONSENT);
        thirdPartyAccess.setExpirationDate(LocalDateTime.now().plusDays(90));
        thirdPartyAccess.setMaxTransactionAmount(1000.00);
        thirdPartyAccess.setStatus(AccessStatus.ACTIVE);

        // When
        ThirdPartyAccess savedAccess = thirdPartyAccessRepository.save(thirdPartyAccess);

        // Retrieve the saved object
        ThirdPartyAccess result = thirdPartyAccessRepository.findById(savedAccess.getId()).orElse(null);

        // Then
        assertNotNull(result);
        assertEquals("100L", result.getAccountId());
        assertEquals(ConsentType.PAYMENT_INITIATION_CONSENT, result.getConsentType());
        assertEquals(1000.00, result.getMaxTransactionAmount());
    }
}
