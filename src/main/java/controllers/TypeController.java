package controllers;

import java.util.List;

import models.AccountType;
import services.TypeService;

public class TypeController {

	private final TypeService ts = new TypeService();

	public List<AccountType> findAll() {
		return ts.findAll();
	}

	public AccountType findById(int id) {
		return ts.findById(id);
	}

}
