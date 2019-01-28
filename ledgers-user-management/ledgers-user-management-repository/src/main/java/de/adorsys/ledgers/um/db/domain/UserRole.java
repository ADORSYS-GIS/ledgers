package de.adorsys.ledgers.um.db.domain;

public enum UserRole {

    CUSTOMER, // A customer with associated bank accounts
    STAFF, // a staff member. Can access all accounts
    TECHNICAL, // a technical user. No SCA. User role reserved for TPP
    SYSTEM // A system user. FOr application management tasks.
}
