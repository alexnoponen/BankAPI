package repos;

import java.util.List;

import models.AccountType;

public interface ITypeDAO {
	
	public List<AccountType> findAll();

	public AccountType findById(int id);

}
