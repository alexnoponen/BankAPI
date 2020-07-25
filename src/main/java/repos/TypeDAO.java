package repos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.AccountType;
import utils.ConnectionUtil;

public class TypeDAO implements ITypeDAO {

	@Override
	public List<AccountType> findAll() {
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM account_type;";

			Statement statement = conn.createStatement();

			List<AccountType> list = new ArrayList<>();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				AccountType as = new AccountType();
				as.setTypeId(result.getInt("type_id"));
				as.setType(result.getString("account_type"));

				list.add(as);
			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public AccountType findById(int id) {
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM account_type WHERE type_id = " + id + ";";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			if (result.next()) {
				AccountType at = new AccountType();
				at.setTypeId(result.getInt("type_id"));
				at.setType(result.getString("account_type"));

				return at;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
