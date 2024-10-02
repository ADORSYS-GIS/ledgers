package de.adorsys.ledgers.baam.db.repository;

import de.adorsys.ledgers.baam.db.domain.AccessStatus;
import de.adorsys.ledgers.baam.db.domain.TellerAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TellerAccessTest {

    private TellerAccess tellerAccess;

    @BeforeEach
    void setUp() {
        tellerAccess = new TellerAccess();
    }

    @Test
    void testTellerAccessCreation() {
        // Create UUID
        UUID id = UUID.randomUUID();
        String tellerName = "John Doe";
        String tellerId = "TELLER123";
        AccessStatus status = AccessStatus.ACTIVE;
        double dailyLimit = 5000.0;
        Date createdDate = new Date();
        Date lastModifiedDate = new Date();

        // Set values using constructor
        TellerAccess tellerAccess = new TellerAccess(id, tellerName, tellerId, status, dailyLimit, createdDate, lastModifiedDate, "TELLER_ACCESS");

        // Assert that the values have been set correctly
        assertEquals(id, tellerAccess.getId());
        assertEquals(tellerName, tellerAccess.getName());
        assertEquals(tellerId, tellerAccess.getTellerId());
        assertEquals(status, tellerAccess.getStatus());
        assertEquals(dailyLimit, tellerAccess.getDailyLimit());
        assertEquals(createdDate, tellerAccess.getCreatedDate());
        assertEquals(lastModifiedDate, tellerAccess.getLastModifiedDate());
        assertEquals("TELLER_ACCESS", tellerAccess.getAccessType());
    }

    @Test
    void testStatusUpdate() {
        // Initial status
        tellerAccess.setStatus(AccessStatus.ACTIVE);
        assertEquals(AccessStatus.ACTIVE, tellerAccess.getStatus());

        // Change status to SUSPENDED
        tellerAccess.setStatus(AccessStatus.SUSPENDED);
        assertEquals(AccessStatus.SUSPENDED, tellerAccess.getStatus());
    }

    @Test
    void testSetDailyLimit() {
        tellerAccess.setDailyLimit(2000.0);
        assertEquals(2000.0, tellerAccess.getDailyLimit());

        // Update daily limit
        tellerAccess.setDailyLimit(3000.0);
        assertEquals(3000.0, tellerAccess.getDailyLimit());
    }

    @Test
    void testSetCreatedDate() {
        Date now = new Date();
        tellerAccess.setCreatedDate(now);
        assertEquals(now, tellerAccess.getCreatedDate());
    }

    @Test
    void testSetLastModifiedDate() {
        Date now = new Date();
        tellerAccess.setLastModifiedDate(now);
        assertEquals(now, tellerAccess.getLastModifiedDate());
    }
}
