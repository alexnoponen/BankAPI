package repos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.AccountStatus;
import utils.ConnectionUtil;

public class StatusDAO implements IStatusDAO {

	@Override
	public List<AccountStatus> findAll() {
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM account_status;";

			Statement statement = conn.createStatement();

			List<AccountStatus> list = new ArrayList<>();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				AccountStatus as = new AccountStatus();
				as.setStatusId(result.getInt("status_id"));
				as.setStatus(result.getString("account_status"));

				list.add(as);
			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public AccountStatus findById(int id) {
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM account_status WHERE status_id = " + id + ";";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			if (result.next()) {
				AccountStatus as = new AccountStatus();
				as.setStatusId(result.getInt("status_id"));
				as.setStatus(result.getString("account_status"));

				return as;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
