package de.adorsys.ledgers.postings.impl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import de.adorsys.ledgers.postings.api.domain.AccountCategoryBO;
import de.adorsys.ledgers.postings.api.domain.BalanceSideBO;
import de.adorsys.ledgers.postings.api.domain.LedgerAccountBO;
import de.adorsys.ledgers.postings.api.domain.LedgerBO;
import de.adorsys.ledgers.postings.api.service.LedgerService;
import de.adorsys.ledgers.postings.impl.test.PostingsApplication;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.io.IOException;
import java.io.InputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PostingsApplication.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("ITLoadCoaIFRSTest-db-entries.xml")
@DatabaseTearDown(value = {"ITLoadCoaIFRSTest-db-delete.xml"}, type = DatabaseOperation.DELETE_ALL)
public class LoadCoaIFRSIT {
    private static final String SYSTEM = "System";
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private LedgerService ledgerService;

    @Before
    public void before() {
        final YAMLFactory ymlFactory = new YAMLFactory();
        mapper = new ObjectMapper(ymlFactory);
    }

    @Test
    public void test_load_coa_ok() throws IOException {
        LedgerBO ledgerBO = ledgerService.findLedgerById("Zd0ND5YwSzGwIfZilhumPg").orElse(null);
        Assume.assumeNotNull(ledgerBO);
        InputStream inputStream = LoadLedgerAccountYMLTest.class.getResourceAsStream("ITLoadCoaIFRSTest-coa.yml");
        LedgerAccountBO[] ledgerAccounts = mapper.readValue(inputStream, LedgerAccountBO[].class);
        for (LedgerAccountBO ledgerAccount : ledgerAccounts) {

            if (ledgerAccount.getName() == null)
                Assert.fail("Missing account name for " + ledgerAccount.getShortDesc());
            String name = ledgerAccount.getName();

            LedgerAccountBO parent = null;
            if (name.contains(".")) {
                String parentName = name.substring(0, name.lastIndexOf('.'));
                parent = new LedgerAccountBO();
                parent.setLedger(ledgerBO);
                parent.setName(parentName);
            }
            ledgerAccount.setLedger(ledgerBO);
            ledgerAccount.setParent(parent);
            ledgerService.newLedgerAccount(ledgerAccount, SYSTEM);
        }

        LedgerAccountBO la = ledgerService.findLedgerAccount(ledgerBO, "2.3");
        Assume.assumeNotNull(la);
        Assert.assertEquals("Other Reserves (Accumulated Other Comprehensive Income)", la.getShortDesc());
        Assert.assertEquals(AccountCategoryBO.EQ, la.getCategory());
        Assert.assertEquals(BalanceSideBO.DrCr, la.getBalanceSide());
    }

}
