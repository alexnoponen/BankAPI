package repos;

import models.LoginDTO;
import models.User;

public interface IUserDAO {
	
	public LoginDTO getLoginCredentials(LoginDTO l);

	public User getUserFromLogin(LoginDTO l);

	public boolean insert(User other);

	public int getUserCount();

	public User[] getUsers();

	public User getUsersById(int id);

	public User updateUser(User other, User origin);

}
