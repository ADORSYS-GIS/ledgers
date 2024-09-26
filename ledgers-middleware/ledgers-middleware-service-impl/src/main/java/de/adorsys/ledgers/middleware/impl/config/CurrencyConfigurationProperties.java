/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.middleware.impl.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;//NOPMD

@Getter
@Configuration
@ConfigurationProperties(prefix = "ledgers.currency")
public class CurrencyConfigurationProperties {
    private final Set<Currency> currencies = new HashSet<>();
}
