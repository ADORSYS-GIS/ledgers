package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "holder_access")
@Data
@NoArgsConstructor(force = true)
public class HolderAccess extends BankAccountAccess {


//    @Column(name = "holder_id")
    @Column(name = "holder_id")
    private String  holderId; // ID of the account holder

    @Column(name = "user_id", nullable = false)
    private String userId;



    // Constructor for creating HolderAccess with full access
    public HolderAccess(String holderId) {
        super(); // Call to the superclass constructor
        this.holderId = holderId;
        this.userId = userId;
    }

    // Method to suspend access
    public void suspendAccess() {
        this.setStatus(AccessStatus.SUSPENDED);
    }

    // Method to reactivate access
    public void reactivateAccess() {
        this.setStatus(AccessStatus.ACTIVE);
    }

    public void restrictAccess() {
        this.setStatus(AccessStatus.RESTRICTED);
    }

    // Method to modify permissions for other roles
    public void modifyPermissions(String newPermissions) {
        this.setPolicies(newPermissions);
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return String.format("HolderAccess{id='%s', accountId='%s', holderId='%s', status='%s'}",
                getId(), getAccountId(), holderId, getStatus());
    }
}