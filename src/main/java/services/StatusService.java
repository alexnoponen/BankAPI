package services;

import java.util.List;

import models.AccountStatus;
import repos.IStatusDAO;
import repos.StatusDAO;

public class StatusService {
	private final IStatusDAO sdao = new StatusDAO();

	public List<AccountStatus> findAll() {
		return sdao.findAll();
	}

	public AccountStatus findById(int id) {
		return sdao.findById(id);
	}

}
