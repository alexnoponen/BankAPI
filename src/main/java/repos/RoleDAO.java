package repos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Role;
import utils.ConnectionUtil;

public class RoleDAO implements IRoleDAO {

	@Override
	public List<Role> findAll() {
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM roles;";

			Statement statement = conn.createStatement();

			List<Role> list = new ArrayList<>();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				Role r = new Role();
				r.setRoleId(result.getInt("role_id"));
				r.setRole(result.getString("user_role"));

				list.add(r);
			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Role findById(int id) {
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM roles WHERE role_id = " + id + ";";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			if (result.next()) {
				Role r = new Role();
				r.setRoleId(result.getInt("role_id"));
				r.setRole(result.getString("user_role"));

				return r;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
