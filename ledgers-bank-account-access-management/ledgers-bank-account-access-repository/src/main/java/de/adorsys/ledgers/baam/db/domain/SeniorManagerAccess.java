/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Entity
@Data
public class SeniorManagerAccess {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private String id;

        private String name;

        @Enumerated(EnumType.STRING)
        private AccessStatus status;

        @ElementCollection
        private Map<String, String> managerRoles = new HashMap<>();

        // Constructor
        public SeniorManagerAccess(String name, AccessStatus status) {
            this.name = name;
            this.status = status.ACTIVE;
        }

        // Default constructor for JPA
        public SeniorManagerAccess() {}

        // Getters and Setters
        //...

        // Methods
        public void createManagerRole(String managerId, String permissions) {
            if (this.status != AccessStatus.ACTIVE) {
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
            this.status = AccessStatus.SUSPENDED;
        }

        public void activateAccess() {
            this.status = AccessStatus.ACTIVE;
        }
    }
