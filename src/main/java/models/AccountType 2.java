package models;

public class AccountType {

	private int typeId; // primary key
	private String type; // not null, unique

	public static AccountType createTypeFromId(int type_id) {

		AccountType t = new AccountType();
		t.setTypeId(type_id);

		if (type_id == 1) {
			t.setType("Checking");
		} else if (type_id == 2) {
			t.setType("Savings");
		} else {
			t = null;
		}

		return t;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}