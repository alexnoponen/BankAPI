package services;

import models.LoginDTO;
import models.User;

public class LoginService {
	private static final UserService us = new UserService();

	public boolean login(LoginDTO l) {
		User u = us.findByUsername(l.username);
		if (l.username.equals(u.getUsername()) && l.password.equals(u.getPassword())) {
			return true;
		}
		return false;

	}

}
