package services;

import models.DepositWithdrawDTO;
import models.TransferDTO;

public class TransferService {
	private static final DepositWithdrawService dws = new DepositWithdrawService();

	public boolean transfer(TransferDTO t) {
		DepositWithdrawDTO w = new DepositWithdrawDTO();
		w.accountId = t.sourceAccountId;
		w.amount = t.amount;

		DepositWithdrawDTO d = new DepositWithdrawDTO();
		d.accountId = t.targetAccountId;
		d.amount = t.amount;

		if (dws.withdraw(w) && dws.deposit(d)) {
			return true;
		}
		return false;
	}

}
