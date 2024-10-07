/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.aa.db;

import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.*;
@Configuration
@ComponentScan(basePackageClasses = {AccountAccessBasePackage.class})
@EnableJpaRepositories
//@EntityScan(basePackages = "de.adorsys.ledgers.aa.db.domain")

public class AccountAccessRepositoryConfiguration {
}
