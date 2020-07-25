package services;

import java.util.List;

import models.Role;
import repos.IRoleDAO;
import repos.RoleDAO;

public class RoleService {
	private final IRoleDAO rdao = new RoleDAO();

	public List<Role> findAll() {
		return rdao.findAll();
	}

	public Role findById(int id) {
		return rdao.findById(id);
	}

}
