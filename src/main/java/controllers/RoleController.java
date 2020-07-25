package controllers;

import java.util.List;

import models.Role;
import services.RoleService;

public class RoleController {
	private final RoleService rs = new RoleService();

	public List<Role> findAll() {
		return rs.findAll();
	}

	public Role findById(int id) {
		return rs.findById(id);
	}

}
