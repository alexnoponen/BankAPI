package services;

import models.LoginDTO;
import models.User;
import repos.IUserDAO;
import repos.UserDAO;

public class LoginService {

	private final IUserDAO dao = new UserDAO();

	public boolean login(LoginDTO l) {

		return dao.getLoginCredentials(l).equals(l);

	}

	public User getUserFromLogin(LoginDTO l) {
		return dao.getUserFromLogin(l);
	}
}