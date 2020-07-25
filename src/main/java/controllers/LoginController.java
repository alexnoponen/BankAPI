package controllers;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.LoginDTO;
import models.User;
import services.LoginService;

public class LoginController {
	private static final LoginService ls = new LoginService();
	private static final ObjectMapper om = new ObjectMapper();
	private static final UserController uc = new UserController();

	public void login(HttpServletRequest req, HttpServletResponse res) throws IOException {

		if (req.getMethod().equals("POST")) {
			BufferedReader reader = req.getReader();

			StringBuilder s = new StringBuilder();

			String line = reader.readLine();

			while (line != null) {
				s.append(line);
				line = reader.readLine();
			}

			String body = new String(s);

			LoginDTO l = om.readValue(body, LoginDTO.class);

			if (ls.login(l)) {
				HttpSession ses = req.getSession();
				ses.setAttribute("user", l);
				ses.setAttribute("loggedin", true);

				User u = uc.findByUsername(l.username);
				String json = om.writeValueAsString(u);

				res.setStatus(200);
				res.getWriter().println(json);

			} else {
				HttpSession ses = req.getSession(false);
				if (ses != null) {
					ses.invalidate();
				}
				res.setStatus(400);
				res.getWriter().println("Invalid Credentials");
			}
		}
	}

	public void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession ses = req.getSession(false);

		if (ses != null) {
			LoginDTO l = (LoginDTO) ses.getAttribute("user");
			ses.invalidate();
			res.setStatus(200);
			res.getWriter().println("You have successfully logged out " + l.username);

		} else {
			res.setStatus(400);
			res.getWriter().println("There was no user logged into the session");

		}
	}

}
