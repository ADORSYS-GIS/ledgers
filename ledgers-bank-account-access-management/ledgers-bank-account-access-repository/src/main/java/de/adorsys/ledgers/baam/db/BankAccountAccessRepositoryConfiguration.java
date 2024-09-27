/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.db;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackageClasses = {BankAccountAccessBasePackage.class})
@EnableJpaRepositories
@EntityScan(basePackages = "de.adorsys.ledgers.baam.db.domain")
public class BankAccountAccessRepositoryConfiguration {
}
