package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.Account;
import models.LoginDTO;
import models.User;
import services.AccountService;

public class AccountController {
	private static final AccountService as = new AccountService();
	private static final UserController uc = new UserController();
	private static final ObjectMapper om = new ObjectMapper();

	public List<Account> findAll() {
		return as.findAll();
	}

	public Account findById(int id) {
		return as.findById(id);
	}

	public boolean addAccount(Account a) {
		return as.addAccount(a);
	}

	public boolean updateAccount(Account a) {
		return as.updateAccount(a);

	}

	private boolean accountOwner(Account a, User u) {
		return as.accountOwner(a, u);
	}

	public List<Account> findByOwner(int userId) {
		return as.findByOwner(userId);
	}

	public Account findLast() {
		return as.findLast();
	}

	public void manageAccount(HttpServletRequest req, HttpServletResponse res, HttpSession ses, String[] portions)
			throws IOException {
		LoginDTO l = (LoginDTO) ses.getAttribute("user");
		User user = uc.findByUsername(l.username);

		if (portions.length == 3) {
			int id = Integer.parseInt(portions[2]);

			switch (portions[1]) {
			case "owner":
				if (user.getUserId() == id || user.getRole().getRoleId() == 3 || user.getRole().getRoleId() == 4) {
					List<Account> list = as.findByOwner(id);
					String json = om.writeValueAsString(list);
					res.setStatus(200);
					res.getWriter().println(json);
				} else {
					res.setStatus(401);
					res.getWriter().println("The requested action is not permitted");
				}
				break;

			case "status":
				if (user.getRole().getRoleId() == 3 || user.getRole().getRoleId() == 4) {
					List<Account> list = as.findByStatus(id);
					String alist = om.writeValueAsString(list);
					res.setStatus(200);
					res.getWriter().println(alist);
				} else {
					res.setStatus(401);
					res.getWriter().println("The requested action is not permitted");
				}
				break;
			}

		} else if (portions.length == 2) {
			int id = Integer.parseInt(portions[1]);
			Account a = as.findById(id);
			List<Account> alist = as.findByOwner(user.getUserId());
			boolean permitted = false;

			for (Account ac : alist) {
				if (ac.getAccountId() == (a.getAccountId())) {
					permitted = true;
				}
			}

			if (permitted || user.getRole().getRoleId() == 3 || user.getRole().getRoleId() == 4) {
				String json = om.writeValueAsString(a);
				res.setStatus(200);
				res.getWriter().println(json);
			} else {
				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
			}

		} else {

			if (req.getMethod().equals("POST")) {
				BufferedReader reader = req.getReader();

				StringBuilder s = new StringBuilder();

				String line = reader.readLine();

				while (line != null) {
					s.append(line);
					line = reader.readLine();
				}

				String body = new String(s);

				Account a = om.readValue(body, Account.class);

				if (addAccount(a)) {
					Account addeda = as.findLast();
					accountOwner(addeda, user);
					String json = om.writeValueAsString(addeda);
					res.setStatus(201);
					res.getWriter().println(json);
				} else {
					res.setStatus(400);
					res.getWriter().println("Invalid fields");
				}
			}

			if (req.getMethod().equals("GET")) {
				if (user.getRole().getRoleId() == 3 || user.getRole().getRoleId() == 4) {
					List<Account> all = as.findAll();
					String alla = om.writeValueAsString(all);
					res.setStatus(200);
					res.getWriter().println(alla);

				} else {
					res.setStatus(401);
					res.getWriter().println("The requested action is not permitted");
				}
			}

			if (req.getMethod().equals("PUT")) {
				if (user.getRole().getRoleId() == 4) {
					BufferedReader reader = req.getReader();

					StringBuilder s = new StringBuilder();

					String line = reader.readLine();

					while (line != null) {
						s.append(line);
						line = reader.readLine();
					}

					String body = new String(s);

					Account a = om.readValue(body, Account.class);

					if (updateAccount(a)) {
						Account updateda = as.findById(a.getAccountId());
						String json = om.writeValueAsString(updateda);
						res.setStatus(200);
						res.getWriter().println(json);
					} else {
						res.setStatus(400);
						res.getWriter().println("Invalid fields");
					}

				} else {
					res.setStatus(401);
					res.getWriter().println("The requested action is not permitted");
				}

			}
		}
	}

}
