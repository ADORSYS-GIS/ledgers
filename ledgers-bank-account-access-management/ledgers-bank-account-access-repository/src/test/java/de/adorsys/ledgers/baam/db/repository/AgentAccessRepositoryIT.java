package de.adorsys.ledgers.baam.db.repository;

import com.github.springtestdbunit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import  de.adorsys.ledgers.baam.db.domain.AgentAccess;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit.jupiter.*;
import org.springframework.test.context.support.*;
import org.springframework.test.context.transaction.*;
import de.adorsys.ledgers.baam.db.domain.AccessCondition;
import de.adorsys.ledgers.baam.db.domain.AccessScope;
import de.adorsys.ledgers.baam.db.domain.AccessStatus;
import  de.adorsys.ledgers.baam.db.test.BaamRepositoryApplication;


import static org.junit.jupiter.api.Assertions.*;

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
