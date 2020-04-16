package de.adorsys.ledgers.um.db.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import de.adorsys.ledgers.um.db.domain.AccessType;
import de.adorsys.ledgers.um.db.domain.AccountAccess;
import de.adorsys.ledgers.um.db.domain.UserEntity;
import de.adorsys.ledgers.um.db.test.UmRepositoryApplication;
import de.adorsys.ledgers.util.Ids;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UmRepositoryApplication.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("AccountAccessRepositoryIT-db-entries.xml")
@DatabaseTearDown(value = {"db-delete-all.xml"}, type = DatabaseOperation.DELETE_ALL)
class AccountAccessRepositoryIT {

    @Autowired
    private AccountAccessRepository accountAccessRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void test_create_ok() {
        // Given
        AccountAccess accountAccess = new AccountAccess();
        accountAccess.setIban("FakeIban");
        accountAccess.setCurrency("EUR");
        accountAccess.setAccessType(AccessType.OWNER);
        accountAccess.setAccountId("accountId");
        UserEntity user = new UserEntity();
        user.setId(Ids.id());
        user.setPin("1234");
        user.setLogin("test");
        user.setEmail("test@mail.de");
        user.getAccountAccesses().add(accountAccess);
        user = userRepository.save(user);
        accountAccess = user.getAccountAccesses().iterator().next();

        // When
        AccountAccess result = accountAccessRepository.findById(accountAccess.getId()).orElse(null);

        // Then
        assertNotNull(result);
    }
}