/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.db.domain;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class SeniorManagerAccess {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @Enumerated(EnumType.STRING)
        private AccessStatus status;

        @ElementCollection
        private Map<Long, String> managerRoles = new HashMap<>();

        // Constructor
        public SeniorManagerAccess(String name) {
            this.name = name;
            this.status = AccessStatus.ACTIVE;
        }

        // Default constructor for JPA
        public SeniorManagerAccess() {}

        // Getters and Setters
        //...

        // Methods
        public void createManagerRole(Long managerId, String permissions) {
            if (this.status != AccessStatus.ACTIVE) {
                throw new IllegalStateException("Cannot create roles when status is not active.");
            }
            managerRoles.put(managerId, permissions);
        }

        public void modifyManagerRole(Long managerId, String newPermissions) {
            if (managerRoles.containsKey(managerId)) {
                managerRoles.put(managerId, newPermissions);
            } else {
                throw new IllegalArgumentException("Manager role not found.");
            }
        }

        public void revokeManagerRole(Long managerId) {
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
