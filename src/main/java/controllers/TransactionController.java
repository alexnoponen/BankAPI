package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.Account;
import models.AddOwnerDTO;
import models.DepositWithdrawDTO;
import models.LoginDTO;
import models.TransferDTO;
import models.User;
import services.AddOwnerService;
import services.DepositWithdrawService;
import services.TransferService;

public class TransactionController {
	private static final UserController uc = new UserController();
	private static final AccountController ac = new AccountController();
	private static final DepositWithdrawService dws = new DepositWithdrawService();
	private static final TransferService ts = new TransferService();
	private static final ObjectMapper om = new ObjectMapper();
	private static final AddOwnerService aos = new AddOwnerService();

	public void manageTransaction(HttpServletRequest req, HttpServletResponse res, HttpSession ses, String[] portions)
			throws IOException {
		LoginDTO l = (LoginDTO) ses.getAttribute("user");
		User user = uc.findByUsername(l.username);

		BufferedReader reader = req.getReader();
		StringBuilder s = new StringBuilder();
		String line = reader.readLine();

		while (line != null) {
			s.append(line);
			line = reader.readLine();
		}

		String body = new String(s);

		switch (portions[1]) {

		case "deposit":
			DepositWithdrawDTO d = om.readValue(body, DepositWithdrawDTO.class);

			Account a_d = ac.findById(d.accountId);
			List<Account> list_d = ac.findByOwner(user.getUserId());
			boolean permitted_d = false;

			for (Account ac : list_d) {
				if (ac.getAccountId() == (a_d.getAccountId())) {
					permitted_d = true;
				}
			}

			if (permitted_d || user.getRole().getRoleId() == 4) {

				if (dws.deposit(d)) {
					res.setStatus(200);
					res.getWriter().println("$" + d.amount + " has been deposited to Account #" + d.accountId);
				} else {
					res.setStatus(400);
					res.getWriter().println("Invalid deposit");
				}

			} else {
				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
			}
			break;

		case "withdraw":
			DepositWithdrawDTO w = om.readValue(body, DepositWithdrawDTO.class);

			Account a_w = ac.findById(w.accountId);
			List<Account> list_w = ac.findByOwner(user.getUserId());
			boolean permitted_w = false;

			for (Account ac : list_w) {
				if (ac.getAccountId() == (a_w.getAccountId())) {
					permitted_w = true;
				}
			}

			if (permitted_w || user.getRole().getRoleId() == 4) {

				if (dws.withdraw(w)) {
					res.setStatus(200);
					res.getWriter().println("$" + w.amount + " has been withdrawn from Account #" + w.accountId);
				} else {
					res.setStatus(400);
					res.getWriter().println("Invalid withdraw");
				}

			} else {
				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
			}
			break;

		case "transfer":
			TransferDTO t = om.readValue(body, TransferDTO.class);

			Account a_t = ac.findById(t.sourceAccountId);
			List<Account> list_t = ac.findByOwner(user.getUserId());
			boolean permitted_t = false;

			for (Account ac : list_t) {
				if (ac.getAccountId() == (a_t.getAccountId())) {
					permitted_t = true;
				}
			}

			if (permitted_t || user.getRole().getRoleId() == 4) {

				if (ts.transfer(t)) {
					res.setStatus(200);
					res.getWriter().println("$" + t.amount + " has been transferred from Account #" + t.sourceAccountId
							+ " to Account #" + t.targetAccountId);
				} else {
					res.setStatus(400);
					res.getWriter().println("Invalid transfer");
				}

			} else {
				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
			}
			break;

		case "addowner":
			AddOwnerDTO ao = om.readValue(body, AddOwnerDTO.class);

			Account a_o = ac.findById(ao.accountId);
			List<Account> list_o = ac.findByOwner(user.getUserId());
			boolean permitted_o = false;

			for (Account ac : list_o) {
				if (ac.getAccountId() == (a_o.getAccountId())) {
					permitted_o = true;
				}
			}

			if ((permitted_o && user.getRole().getRoleId() == 2) || (user.getRole().getRoleId() == 4)) {

				if (aos.AddOwner(ao)) {
					res.setStatus(200);
					res.getWriter().println("Owner #" + ao.userId + " has been added to Account #" + ao.accountId);
				} else {
					res.setStatus(400);
					res.getWriter().println("Failed to add owner");
				}

			} else {
				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
			}

			break;

		}
	}

}
