package de.adorsys.ledgers.baam.db.domain;

/**
 * Enum representing different types of consents in the context of open banking.
 * These consents define the scope of access granted to third-party service providers.
 */
public enum ConsentType {

     /*
     * The need to implement Other Open Banking Services may arouse the creation of more ConsentTypes
     */

    /**
     * Consent allowing the third-party to access account information such as
     * balances, transaction histories, and account details without modifying them.
     * This is typically used by Account Information Service Providers (AISP).
     */
    ACCOUNT_INFORMATION_CONSENT,

    /**
     * Consent allowing the third-party to initiate payments from the account
     * on behalf of the account holder. This is typically used by Payment
     * Initiation Service Providers (PISP).
     */
    PAYMENT_INITIATION_CONSENT,

    /**
     * Consent allowing the third-party to verify whether the account has
     * sufficient funds for a particular transaction. No additional account
     * information is provided beyond the confirmation of funds.
     */
    CONFIRMATION_OF_FUNDS_CONSENT

}

