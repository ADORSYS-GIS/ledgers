/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.rest.client;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackageClasses= {LedgersDepositAccountRestClientBasePackage.class})
public class LedgersDepositAccountRestClientConfiguration {
}
