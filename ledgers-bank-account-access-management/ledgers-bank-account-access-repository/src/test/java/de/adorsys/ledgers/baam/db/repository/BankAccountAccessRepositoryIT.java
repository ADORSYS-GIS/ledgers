/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.db.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.adorsys.ledgers.baam.db.domain.AccessCondition;
import de.adorsys.ledgers.baam.db.domain.AccessScope;
import de.adorsys.ledgers.baam.db.domain.AccessStatus;
import de.adorsys.ledgers.baam.db.domain.ExampleBankAccountAcess;
import de.adorsys.ledgers.baam.db.test.BaamRepositoryApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = BaamRepositoryApplication.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})

public class BankAccountAccessRepositoryIT {

    @Autowired
    private ExampleBankAccountAccessRepository exampleBankAccountAccessRepository;
    @Test
    void test_create_ok() {
        // Given
        exampleBankAccountAccessRepository.deleteAll(); // Clean up any existing records
        ExampleBankAccountAcess bankAccountAccess = new ExampleBankAccountAcess();
        bankAccountAccess.setId("1");
        bankAccountAccess.setAccountId("1L");
        bankAccountAccess.setEntityId("2L");
        bankAccountAccess.setScope(AccessScope.READ);
        bankAccountAccess.setWeight(1.0);
        bankAccountAccess.setConditions(AccessCondition.AMOUNT_RESTRICTED);
        bankAccountAccess.setStatus(AccessStatus.ACTIVE);
        bankAccountAccess.setPolicies("Standard Policy");

        // When
        ExampleBankAccountAcess savedAccess = exampleBankAccountAccessRepository.save(bankAccountAccess);

        // Retrieve the saved object
        ExampleBankAccountAcess result = exampleBankAccountAccessRepository.findById(savedAccess.getId()).orElse(null);

        // Then
        assertNotNull(result);
    }
}
