package models;

public class Role {

	private int roleId; // primary key
	private String role; // not null, unique

	public static Role createFromId(int roleId) {

		Role r = new Role();
		r.setRoleId(roleId);
		if (roleId == 1) {
			r.setRole("Admin");
		} else if (roleId == 2) {
			r.setRole("Employee");
		} else if (roleId == 3) {
			r.setRole("Standard");
		} else if (roleId == 4) {
			r.setRole("Premium");
		} else {
			r = null;
		}

		return r;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}