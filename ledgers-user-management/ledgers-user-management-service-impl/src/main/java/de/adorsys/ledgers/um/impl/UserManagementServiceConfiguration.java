package de.adorsys.ledgers.um.impl;

import de.adorsys.ledgers.um.db.EnableUserManagmentRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = UserManagementServiceBasePackage.class)
@EnableUserManagmentRepository
public class UserManagementServiceConfiguration {
}
