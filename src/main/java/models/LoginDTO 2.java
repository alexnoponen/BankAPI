package models;

public class LoginDTO {
	
	public String username;
	public String password;

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LoginDTO)) {
			return false;
		}
		LoginDTO other = (LoginDTO) o;

		if (this.username.equals(other.username) && this.password.equals(other.password)) {
			return true;
		} else {
			return false;
		}
	}

}
