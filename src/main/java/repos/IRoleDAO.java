package repos;

import java.util.List;

import models.Role;

public interface IRoleDAO {

	public List<Role> findAll();

	public Role findById(int id);

}
