package repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Role;
import models.User;
import utils.ConnectionUtil;

public class UserDAO implements IUserDAO {

	private static final IRoleDAO rdao = new RoleDAO();

	@Override
	public List<User> findAllUsers() {

		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM users;";

			Statement statement = conn.createStatement();

			List<User> list = new ArrayList<>();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				User u = new User();
				u.setUserId(result.getInt("user_id"));
				u.setUsername(result.getString("username"));
				u.setPassword(result.getString("user_password"));
				u.setFirstName(result.getString("first_name"));
				u.setLastName(result.getString("last_name"));
				u.setEmail(result.getString("email"));
				Role r = rdao.findById(result.getInt("user_role"));
				u.setRole(r);

				list.add(u);

			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public User findById(int id) {
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM users WHERE user_id = " + id + ";";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			if (result.next()) {
				User u = new User();
				u.setUserId(result.getInt("user_id"));
				u.setUsername(result.getString("username"));
				u.setPassword(result.getString("user_password"));
				u.setFirstName(result.getString("first_name"));
				u.setLastName(result.getString("last_name"));
				u.setEmail(result.getString("email"));
				Role r = rdao.findById(result.getInt("user_role"));
				u.setRole(r);

				return u;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public User findByUsername(String username) {
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM users WHERE username = '" + username + "';";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			if (result.next()) {
				User u = new User();
				u.setUserId(result.getInt("user_id"));
				u.setUsername(result.getString("username"));
				u.setPassword(result.getString("user_password"));
				u.setFirstName(result.getString("first_name"));
				u.setLastName(result.getString("last_name"));
				u.setEmail(result.getString("email"));
				Role r = rdao.findById(result.getInt("user_role"));
				u.setRole(r);

				return u;

			}

		} catch (

		SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean addUser(User u) {
		try (Connection conn = ConnectionUtil.getConnection()) {

			int index = 0;

			String sql = "INSERT INTO users(username, user_password, first_name, last_name, email, user_role)"
					+ " VALUES(?,?,?,?,?,?);";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(++index, u.getUsername());
			statement.setString(++index, u.getPassword());
			statement.setString(++index, u.getFirstName());
			statement.setString(++index, u.getLastName());
			statement.setString(++index, u.getEmail());
			statement.setInt(++index, u.getRole().getRoleId());

			statement.execute();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean updateUser(User u) {

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "UPDATE users SET username = ?, user_password = ?, "
					+ "first_name = ?, last_name = ?, email = ?, user_role = ? WHERE user_id = ?;";

			PreparedStatement statement = conn.prepareStatement(sql);
			int index = 0;

			statement.setString(++index, u.getUsername());
			statement.setString(++index, u.getPassword());
			statement.setString(++index, u.getFirstName());
			statement.setString(++index, u.getLastName());
			statement.setString(++index, u.getEmail());
			statement.setInt(++index, u.getRole().getRoleId());
			statement.setInt(++index, u.getUserId());

			statement.execute();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

}
