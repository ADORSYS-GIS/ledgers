package de.adorsys.ledgers.baam.db.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.adorsys.ledgers.baam.db.domain.*;
import de.adorsys.ledgers.baam.db.test.BaamRepositoryApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = BaamRepositoryApplication.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})

public class AgentAccessRepositoryIT {

    @Autowired
    private AgentAccessRepository agentAccessRepository;

    @Test
    void test_create_ok() {
        // Given
        agentAccessRepository.deleteAll(); // Clean up any existing records
        AgentAccess agentAccess = new AgentAccess();
        agentAccess.setId("1");
        agentAccess.setAccountId("1L");
        agentAccess.setEntityId("2L");
        agentAccess.setScope(AccessScope.EXECUTE); // Example action scope
        agentAccess.setWeight(0.5); // Partial authority
        agentAccess.setConditions(AccessCondition.AMOUNT_RESTRICTED); // Example condition
        agentAccess.setStatus(AccessStatus.ACTIVE); // Agent is active
        agentAccess.setPolicies("Payment-Only Policy"); // Example policy

        // When
        AgentAccess savedAccess = agentAccessRepository.save(agentAccess);

        // Retrieve the saved object
        AgentAccess result = agentAccessRepository.findById(savedAccess.getId()).orElse(null);

        // Then
        assertNotNull(result);
    }
}
