package controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.Account;
import models.Role;
import models.User;
import services.AccountService;
import services.UserService;

public class UserController {
	
	private static final UserService us = new UserService();
	private static final AccountService as = new AccountService();
	private static final ObjectMapper om = new ObjectMapper();
	private static final RequestHelper rh = new RequestHelper();

	public void register(HttpServletRequest req, HttpServletResponse res) throws IOException {

		// Cannot Do Action Because User is not logged in
		HttpSession ses = req.getSession(false);
		if (rh.isLoggedOut(req, res)) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");

			return;
		}

		else {

			// GET AND CHECK CREDENTIAL OF CURRENT USER
			User u = (User) ses.getAttribute("user");
			if (!u.getRole().getRole().equals("Admin")) {

				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
				return;
			} else {

				// Reading Body of Request into string
				String body = rh.readBody(req, res);

				// READ OBJECT FROM BODY
				User other = om.readValue(body, User.class);

				// REGISTER METHOD
				if (us.register(other)) {

					// SUCCESSFUL OBJECT CREATION
					String json = om.writeValueAsString(other);
					res.setStatus(201);
					res.getWriter().println(json);
				} else {
					res.setStatus(400);
					res.getWriter().println("Invalid fields");
				}

			}
		}
	}

	public void submitAccount(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// Cannot Do Action Because User is not logged in
		HttpSession ses = req.getSession(false);
		if (rh.isLoggedOut(req, res)) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		} else {

			// Reading Body of Request into string
			String body = rh.readBody(req, res);

			// CREATE OBJECT FROM SESSION
			User u = (User) ses.getAttribute("user");

			// READ OBJECT FROM BODY
			Account a = om.readValue(body, Account.class);

			if (u.getRole().getRole().equals("Standard") && a.getUserId() != u.getUserId()) {

				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
				return;
			}

			if (as.submitAccount(a)) {

				// WRITE OBJECT TO BODY
				String json = om.writeValueAsString(a);
				res.setStatus(201);
				res.getWriter().println(json);

			} else {
				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
			}
		}

	}

	public void updateUser(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// Cannot Do Action Because User is not logged in
		HttpSession ses = req.getSession(false);
		if (rh.isLoggedOut(req, res)) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		// Reading Body of Request into string
		String body = rh.readBody(req, res);
		// READ OBJECT FROM BODY
		User other = om.readValue(body, User.class);

		// GET AND CHECK CREDENTIAL OF CURRENT USER
		User u = (User) ses.getAttribute("user");
		if (!u.getRole().getRole().equals("Admin") && u.getUserId() != other.getUserId()) {

			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		User origin = us.getUsersById(other.getUserId());
		other = us.updateUser(other, origin);

		if (other == null) {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
			return;
		}

		// return users
		String json = om.writeValueAsString(other);
		res.setStatus(200);
		res.getWriter().println(json);

	}

	public void findUsers(HttpServletRequest req, HttpServletResponse res) throws IOException {
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

		User[] users;

		// Find number of users
		int userCount = us.getUserCount();

		users = new User[userCount];

		users = us.getUsers();
		// Populate users

		// return users
		String json = om.writeValueAsString(users);
		res.setStatus(200);
		res.getWriter().println(json);

	}

	public void findUsersById(HttpServletRequest req, HttpServletResponse res, int id) throws IOException {
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

		User user = us.getUsersById(id);

		// return user
		String json = om.writeValueAsString(user);
		res.setStatus(200);
		res.getWriter().println(json);

	}

	public void test(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// TODO Auto-generated method stub

		Role role = new Role();

		role.setRoleId(0);
		role.setRole("jello");

		// WRITE OBJECT TO BODY
		String json = om.writeValueAsString(role);

		res.setStatus(200);
		res.getWriter().println(json);
		return;

	}

}