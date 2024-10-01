/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.db.domain;

public enum AccessScope {

    READ,
    WRITE,
    EXECUTE,
    DELETE;

        public boolean allowsAction(String action) {
            try {
                AccessScope requestedAction = AccessScope.valueOf(action);
                return this == requestedAction;
            } catch (IllegalArgumentException e) {
                // Action not allowed
                return false;
            }
        }
    
    
}
