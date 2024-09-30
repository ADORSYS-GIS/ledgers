/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.db.test;

import de.adorsys.ledgers.baam.db.EnableBankAccountAccessRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBankAccountAccessRepository
public class BaamRepositoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaamRepositoryApplication.class, args);
    }
}
