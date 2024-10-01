package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "agent_access")
public class AgentAccess extends BankAccountAccess {

    public AgentAccess() {
        this.setStatus(AccessStatus.ACTIVE);
    }

    // Impersonate the account holder to perform an action within the authorized scope
    public void impersonate(String action) {
        if (this.getStatus() != AccessStatus.ACTIVE) {
            throw new IllegalStateException("Access is not active, impersonation is not allowed.");
        }
        if (this.getScope().allowsAction(action)) {
            return;
            // Perform the payment logic
        } else {
            throw new IllegalArgumentException("Action not permitted within the current scope.");
        }
    }

    // Revoke the agent's access
    public void revokeAccess() {
        this.setStatus(AccessStatus.SUSPENDED);
    }

    // Activate agent access if it has been suspended
    public void activateAccess() {
        this.setStatus(AccessStatus.ACTIVE);
    }

    // Restrict access temporarily without full revocation
    public void restrictAccess() {
        this.setStatus(AccessStatus.RESTRICTED);
    }

    // Example usage scenario: Execute a payment on behalf of the account holder
    public void executePayment(double amount) {
        if (this.getStatus() == AccessStatus.ACTIVE && this.getScope().allowsAction("EXECUTE_PAYMENT")) {
           return;
            // Perform the payment logic
        } else {
            throw new IllegalStateException("Payment execution not allowed under the current access status or scope.");
        }
    }
}
