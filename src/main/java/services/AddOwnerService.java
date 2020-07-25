package services;

import models.Account;
import models.AddOwnerDTO;
import models.DepositWithdrawDTO;
import models.User;

public class AddOwnerService {

	private final AccountService as = new AccountService();
	private final UserService us = new UserService();
	private final DepositWithdrawService dps = new DepositWithdrawService();
	private final DepositWithdrawDTO dwdto = new DepositWithdrawDTO();

	public boolean AddOwner(AddOwnerDTO ao) {
		Account a = as.findById(ao.accountId);
		User u = us.findById(ao.userId);
		dwdto.accountId = a.getAccountId();
		dwdto.amount = 50;
		if (dps.withdraw(dwdto)) {
			as.accountOwner(a, u);
			return true;
		}

		return false;
	}

}
