package repos;

import java.util.List;

import models.AccountStatus;

public interface IStatusDAO {
	
	public List<AccountStatus> findAll();

	public AccountStatus findById(int id);

}
