package de.adorsys.ledgers.app.mock;

import java.util.ArrayList;
import java.util.List;

public class BalancesData {
	private List<AccountBalances> balancesList = new ArrayList<>();

	public List<AccountBalances> getBalancesList() {
		return balancesList;
	}

	public void setBalancesList(List<AccountBalances> balancesList) {
		this.balancesList = balancesList;
	}
}
