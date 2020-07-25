package services;

import models.Account;
import models.DepositWithdrawDTO;

public class DepositWithdrawService {

	private static final AccountService as = new AccountService();

	public boolean deposit(DepositWithdrawDTO d) {
		Account a = as.findById(d.accountId);
		double currentBalance = a.getBalance();
		if (d.amount > 0 && a.getStatus().getStatusId() == 2) {
			a.setBalance(currentBalance + d.amount);
			as.updateAccount(a);
			return true;
		}
		return false;
	}

	public boolean withdraw(DepositWithdrawDTO w) {
		Account a = as.findById(w.accountId);
		double currentBalance = a.getBalance();
		if (currentBalance > w.amount && a.getStatus().getStatusId() == 2) {
			a.setBalance(currentBalance - w.amount);
			as.updateAccount(a);
			return true;
		}
		return false;
	}
}
