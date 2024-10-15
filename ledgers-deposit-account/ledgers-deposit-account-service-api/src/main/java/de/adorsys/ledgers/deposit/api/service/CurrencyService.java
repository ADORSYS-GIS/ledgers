/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.api.service;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.Set;


public interface CurrencyService {

    /**
     * @return list of supported currencies
     */
    Set<Currency> getSupportedCurrencies();

    /**
     * Return boolean value if currency is supported
     *
     * @param currency currency used
     */
    boolean isCurrencyValid(Currency currency);
}
