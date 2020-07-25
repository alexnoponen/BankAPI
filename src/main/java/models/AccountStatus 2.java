package models;

public class AccountStatus {
	
	private int statusId; // primary key
	private String status; // not null, unique

	public static AccountStatus createFromID(int status_id) {
		AccountStatus a = new AccountStatus();

		a.setStatusId(status_id);
		if (status_id == 1) {
			a.setStatus("Pending");
		} else if (status_id == 2) {
			a.setStatus("Open");
		} else if (status_id == 3) {
			a.setStatus("Closed");
		} else if (status_id == 4) {
			a.setStatus("Denied");
		} else {
			return null;
		}
		return a;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}