package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.LoginDTO;
import models.User;
import services.UserService;

public class UserController {
	private static final UserService us = new UserService();
	private static final ObjectMapper om = new ObjectMapper();

	public List<User> findAllUsers() {
		return us.findAllUsers();
	}

	public User findById(int id) {
		return us.findById(id);
	}

	public User findByUsername(String username) {
		return us.findByUsername(username);
	}

	private boolean updateUser(User u) {
		return us.updateUser(u);
	}

	public void addUser(HttpServletRequest req, HttpServletResponse res, HttpSession ses) throws IOException {
		LoginDTO l = (LoginDTO) ses.getAttribute("user");
		User user = findByUsername(l.username);

		if (user.getRole().getRoleId() == 4) {

			if (req.getMethod().equals("POST")) {
				BufferedReader reader = req.getReader();

				StringBuilder s = new StringBuilder();

				String line = reader.readLine();

				while (line != null) {
					s.append(line);
					line = reader.readLine();
				}

				String body = new String(s);

				User u = om.readValue(body, User.class);

				if (us.addUser(u)) {
					User addedu = findByUsername(u.getUsername());
					String json = om.writeValueAsString(addedu);
					res.setStatus(201);
					res.getWriter().println(json);
				} else {
					res.setStatus(400);
					res.getWriter().println("Invalid fields");
				}
			}
		} else {
			res.setStatus(401);
			res.getWriter().println("The requested action is not permitted");
		}
	}

	public void manageUser(HttpServletRequest req, HttpServletResponse res, HttpSession ses, String[] portions)
			throws IOException {
		LoginDTO l = (LoginDTO) ses.getAttribute("user");
		User user = findByUsername(l.username);

		if (portions.length == 2) {
			int id = Integer.parseInt(portions[1]);

			if (user.getUserId() == id || user.getRole().getRoleId() == 3 || user.getRole().getRoleId() == 4) {
				User u = findById(id);
				String json = om.writeValueAsString(u);
				res.setStatus(200);
				res.getWriter().println(json);
			} else {
				res.setStatus(401);
				res.getWriter().println("The requested action is not permitted");
			}
		} else {

			if (req.getMethod().equals("PUT")) {
				BufferedReader reader = req.getReader();
				StringBuilder s = new StringBuilder();
				String line = reader.readLine();

				while (line != null) {
					s.append(line);
					line = reader.readLine();
				}

				String body = new String(s);
				User u = om.readValue(body, User.class);

				System.out.println(u);
				System.out.println(user);

				if (user.getRole().getRoleId() == 4) {

					if (updateUser(u)) {
						User updatedu = findById(u.getUserId());
						String json = om.writeValueAsString(updatedu);
						res.setStatus(200);
						res.getWriter().println(json);
					} else {
						res.setStatus(400);
						res.getWriter().println("Invalid fields");
					}

				} else if (user.getUserId() == u.getUserId()) {
					u.setRole(user.getRole());

					if (us.updateUser(u)) {
						User updatedu = findById(u.getUserId());
						String json = om.writeValueAsString(updatedu);
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
			} else {

				if (user.getRole().getRoleId() == 3 || user.getRole().getRoleId() == 4) {
					List<User> all = findAllUsers();
					String allu = om.writeValueAsString(all);
					res.setStatus(200);
					res.getWriter().println(allu);
				} else {
					res.setStatus(401);
					res.getWriter().println("The requested action is not permitted");
				}
			}
		}

	}

}
