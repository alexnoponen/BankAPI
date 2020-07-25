package repos;

import java.util.List;

import models.User;

public interface IUserDAO {
	
	public List<User> findAllUsers();

	public User findById(int id);

	public User findByUsername(String username);

	public boolean addUser(User u);

	public boolean updateUser(User u);

}
