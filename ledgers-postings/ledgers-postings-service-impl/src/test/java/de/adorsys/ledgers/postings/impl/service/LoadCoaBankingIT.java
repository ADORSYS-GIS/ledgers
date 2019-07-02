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
@DatabaseSetup("LoadCoaBankingIT-db-entries.xml")
@DatabaseTearDown(value = {"LoadCoaBankingIT-db-delete.xml"}, type = DatabaseOperation.DELETE_ALL)
public class LoadCoaBankingIT {

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
        LedgerBO ledger = ledgerService.findLedgerById("Zd0ND5YwSzGwIfZilhumPg").orElse(null);
        Assume.assumeNotNull(ledger);

        InputStream inputStream = LoadLedgerAccountYMLTest.class.getResourceAsStream("LoadCoaBankingIT-coa.yml");
        LegAccYamlModel[] ledgerAccounts = mapper.readValue(inputStream, LegAccYamlModel[].class);
        for (LegAccYamlModel model : ledgerAccounts) {
            LedgerAccountBO parent = null;
            if (model.getParent() != null) {
                parent = new LedgerAccountBO();
                parent.setLedger(ledger);
                parent.setName(model.getParent());
            }
            LedgerAccountBO l = new LedgerAccountBO();
            l.setShortDesc(model.getShortDesc());
            l.setName(model.getName());
            l.setBalanceSide(model.getBalanceSide());
            l.setCategory(model.getCategory());
            l.setLedger(ledger);
            l.setParent(parent);
            ledgerService.newLedgerAccount(l, SYSTEM);
        }

        LedgerAccountBO la = ledgerService.findLedgerAccount(ledger, "1003");
        Assume.assumeNotNull(la);
        Assert.assertEquals("Cash in transit", la.getShortDesc());
        Assert.assertEquals(AccountCategoryBO.AS, la.getCategory());
        Assert.assertEquals(BalanceSideBO.Dr, la.getBalanceSide());
    }


    private static class LegAccYamlModel {
        private String shortDesc;
        private String name;
        private AccountCategoryBO category;
        private BalanceSideBO balanceSide;
        private String parent;

        public String getShortDesc() {
            return shortDesc;
        }

        public void setShortDesc(String shortDesc) {
            this.shortDesc = shortDesc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public AccountCategoryBO getCategory() {
            return category;
        }

        public void setCategory(AccountCategoryBO category) {
            this.category = category;
        }

        public BalanceSideBO getBalanceSide() {
            return balanceSide;
        }

        public void setBalanceSide(BalanceSideBO balanceSide) {
            this.balanceSide = balanceSide;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }
    }

}
