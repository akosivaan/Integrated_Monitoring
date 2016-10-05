package entity;

import utilities.DatabaseUtil;

public class Server {
	private int id;
	private String name;
	private String address;
	private String type;
	private User user;
	
	public Server() {
	
	}
	
	/**
	 * Description: Constructor of the Server that is not yet saved in the database
	 * @param name name of the server
	 * @param address address of the server
	 * @param type type of server
	 * @param user the user to be used in logging in
	 */
	public Server(String name, String address, String type, User user){
		this.name = name;
		this.address = address;
		this.type = type;
		this.user = user;
	}
	
	/**
	 * Description: Constructor of the Server that is saved in the database
	 * @param id id of the server
	 * @param name name of the server
	 * @param address address of the server
	 * @param type type of server
	 * @param user the user to be used in logging in
	 */
	public Server(int id,String name, String address, String type, User user){
		this.id = id;
		this.name = name;
		this.address = address;
		this.type = type;
		this.user = user;
	}
	
	/**
	 * Description: Setter of the server name
	 * @param name name of the server
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * Description: Setter of the server address
	 * @param address address of the server
	 */
	public void setAddress(String address){
		this.address = address;
	}
	
	/**
	 * Description: Setter of the server type
	 * @param type type of the server
	 */
	public void setType(String type){
		this.type = type;
	}
	
	/**
	 * Description: Setter of the user credentials
	 * @param user user of the server
	 */
	public void setUser(User user){
		this.user = user;
	}
	
	/**
	 * Description: Getter of server id
	 * @return id of the server
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * Description: Getter of the server name
	 * @return name of the server
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Description: Getter of server address
	 * @return address of the server
	 */
	public String getAddress(){
		return this.address;
	}
	
	/**
	 * Description: Getter of server type
	 * @return type of the server
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 * Description: Getter of server user
	 * @return user of the server
	 */
	public User getUser(){
		return this.user;
	}
	
	/**
	 * Update this server
	 */
	//Use this if there is already a record in the database and you only need to update the data.
	public void updateServerRecord(){
		DatabaseUtil.updateServer(this);
	}
	
	/**
	 * Insert this server
	 */
	//Use this if the server needs to be saved to the server, and if there is still no record of the server
	public void insertServerRecord(){
		DatabaseUtil.addServer(this);
	}
	
	/**
	 * Delete this server
	 */
	public void deleteServerRecord(){
		DatabaseUtil.deleteServer(this);
	}
}