package services;

import java.util.List;

import models.User;
import repos.IUserDAO;
import repos.UserDAO;

public class UserService {

	private final IUserDAO udao = new UserDAO();

	public List<User> findAllUsers() {
		return udao.findAllUsers();
	}

	public User findById(int id) {
		return udao.findById(id);
	}

	public User findByUsername(String username) {
		return udao.findByUsername(username);
	}

	public boolean addUser(User u) {
		List<User> list = findAllUsers();

		for (User us : list) {
			if (us.getUsername().equals(u.getUsername()) && us.getFirstName().equals(u.getFirstName())
					&& us.getLastName().equals(u.getLastName()) && us.getEmail().equals(u.getEmail())) {
				return false;
			}
		}

		return udao.addUser(u);
	}

	public boolean updateUser(User u) {
		List<User> list = findAllUsers();

		for (User us : list) {
			if (us.getUsername().equals(u.getUsername()) && us.getPassword().equals(u.getPassword())
					&& us.getFirstName().equals(u.getFirstName()) && us.getLastName().equals(u.getLastName())
					&& us.getEmail().equals(u.getEmail()) && us.getRole().equals(u.getRole())) {
				return false;
			}
		}

		return udao.updateUser(u);
	}

}
