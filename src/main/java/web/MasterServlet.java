package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controllers.AccountController;
import controllers.LoginController;
import controllers.TransactionController;
import controllers.UserController;

public class MasterServlet {
	private static final LoginController lc = new LoginController();
	private static final UserController uc = new UserController();
	private static final AccountController ac = new AccountController();
	private static final TransactionController tc = new TransactionController();

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("application/json");
		res.setStatus(404);

		final String URI = req.getRequestURI().replace("/rocp-project/", "");

		String[] portions = URI.split("/");

		try {
			switch (portions[0]) {
			case "login":
				lc.login(req, res);
				break;

			case "logout":
				lc.logout(req, res);
				break;

			case "register":
				HttpSession ses = req.getSession(false);
				if (ses != null && ((Boolean) ses.getAttribute("loggedin"))) {
					uc.addUser(req, res, ses);
				} else {
					res.setStatus(400);
					res.getWriter().println("There was no user logged into the session");
				}
				break;

			case "users":
				ses = req.getSession(false);
				if (ses != null && ((Boolean) ses.getAttribute("loggedin"))) {
					uc.manageUser(req, res, ses, portions);
				} else {
					res.setStatus(400);
					res.getWriter().println("There was no user logged into the session");
				}
				break;

			case "accounts":
				ses = req.getSession(false);
				if (ses != null && ((Boolean) ses.getAttribute("loggedin"))) {

					if (req.getMethod().equals("POST") && portions.length == 2) {
						tc.manageTransaction(req, res, ses, portions);
					} else {
						ac.manageAccount(req, res, ses, portions);
					}
				} else {
					res.setStatus(400);
					res.getWriter().println("There was no user logged into the session");
				}
				break;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			res.setStatus(400);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}


	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}

}
