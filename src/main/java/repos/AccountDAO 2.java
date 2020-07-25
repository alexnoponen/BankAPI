package repos;

import models.Account;
import models.TransactionDTO;

public class AccountDAO implements IAccountDAO {

	private final IAccountDAO dao = new AccountDAO();

	public boolean submitAccount(Account account) {
		return dao.insertAccount(account);
	}

	public Account getAccountFromAccountID(int accountID) {
		return dao.getAccountFromAccountID(accountID);
	}

	public boolean withdraw(TransactionDTO withdraw, double balance) {
		return dao.withdraw(withdraw, balance);
	}

	public boolean deposit(TransactionDTO deposit, double balance) {
		return dao.deposit(deposit, balance);
	}

	public Account updateAccount(Account account, Account origin) {
		return dao.updateAccount(account, origin);
	}

	public int getAccountCount() {
		return dao.getAccountCount();
	}

	public Account[] getAccounts() {
		return dao.getAccounts();
	}

	public Account getAccountsById(int id) {
		return dao.getAccountsById(id);
	}

	public Account[] findAccountsByStatus(int id) {
		return dao.findAccountsByStatus(id);
	}

	public Account[] findAccountsByUser(int id) {
		return dao.findAccountsByUser(id);
	}

	@Override
	public boolean insertAccount(Account account) {
		return false;
	}



}