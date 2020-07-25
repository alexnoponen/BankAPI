package controllers;

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
	private static final RequestHelper rh = new RequestHelper();

	public void login(HttpServletRequest req, HttpServletResponse res) throws IOException {

		// Reading Body of Request into string
		String body = rh.readBody(req, res);
		LoginDTO l = om.readValue(body, LoginDTO.class);

		if (ls.login(l)) {
			HttpSession ses = req.getSession();
			User u = ls.getUserFromLogin(l);

			ses.setAttribute("user", u);
			ses.setAttribute("loggedin", true);

			String json = om.writeValueAsString(u);

			res.getWriter().println(json);
			res.setStatus(200);

		} else {
			HttpSession ses = req.getSession(false);
			if (ses != null) {
				ses.invalidate();
			}
			res.setStatus(401);
			res.getWriter().println("Invalid Credentials");
		}
	}

	public void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
		HttpSession ses = req.getSession(false);

		if (ses != null) {
			User l = (User) ses.getAttribute("user");
			ses.invalidate();
			res.setStatus(200);
			res.getWriter().println("You have successfully logged out " + l.getUsername());
		} else {
			res.setStatus(400);
			res.getWriter().println("There was no user logged into the session");
		}

	}

}