/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.sca.db;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.adorsys.ledgers.sca.db.domain.SCAOperationEntity;

@Configuration
@ComponentScan(basePackageClasses= {SCABasePackage.class})
@EnableJpaRepositories
@EntityScan(basePackageClasses= {SCAOperationEntity.class, Jsr310JpaConverters.class})
public class SCARepositoryConfiguration {
}
