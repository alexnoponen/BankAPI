package controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.Account;
import models.TransactionDTO;
import models.TransferDTO;
import models.User;
import services.AccountService;

public class AccountController {
	private static final AccountService as = new AccountService();
	private static final ObjectMapper om = new ObjectMapper();
	private static final RequestHelper rh = new RequestHelper();

	public void withdraw(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession ses = req.getSession(false);
		if (rh.isLoggedOut(req, res)) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		String body = rh.readBody(req, res);
		TransactionDTO withdraw = om.readValue(body, TransactionDTO.class);

		Account account = as.getAccountFromAccountID(withdraw.amountId);

		// GET AND CHECK CREDENTIAL OF CURRENT USER
		User u = (User) ses.getAttribute("user");
		if (!u.getRole().getRole().equals("Admin") && account.getUserId() != u.getUserId()) {

			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		} else {
			// success in accessing DB
			if (as.withdraw(withdraw, account.getBalance())) {
				res.setStatus(200);
				res.getWriter()
						.println("$" + withdraw.amount + " has been withdrawn from Account #" + withdraw.amountId);
				return;
			}
			// fail in accessing DB
			else {
				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
				return;
			}
		}
	}

	public void deposit(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession ses = req.getSession(false);
		if (rh.isLoggedOut(req, res)) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		String body = rh.readBody(req, res);
		TransactionDTO deposit = om.readValue(body, TransactionDTO.class);

		Account account = as.getAccountFromAccountID(deposit.amountId);

		// GET AND CHECK CREDENTIAL OF CURRENT USER
		User u = (User) ses.getAttribute("user");
		if (!u.getRole().getRole().equals("Admin") && account.getUserId() != u.getUserId()) {

			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		} else {
			// success in accessing DB
			if (as.deposit(deposit, account.getBalance())) {
				res.setStatus(200);
				res.getWriter().println("$" + deposit.amount + " has been deposited to Account #" + deposit.amountId);
				return;
			}
			// fail in accessing DB
			else {
				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
				return;
			}
		}

	}

	public void transfer(HttpServletRequest req, HttpServletResponse res) throws IOException {
		if (rh.isLoggedOut(req, res)) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		String body = rh.readBody(req, res);
		TransferDTO transfer = om.readValue(body, TransferDTO.class);

		Account sourceAccount = as.getAccountFromAccountID(transfer.sourceAccountId);
		Account targetAccount = as.getAccountFromAccountID(transfer.targetAccountId);

		// GET AND CHECK CREDENTIAL OF CURRENT USER
		HttpSession ses = req.getSession(false);

		User u = (User) ses.getAttribute("user");
		if (!u.getRole().getRole().equals("Admin") && sourceAccount.getUserId() != u.getUserId()) {

			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		} else {
			TransactionDTO withdraw = new TransactionDTO();
			TransactionDTO deposit = new TransactionDTO();

			withdraw.amountId = sourceAccount.getAccountId();
			withdraw.amount = transfer.amount;

			deposit.amountId = targetAccount.getAccountId();
			deposit.amount = transfer.amount;

			if (sourceAccount.getAccountId() == targetAccount.getAccountId()) {
				res.setStatus(200);
				res.getWriter().println("$" + transfer.amount + " has been transferred from Account #"
						+ sourceAccount.getAccountId() + " to Account #" + targetAccount.getAccountId());
				return;
			}

			if (as.withdraw(withdraw, sourceAccount.getBalance())) {

				if (as.deposit(deposit, targetAccount.getBalance())) {
					res.setStatus(200);
					res.getWriter().println("$" + transfer.amount + " has been transferred from Account #"
							+ sourceAccount.getAccountId() + " to Account #" + targetAccount.getAccountId());
					return;
				} else {

					// UNDO withdraw (simulate SQL ATOMICITY)
					as.deposit(withdraw, sourceAccount.getBalance());
					res.setStatus(401);
					res.getWriter().println("The requested action is not permitted");
					return;
				}

			}
			// fail in accessing DB
			else {
				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
				return;
			}

		}

	}

	public void updateAccount(HttpServletRequest req, HttpServletResponse res) throws IOException {

		if (rh.isLoggedOut(req, res)) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		String body = rh.readBody(req, res);
		Account account = om.readValue(body, Account.class);

		// GET AND CHECK CREDENTIAL OF CURRENT USER
		HttpSession ses = req.getSession(false);

		User u = (User) ses.getAttribute("user");
		if (!u.getRole().getRole().equals("Admin")) {

			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		Account origin = as.getAccountFromAccountID(account.getAccountId());
		account = as.updateAccount(account, origin);

		if (account == null) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		// return users
		String json = om.writeValueAsString(account);
		res.setStatus(200);
		res.getWriter().println(json);

	}

	public void findAccounts(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// Cannot Do Action Because User is not logged in
		HttpSession ses = req.getSession(false);
		if (rh.isLoggedOut(req, res)) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}
		// GET AND CHECK CREDENTIAL OF CURRENT USER
		User u = (User) ses.getAttribute("user");
		if (u.getRole().getRole().equals("Standard")) {

			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		Account[] accounts;

		// Find number of users
		int accountCount = as.getAccountCount();

		accounts = new Account[accountCount];

		accounts = as.getAccounts();
		// Populate users

		// return users
		String json = om.writeValueAsString(accounts);
		res.setStatus(200);
		res.getWriter().println(json);

	}

	public void findAccountsById(HttpServletRequest req, HttpServletResponse res, int id) throws IOException {
		// Cannot Do Action Because User is not logged in
		HttpSession ses = req.getSession(false);
		if (rh.isLoggedOut(req, res)) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		// GET AND CHECK CREDENTIAL OF CURRENT USER
		User u = (User) ses.getAttribute("user");
		Account account = as.getAccountsById(id);

		if (u.getRole().getRole().equals("Standard") && u.getUserId() != account.getUserId()) {

			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		// return user
		String json = om.writeValueAsString(account);
		res.setStatus(200);
		res.getWriter().println(json);

	}

	public void findAccountsByStatus(HttpServletRequest req, HttpServletResponse res, int id) throws IOException {
		// Cannot Do Action Because User is not logged in
		HttpSession ses = req.getSession(false);
		if (rh.isLoggedOut(req, res)) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		// GET AND CHECK CREDENTIAL OF CURRENT USER
		User u = (User) ses.getAttribute("user");

		if (u.getRole().getRole().equals("Standard")) {

			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		Account[] account = as.findAccountsByStatus(id);
		// return user
		String json = om.writeValueAsString(account);
		res.setStatus(200);
		res.getWriter().println(json);

	}

	public void findAccountsByUser(HttpServletRequest req, HttpServletResponse res, int id) throws IOException {
		// Cannot Do Action Because User is not logged in
		HttpSession ses = req.getSession(false);
		if (rh.isLoggedOut(req, res)) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		// GET AND CHECK CREDENTIAL OF CURRENT USER
		User u = (User) ses.getAttribute("user");

		if (u.getRole().getRole().equals("Standard") && u.getUserId() != id) {

			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		Account[] account = as.findAccountsByUser(id);
		// return user
		String json = om.writeValueAsString(account);
		res.setStatus(200);
		res.getWriter().println(json);

	}

}