/*
 * Copyright (c) 2018-2023 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.ledgers.baam.db.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.adorsys.ledgers.baam.db.domain.AccessCondition;
import de.adorsys.ledgers.baam.db.domain.AccessScope;
import de.adorsys.ledgers.baam.db.test.BaamRepositoryApplication;
import de.adorsys.ledgers.baam.db.domain.HolderAccess;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;


@SpringBootTest(classes = BaamRepositoryApplication.class) // Main application class
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
public class HolderAccessRepositoryIT {

    @Autowired
    private HolderAccessRepository repository;
    void mockTest() {
        repository.deleteAll();
        HolderAccess access = new HolderAccess();
        access.setId("1");
        access.setUserId("1");
        access.setHolderId("1");
        access.setAccountId("1");
        access.setEntityId("1");
        access.setWeight(1);
        access.setScope(AccessScope.READ);
        access.setConditions(AccessCondition.AMOUNT_RESTRICTED);
        access.setPolicies("Standard Policy");
        repository.save(access);

        HolderAccess result = repository.findById(access.getId()).orElse(null);

        assert result != null;



    }

}