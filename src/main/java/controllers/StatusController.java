package controllers;

import java.util.List;

import models.AccountStatus;
import services.StatusService;

public class StatusController {

	private final StatusService ss = new StatusService();

	public List<AccountStatus> findAll() {
		return ss.findAll();
	}

	public AccountStatus findById(int id) {
		return ss.findById(id);
	}

}
