package entity;

import utilities.DatabaseUtil;

public class FileSystem {
	private int id;
	private int server_id;
	private String server_name;
	private String fs;
	
	public FileSystem() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Description: Constructor for Filesystem that was already saved in the database
	 * @param id ID of the filesystem generated in the database when saved
	 * @param server_id ID of the server where the filesystem is under
	 * @param server_name name of the server
	 * @param fs name of the filesystem
	 */
	public FileSystem(int id, int server_id, String server_name, String fs){
		this.id = id;
		this.server_id = server_id;
		this.server_name = server_name;
		this.fs = fs;
	}
	
	/**
	 * Description: Constructor for Filesystem that is not yet saved in the database
	 * @param server_id ID of the server where the filesystem is under
	 * @param server_name name of the server
	 * @param fs name of the filesystem
	 */
	public FileSystem(int server_id, String server_name, String fs){
		this.server_id = server_id;
		this.server_name = server_name;
		this.fs = fs;
	}
	
	/**
	 * Description: Setter of server id
	 * @param server_id ID of the server where the filesystem is under
	 */
	public void setServerId(int server_id){
		this.server_id = server_id;
	}
	
	/**
	 * Description: Setter of server name
	 * @param server_name name of the server
	 */
	public void setServerName(String server_name){
		this.server_name = server_name;
	}
	
	/**
	 * Description: Setter of filesystem name
	 * @param fs name of the filesystem
	 */
	public void setFileSystem(String fs){
		this.fs = fs;
	}
	
	/**
	 * Description: Getter of the filesystem id
	 * @return ID of the filesystem
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * Description: Getter of the server id
	 * @return ID of the server where the filesystem is under
	 */
	public int getServerId(){
		return this.server_id;
	}
	
	/**
	 * Description: Getter of the server name
	 * @return name of the server
	 */
	public String getServerName(){
		return this.server_name;
	}
	
	/**
	 * Description: Getter of the filesystem name
	 * @return name of the filesystem
	 */
	public String getFileSystem(){
		return this.fs;
	}
	
	/**
	 * Description: Update this filesystem in the database
	 */
	public void updateFS(){
		DatabaseUtil.updateFileSystem(this);
	}
	
	/**
	 * Description: Save this filesystem in the database
	 */
	public void insertFS(){
		DatabaseUtil.addFileSystem(this);
	}
	
	/**
	 * Description: Delete this filesystem in the database
	 */
	public void deleteFS(){
		DatabaseUtil.deleteFileSystem(this);
	}
}
