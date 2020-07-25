package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.AccountController;
import controllers.LoginController;
import controllers.UserController;

public class MasterServlet extends HttpServlet {

	private static final ObjectMapper om = new ObjectMapper();
	private static final UserController uc = new UserController();
	private static final LoginController lc = new LoginController();
	private static final AccountController ac = new AccountController();

	public MasterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		System.out.println("IN DO GET");

		// System.out.println(req.getRequestURI());

		res.setContentType("application/json");
		// this will set the default response to not found; we will change it later if
		// the request was successful
		res.setStatus(404);

		final String URI = req.getRequestURI().replace("/rocp-project/", "");

		String[] portions = URI.split("/");

		// System.out.println(Arrays.toString(portions));

		try {
			switch (portions[0]) {

			case "users":
				if (portions.length == 1) {
					uc.findUsers(req, res);
				} else {
					uc.findUsersById(req, res, Integer.parseInt(portions[1]));
				}
				break;
			case "accounts":
				if (portions.length == 1) {
					ac.findAccounts(req, res);
				} else if (portions.length == 2) {
					ac.findAccountsById(req, res, Integer.parseInt(portions[1]));
				} else {
					if (portions[1].equals("status")) {
						ac.findAccountsByStatus(req, res, Integer.parseInt(portions[2]));
					}
					if (portions[1].equals("owner")) {
						ac.findAccountsByUser(req, res, Integer.parseInt(portions[2]));
					}
				}
				break;

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			res.getWriter().println("The id you provided is not an integer");
			res.setStatus(400);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		System.out.println("IN DO POST");
		// System.out.println(req.getRequestURI());

		res.setContentType("application/json");
		// this will set the default response to not found; we will change it later if
		// the request was successful
		res.setStatus(404);

		final String URI = req.getRequestURI().replace("/rocp-project/", "");

		String[] portions = URI.split("/");

		// System.out.println(Arrays.toString(portions));

		try {
			switch (portions[0]) {

			case "test":
				uc.test(req, res);
				break;
			case "login":
				;
				lc.login(req, res);
				break;
			case "logout":
				lc.logout(req, res);
				break;
			case "register":
				uc.register(req, res);
				break;
			case "accounts":
				if (portions.length == 1) {
					uc.submitAccount(req, res);
				}
				if (portions.length == 2) {
					switch (portions[1]) {

					case "withdraw":
						ac.withdraw(req, res);
						break;
					case "deposit":
						ac.deposit(req, res);
						break;
					case "transfer":
						ac.transfer(req, res);
						break;
					}
				}
				break;

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			res.getWriter().println("The id you provided is not an integer");
			res.setStatus(400);
		}

	}

	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		System.out.println("IN DO PUT");

		// System.out.println(req.getRequestURI());

		res.setContentType("application/json");
		// this will set the default response to not found; we will change it later if
		// the request was successful
		res.setStatus(404);

		final String URI = req.getRequestURI().replace("/rocp-project/", "");

		String[] portions = URI.split("/");

		// System.out.println(Arrays.toString(portions));

		try {
			switch (portions[0]) {

			case "users":
				uc.updateUser(req, res);
				break;
			case "accounts":
				ac.updateAccount(req, res);
				break;

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			res.getWriter().println("The id you provided is not an integer");
			res.setStatus(400);
		}
	}

}