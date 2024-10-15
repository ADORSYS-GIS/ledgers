/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.deposit.rest;

import de.adorsys.ledgers.deposit.api.client.ExchangeRateClient;
import de.adorsys.ledgers.deposit.api.service.EnableDepositAccountService;
import de.adorsys.ledgers.postings.impl.EnablePostingService;
import de.adorsys.ledgers.util.EnableUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;

@EnableScheduling
@SpringBootApplication
@EnableUtils
@EnableDepositAccountService
@EnablePostingService
@EnableLedgersDepositRest
@EnableFeignClients(basePackageClasses = ExchangeRateClient.class)
public class LedgersDepositAccountRestApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(LedgersDepositAccountRestApplication.class).run(args);
    }
}
