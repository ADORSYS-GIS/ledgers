package de.adorsys.ledgers.deposit.api.service.domain;

import de.adorsys.ledgers.postings.api.domain.AccountCategoryBO;
import de.adorsys.ledgers.postings.api.domain.BalanceSideBO;
import lombok.Data;

@Data
public class LedgerAccountModel {
    private String shortDesc;
    private String name;
    private AccountCategoryBO category;
    private BalanceSideBO balanceSide;
    private String parent;
}
