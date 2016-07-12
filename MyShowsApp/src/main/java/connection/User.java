package connection;


public class User {
	private String username;
	private String password;
	private static User instance;
	
	public static User getInstance() {
        if (instance == null)
            instance = new User();
        return instance;
    }
	
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password +"]";
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
