package entity;
/**
 * 
 * @author paul.ivann.e.granada
 * Description: This is the entity User where the username and password used to connect to the server is saved.
 * 
 */
public class User {
	
	private String username;
	private String password;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Description: Constructor with username and password
	 * @param username - username of the user
	 * @param password - password of the user
	 */
	public User(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Description: The setter of username data
	 * @param username - username to be saved
	 */
	public void setUsername(String username){
		this.username = username;
	}
	
	/**
	 * Description: The setter of the password data
	 * @param password - password to be saved
	 */
	public void setPassword(String password){
		this.password = password;
	}
	
	/**
	 * Description: The getter of the username
	 * @return - username of the user
	 */
	public String getUsername(){
		return this.username;
	}
	
	/**
	 * Description: The getter of the password
	 * @return - password of the user
	 */
	public String getPassword(){
		return this.password;
	}

}
