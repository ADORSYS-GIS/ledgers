package de.adorsys.ledgers.baam.db.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SeniorManagerAccessTest {

    @Test
    public void testCreateManagerRole() {
        SeniorManagerAccess seniorManager = new SeniorManagerAccess();
        seniorManager.createManagerRole("Manager1", "CAN_VIEW");

        assertTrue(seniorManager.getManagerRoles().containsKey("Manager1"));
        assertEquals("CAN_VIEW", seniorManager.getManagerRoles().get("Manager1"));
    }

    @Test
    public void testModifyManagerRole() {
        SeniorManagerAccess seniorManager = new SeniorManagerAccess();
        seniorManager.createManagerRole("Manager1", "CAN_VIEW");
        seniorManager.modifyManagerRole("Manager1", "CAN_EDIT");
    
        assertEquals("CAN_EDIT", seniorManager.getManagerRoles().get("Manager1"));
    }
    
    @Test
    public void testRevokeManagerRole() {
        SeniorManagerAccess seniorManager = new SeniorManagerAccess();
        seniorManager.createManagerRole("Manager1", "CAN_VIEW");
        seniorManager.revokeManagerRole("Manager1");
    
        assertFalse(seniorManager.getManagerRoles().containsKey("Manager1"));
    }
    
    @Test
    public void testSuspendAndActivateAccess() {
        SeniorManagerAccess seniorManager = new SeniorManagerAccess();
        
        seniorManager.suspendAccess();
        assertEquals(AccessStatus.SUSPENDED, seniorManager.getStatus());
    
        seniorManager.activateAccess();
        assertEquals(AccessStatus.ACTIVE, seniorManager.getStatus());
    }
    
    @Test
    public void testCreateManagerRoleWhenSuspended() {
        SeniorManagerAccess seniorManager = new SeniorManagerAccess();
        seniorManager.suspendAccess();
    
        assertThrows(IllegalStateException.class, () -> {
            seniorManager.createManagerRole("Manager1", "CAN_VIEW");
        });
    }
    
}
