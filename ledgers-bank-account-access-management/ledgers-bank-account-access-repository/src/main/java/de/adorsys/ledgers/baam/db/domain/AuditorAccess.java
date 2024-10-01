package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.*;
import java.util.logging.Logger;

@Entity
@Table(name = "auditor_access")
public class AuditorAccess extends BankAccountAccess {
    private static final Logger logger = Logger.getLogger(AuditorAccess.class.getName());

    // Constructor sets default values
    public AuditorAccess() {
        this.setStatus(AccessStatus.ACTIVE);  // By default, AuditorAccess is active
        this.setWeight(1.0);                  // Weight is always 1 for AuditorAccess
        logger.info("AuditorAccess created with default active status and weight of 1");
    }

    public boolean allowsAction(String action) {
        // Only allow read actions such as viewing transactions and balances
        return switch (action) {
            case "VIEW_ACCOUNT_BALANCES", "VIEW_TRANSACTION_HISTORY", "VIEW_ACCESS_LOGS" -> true;
            default -> false;  // No modifications are allowed
        };
    }

    // Overriding status management to ensure that AuditorAccess can be restricted or suspended
    public void suspendAccess() {
        this.setStatus(AccessStatus.SUSPENDED);
        logger.info("AuditorAccess has been suspended.");
    }

    public void activateAccess() {
        this.setStatus(AccessStatus.ACTIVE);
        logger.info("AuditorAccess has been reactivated.");
    }

    public void restrictAccess() {
        this.setStatus(AccessStatus.RESTRICTED);
        logger.info("AuditorAccess has been restricted.");
    }
}

