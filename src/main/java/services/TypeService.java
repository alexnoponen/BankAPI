package services;

import java.util.List;

import models.AccountType;
import repos.ITypeDAO;
import repos.TypeDAO;

public class TypeService {

	private final ITypeDAO tdao = new TypeDAO();

	public List<AccountType> findAll() {
		return tdao.findAll();
	}

	public AccountType findById(int id) {
		return tdao.findById(id);
	}

}
