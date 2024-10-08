/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.api.domain.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateTO {
    private Currency currencyFrom;
    private String rateFrom;
    private Currency currency;
    private String rateTo;
    private LocalDate rateDate;
    private String rateContract;
}
