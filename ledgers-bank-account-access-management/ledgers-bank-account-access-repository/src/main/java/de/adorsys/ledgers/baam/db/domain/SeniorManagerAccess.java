package de.adorsys.ledgers.baam.db.domain;


import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table (name = "senior_manager_access")
public class SeniorManagerAccess extends BankAccountAccess {

    @ElementCollection
    private Map<String, String> managerRoles = new HashMap<>();
   
    public SeniorManagerAccess() {
            this.setStatus(AccessStatus.ACTIVE) ;
    }

    public Map<String, String> getManagerRoles() {
        return managerRoles;
    }    

    public void createManagerRole(String managerId, String permissions) {
        if (this.getStatus() != AccessStatus.ACTIVE) {
            throw new IllegalStateException("Cannot create roles when status is not active.");
        }
        managerRoles.put(managerId, permissions);
    }

    public void modifyManagerRole(String managerId, String newPermissions) {
        if (managerRoles.containsKey(managerId)) {
            managerRoles.put(managerId, newPermissions);
        } else {
            throw new IllegalArgumentException("Manager role not found.");
        }
    }

    public void revokeManagerRole(String managerId) {
        if (managerRoles.containsKey(managerId)) {
            managerRoles.remove(managerId);
        } else {
            throw new IllegalArgumentException("Manager role not found.");
        }
    }

    public void suspendAccess() {
        this.setStatus(AccessStatus.SUSPENDED);
    }

    public void activateAccess() {
        this.setStatus(AccessStatus.ACTIVE);
    }
}
