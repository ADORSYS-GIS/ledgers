/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.rest.client;

import org.springframework.context.annotation.*;

import java.lang.annotation.*;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({
        LedgersDepositAccountRestClientConfiguration.class,
})
public @interface EnableLedgersDepositAccountRestClient {
}
