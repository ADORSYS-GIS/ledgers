/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.api.utils;


public abstract class Constants {

    public static final String ACCOUNT_ID = "accountId";
    public static final String IBAN = "ibanParam";
    public static final String CURRENCY = "currency";
    public static final String ACCOUNT_IDENTIFIER_TYPE = "accountIdentifierType";
    public static final String ACCOUNT_IDENTIFIER = "accountIdentifier";

    public static final String USER_ID = "userId";

    public static final String EMAIL ="email";
    public static final String PAYMENT_ID = "paymentId";
    public static final String PAYMENT_TYPE = "paymentType";
    public static final String CONSENT_ID = "consentId";
    public static final String TRANSACTION_ID = "transactionId";



    public static final String QUERY_PARAM = "queryParam";
    public static final String BLOCKED = "blockedParam";
    public static final String PAGE = "page";
    public static final String SIZE = "size";

    public static final String LOCAL_DATE_YYYY_MM_DD_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TO_QUERY_PARAM = "dateTo";
    public static final String DATE_FROM_QUERY_PARAM = "dateFrom";
    public static final String WITH_BALANCE_QUERY_PARAM = "withBalance";

    private Constants() {
    }
}
