package de.adorsys.ledgers.baam.db.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.adorsys.ledgers.baam.db.domain.AccessStatus;
import de.adorsys.ledgers.baam.db.domain.SeniorManagerAccess;
import de.adorsys.ledgers.baam.db.test.BaamRepositoryApplication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@SpringBootTest(classes = BaamRepositoryApplication.class)

@ExtendWith(SpringExtension.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
        
public class SeniorManagerAccessTest {

    @Autowired
    private SeniorManagerAccessRepository seniorManagerAccessRepository;

    @Test
    public void testCreateManagerRole() {

        SeniorManagerAccess seniorManager = new SeniorManagerAccess();
        seniorManager.setId("1"); 
        seniorManager.createManagerRole("Manager1", "CAN_VIEW");
        seniorManagerAccessRepository.save(seniorManager);

        assertTrue(seniorManager.getManagerRoles().containsKey("Manager1"));
        assertEquals("CAN_VIEW", seniorManager.getManagerRoles().get("Manager1"));
    }

    @Test
    public void testModifyManagerRole() {
        SeniorManagerAccess seniorManager = new SeniorManagerAccess();
        seniorManager.setId("1");
        seniorManager.createManagerRole("Manager1", "CAN_VIEW");
        seniorManager.modifyManagerRole("Manager1", "CAN_EDIT");
    
        seniorManagerAccessRepository.save(seniorManager);

        assertEquals("CAN_EDIT", seniorManager.getManagerRoles().get("Manager1"));
    }
    
    @Test
    public void testRevokeManagerRole() {
        SeniorManagerAccess seniorManager = new SeniorManagerAccess();
        seniorManager.setId("1");
        seniorManager.createManagerRole("Manager1", "CAN_VIEW");
        seniorManager.revokeManagerRole("Manager1");
    
        seniorManagerAccessRepository.save(seniorManager);

        assertFalse(seniorManager.getManagerRoles().containsKey("Manager1"));
    }
    
    @Test
    public void testSuspendAndActivateAccess() {
        SeniorManagerAccess seniorManager = new SeniorManagerAccess();
        seniorManager.setId("1");
        seniorManager.suspendAccess();
        seniorManagerAccessRepository.save(seniorManager);
        assertEquals(AccessStatus.SUSPENDED, seniorManager.getStatus());
    
        seniorManager.activateAccess();
        seniorManagerAccessRepository.save(seniorManager);
        assertEquals(AccessStatus.ACTIVE, seniorManager.getStatus());
    }
    
    @Test
    public void testCreateManagerRoleWhenSuspended() {
        SeniorManagerAccess seniorManager = new SeniorManagerAccess();
        seniorManager.setId("1");
        seniorManager.suspendAccess();

        seniorManagerAccessRepository.save(seniorManager);
    
        assertThrows(IllegalStateException.class, () -> {
            seniorManager.createManagerRole("Manager1", "CAN_VIEW");
        });
    }
    
}
