package utilities;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import entity.FSRecord;
import entity.FileSystem;
import entity.Server;
import entity.User;

public class DatabaseUtil {
	private final static String LOCATION = "jdbc:hsqldb:file:database/server_monitoring;shutdown=true";
	private final static String USERNAME = "SA";
	private final static String PASSWORD = "";
	private final static String DRIVER = "org.hsqldb.jdbcDriver";
	
	public DatabaseUtil() {
	}
	
	public static void updateServerUser(String username, String pwd, int id){
		Connection conn = connectToDatabaseServer();
		String sql = "UPDATE SERVER SET PASSWORD = ?, USERNAME = ? WHERE ID = ?";
		PreparedStatement pmst;
		try {
			pmst = conn.prepareStatement(sql);
			pmst.setString(1, SecurityUtil.encrypt(pwd));
			pmst.setString(2, username);
			pmst.setInt(3, id);
			pmst.executeUpdate();
			
			System.out.println("User updated for all servers!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateUser(String username, String pwd){
		Connection conn = connectToDatabaseServer();
		String sql = "UPDATE SERVER SET PASSWORD = ? WHERE USERNAME = ?";
		PreparedStatement pmst;
		try {
			pmst = conn.prepareStatement(sql);
			pmst.setString(1, SecurityUtil.encrypt(pwd));
			pmst.setString(2, username);
			pmst.executeUpdate();
			
			System.out.println("User updated for all servers!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int findServer(int id){
		Connection conn = connectToDatabaseServer();
		String sql = "SELECT * FROM SERVER WHERE ID = ?";
		PreparedStatement pmst;
		try {
			pmst = conn.prepareStatement(sql);
			pmst.setInt(1, id);
			ResultSet rs = pmst.executeQuery();
			if(rs.next()){
				return 1;
			}
			else{
				return -1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public static int findUser(String username, String pwd){
		Connection conn = connectToDatabaseServer();
		String sql = "SELECT * FROM SERVER WHERE USERNAME = ? AND PASSWORD = ?";
		PreparedStatement pmst;
		try {
			pmst = conn.prepareStatement(sql);
			pmst.setString(1, username);
			pmst.setString(2, SecurityUtil.encrypt(pwd));
			ResultSet rs = pmst.executeQuery();
			if(rs.next()){
				return 1;
			}
			else{
				return -1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Description: Add the result of the monitoring of the filesystem in the database
	 * @param fsd - the filesystem record to be saved
	 */
	public static void addFSData(FSRecord fsd){
		Connection conn = connectToDatabaseServer();
		String sql = "INSERT INTO FS_DATA(FS_ID,FILESYSTEM,FREE_SPACE,TOTAL_SPACE,PERC_USED,DIFF_FROM_LAST,DATE_CHECKED) VALUES(?,?,?,?,?,?,?)";
		PreparedStatement pmst;
		try {
			pmst = conn.prepareStatement(sql);
			pmst.setInt(1, fsd.getFs_id());
			pmst.setString(2, fsd.getFilesystem());
			pmst.setBigDecimal(3, fsd.getFree_space());
			pmst.setBigDecimal(4, fsd.getTotal_space());
			pmst.setBigDecimal(5, fsd.getPerc_used());
			pmst.setBigDecimal(6, fsd.getDiff_from_last());
			pmst.setDate(7, new Date(fsd.getDate_checked().getTime()));
			pmst.executeUpdate();
			
			conn.commit();
			pmst.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Description: Get the second to the last record of the result of the monitored filesystem
	 * @param fsid - id of the filesystem
	 * @return - the second to the last filesystem
	 */
	public static FSRecord getSecondToLast(int fsid){
		Connection conn = connectToDatabaseServer();
		FSRecord f = null;
		String sql = "SELECT * FROM FS_DATA WHERE FS_ID = ? ORDER BY DATE_CHECKED DESC LIMIT 2";
		try {
			PreparedStatement pmst = conn.prepareStatement(sql);
			pmst.setInt(1, fsid);
			ResultSet rs = pmst.executeQuery();
			int counter = 0;
			while(rs.next()){
				int id = rs.getInt("ID");
				int fs_id = fsid;
				String fs = rs.getString("FILESYSTEM");
				BigDecimal f_s = rs.getBigDecimal("FREE_SPACE");
				BigDecimal t_s = rs.getBigDecimal("TOTAL_SPACE");
				BigDecimal p_u = rs.getBigDecimal("PERC_USED");
				BigDecimal d_f_l = rs.getBigDecimal("DIFF_FROM_LAST");
				Timestamp d_c = rs.getTimestamp("DATE_CHECKED");
				f = new FSRecord(id,fs_id, fs, f_s, t_s ,p_u, d_f_l, d_c);
				counter++;
			}
			if(counter < 2){
				f = null;
			}
			return f;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return f;
		}
	}
	
	/**
	 * Description: Get the last record of the result of the monitored filesystem
	 * @param fsid - id of the filesystem
	 * @return - the last record of the filesystem
	 */
	public static FSRecord getFSLastRecord(int fsid){
		Connection conn = connectToDatabaseServer();
		FSRecord f = null;
		String sql = "SELECT * FROM FS_DATA WHERE FS_ID = ? ORDER BY DATE_CHECKED DESC LIMIT 1";
		try {
			PreparedStatement pmst = conn.prepareStatement(sql);
			pmst.setInt(1, fsid);
			ResultSet rs = pmst.executeQuery();
			while(rs.next()){
				int id = rs.getInt("ID");
				int fs_id = fsid;
				String fs = rs.getString("FILESYSTEM");
				BigDecimal f_s = rs.getBigDecimal("FREE_SPACE");
				BigDecimal t_s = rs.getBigDecimal("TOTAL_SPACE");
				BigDecimal p_u = rs.getBigDecimal("PERC_USED");
				BigDecimal d_f_l = rs.getBigDecimal("DIFF_FROM_LAST");
				Timestamp d_c = rs.getTimestamp("DATE_CHECKED");
				f = new FSRecord(id,fs_id, fs, f_s, t_s ,p_u, d_f_l, d_c);
				return f;
			}
			return f;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return f;
		}
	}
	
	/**
	 * Description: Delete the server and the filesystem under it
	 * @param server - the server to be deleted
	 */
	public static void deleteServer(Server server){
		Connection conn = connectToDatabaseServer();
		String sql2 = "DELETE FROM SERVER WHERE ID = ?";
		String sql = "DELETE FROM FS_SERVER WHERE SERVER_ID = ?";
		PreparedStatement pmst;
		PreparedStatement pmst2;
		try {
			pmst = conn.prepareStatement(sql);
			pmst.setInt(1, server.getId());
			pmst.executeUpdate();
			pmst.close();
			
			
			pmst2 = conn.prepareStatement(sql2);
			pmst2.setInt(1,server.getId());
			pmst2.executeUpdate();
			conn.commit();
			pmst2.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Description: Delete the filesystem
	 * @param fs - the filesystem to be deleted
	 */
	public static void deleteFileSystem(FileSystem fs){
		Connection conn = connectToDatabaseServer();
		String sql = "DELETE FROM FS_SERVER WHERE ID = ?";
		PreparedStatement pmst;
		try {
			pmst = conn.prepareStatement(sql);
			pmst.setInt(1, fs.getId());
			pmst.executeUpdate();
			conn.commit();
			pmst.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Description: Update the server
	 * @param server - the server to be updated
	 */
	public static void updateServer(Server server){
		Connection conn = connectToDatabaseServer();
		String sql = "UPDATE SERVER SET ADDRESS = ?, TYPE = ?, USERNAME = ?, PASSWORD = ?, NAME = ? WHERE ID = ?";
		String sql2 = "UPDATE FS_SERVER SET NAME = ? WHERE SERVER_ID = ?";
		try {
			
			PreparedStatement pmst2 = conn.prepareStatement(sql2);
			pmst2.setString(1, server.getName());
			pmst2.setInt(2, server.getId());
			conn.commit();
			pmst2.close();
			
			PreparedStatement pmst = conn.prepareStatement(sql);
			pmst.setString(1, server.getAddress());
			pmst.setString(2, server.getType());
			pmst.setString(3, server.getUser().getUsername());
			pmst.setString(4, SecurityUtil.encrypt(server.getUser().getPassword()));
			pmst.setString(5, server.getName());
			pmst.setInt(6, server.getId());
			pmst.executeUpdate();
			conn.commit();
			pmst.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Description: Update the filesystem
	 * @param fs - the filesystem to be updated
	 */
	public static void updateFileSystem(FileSystem fs){
		Connection conn = connectToDatabaseServer();
		String sql = "UPDATE FS_SERVER SET FILESYSTEM = ? WHERE ID = ?";
		try {
			PreparedStatement pmst = conn.prepareStatement(sql);
			pmst.setString(1, fs.getFileSystem());
			pmst.setInt(2, fs.getId());
			pmst.executeUpdate();
			conn.commit();
			pmst.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Description: Add the server to the database
	 * @param server - the server to be inserted in the database
	 */
	//Add a server to the database
	public static void addServer(Server server){
		Connection conn = connectToDatabaseServer();
		String sql = "INSERT INTO SERVER(NAME,ADDRESS,TYPE,USERNAME,PASSWORD) VALUES(?,?,?,?,?)";
		try {
			PreparedStatement pmst = conn.prepareStatement(sql);
			pmst.setString(1, server.getName());
			pmst.setString(2, server.getAddress());
			pmst.setString(3, server.getType());
			pmst.setString(4, server.getUser().getUsername());
			pmst.setString(5, SecurityUtil.encrypt(server.getUser().getPassword()));
			int rcnt = pmst.executeUpdate();
			
			System.out.println(rcnt + " row(s) affected");
			
			conn.commit();
			pmst.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Description: Add the filesystem in the database
	 * @param fs - the filesystem to be inserted
	 */
	//Add a file system to the database
	public static void addFileSystem(FileSystem fs){
		Connection conn = connectToDatabaseServer();
		String sql = "INSERT INTO FS_SERVER(SERVER_ID,NAME,FILESYSTEM) VALUES(?,?,?)";
		PreparedStatement pmst;
		try {
			pmst = conn.prepareStatement(sql);
			pmst.setInt(1, fs.getServerId());
			pmst.setString(2, fs.getServerName());
			pmst.setString(3,fs.getFileSystem());
			int rcnt = pmst.executeUpdate();
			
			System.out.println(rcnt + "row(s) affected");
			
			conn.commit();
			pmst.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Description: Get the filesystem base on the id 
	 * @param id - id of the filesystem to be retrieved
	 * @return - the retrieved filesystem
	 */
	public static FileSystem getFileSystem(int id){
		FileSystem fs = null;
		Connection conn = connectToDatabaseServer();
		String sql = "SELECT * FROM FS_SERVER WHERE ID = ?";
		PreparedStatement pmst;
		try {
			pmst = conn.prepareStatement(sql);
			pmst.setInt(1, id);
			ResultSet rs = pmst.executeQuery();
			while(rs.next()){
				int myid = rs.getInt("ID");
				String svr_n = rs.getString("NAME");
				String fs_n = rs.getString("FILESYSTEM");
				int s_id = rs.getInt("SERVER_ID");
				
				fs = new FileSystem(myid,s_id,svr_n,fs_n);
			}
			pmst.close();
			conn.close();
			return fs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fs;
		}
		
	}
	
	/**
	 * Description: Get the server with specified id
	 * @param id - id of the server to be retrieved
	 * @return - the retrieved server
	 */
	public static Server getServer(int id){
		Server svr = null;
		Connection conn = connectToDatabaseServer();
		String sql = "SELECT * FROM SERVER WHERE ID = ? ORDER BY NAME ASC";
		PreparedStatement pmst;
		try {
			pmst = conn.prepareStatement(sql);
			pmst.setInt(1, id);
			ResultSet rs = pmst.executeQuery();
			while(rs.next()){
				String servername = rs.getString("NAME");
				String address = rs.getString("ADDRESS");
				String type = rs.getString("TYPE");
				String username = rs.getString("USERNAME");
				String password = SecurityUtil.decrypt(rs.getString("PASSWORD"));
				User usr = new User(username,password);
				int myid = rs.getInt("ID");
				svr = new Server(myid,servername,address,type,usr);
			}
			pmst.close();
			conn.close();
			return svr;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Description: Get all the servers
	 * @return - the list of servers
	 */
	public static ArrayList<Server> getServers(){
		ArrayList<Server> server_list = new ArrayList<Server>();
		Connection conn = connectToDatabaseServer();
		String sql = "SELECT * FROM SERVER";
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt("ID");
				String name = rs.getString("NAME");
				String address = rs.getString("ADDRESS");
				String type = rs.getString("TYPE");
				String username = rs.getString("USERNAME");
				String password = SecurityUtil.decrypt(rs.getString("PASSWORD"));
				
				User usr = new User(username,password);
				Server svr = new Server(id,name,address,type,usr);
				server_list.add(svr);
			}
			stmt.close();
			conn.close();
			return server_list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Description: Retrieved all the server based on the specified type
	 * @param type_server - type of server to retrieved i.e. FILESYSTEM, PRINTER, OTHERS
	 * @return - the list of all the server with the specified type
	 */
	public static ArrayList<Server> getServers(String type_server){
		ArrayList<Server> server_list = new ArrayList<Server>();
		Connection conn = connectToDatabaseServer();
		String sql = "SELECT * FROM SERVER WHERE TYPE = ? ORDER BY NAME ASC";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, type_server);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				int id = rs.getInt("ID");
				String name = rs.getString("NAME");
				String address = rs.getString("ADDRESS");
				String type = rs.getString("TYPE");
				String username = rs.getString("USERNAME");
				String password = SecurityUtil.decrypt(rs.getString("PASSWORD"));
				
				User usr = new User(username,password);
				Server svr = new Server(id,name,address,type,usr);
				server_list.add(svr);
			}
			stmt.close();
			conn.close();
			return server_list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Description: Get all the filesystem under the server specified in the parameter
	 * @param server the server that contains all the filesystem to be get
	 * @return the list of all the filesystem under the server
	 */
	public static ArrayList<FileSystem> getFileSystem(Server server){
		ArrayList<FileSystem> fs_list = new ArrayList<FileSystem>();
		Connection conn = connectToDatabaseServer();
		String sql = "SELECT * FROM FS_SERVER WHERE SERVER_ID = ? ORDER BY FILESYSTEM ASC";
		try {
			PreparedStatement pmst = conn.prepareStatement(sql);
			pmst.setInt(1, server.getId());
			ResultSet rs = pmst.executeQuery();
			while(rs.next()){
				int id = rs.getInt("ID");
				int server_id = rs.getInt("SERVER_ID");
				String server_name = rs.getString("NAME");
				String fs_name = rs.getString("FILESYSTEM");
				
				FileSystem fs = new FileSystem(id,server_id,server_name,fs_name);
				fs_list.add(fs);
			}
			pmst.close();
			conn.close();
			return fs_list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Description: Setup the database needed by the monitoring tool. It will create the server if there is no 
	*/
	public static void setupDatabases(){
		Connection conn = connectToDatabaseServer();
		String serverSQL = "CREATE TABLE SERVER("+
				"ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1,  INCREMENT BY 1) PRIMARY KEY,"+
				"NAME VARCHAR(100) NOT NULL,"+
				"ADDRESS VARCHAR(200) NOT NULL,"+
				"TYPE VARCHAR(100) NOT NULL,"+
				"USERNAME VARCHAR(100) NOT NULL,"+
				"PASSWORD VARCHAR(100) NOT NULL,"+
				")";
		String fssSQL = "CREATE TABLE FS_SERVER("+
				"ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,"+
				"SERVER_ID BIGINT,"+
				"NAME VARCHAR(100),"+
				"FILESYSTEM VARCHAR(200),"+
				"CONSTRAINT FS_NAME_FK FOREIGN KEY (SERVER_ID) REFERENCES SERVER(ID)"+
				")";
		String fsSQL = "CREATE TABLE FS_DATA ("+
				"ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,"+
				"FS_ID BIGINT,"+
				"FILESYSTEM VARCHAR(200),"+
				"FREE_SPACE DECIMAL,"+
				"TOTAL_SPACE DECIMAL,"+
				"PERC_USED DECIMAL,"+
				"DIFF_FROM_LAST DECIMAL,"+
				"DATE_CHECKED TIMESTAMP"+
				")";
		
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(serverSQL);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			if(e.getErrorCode() == -5504){
				System.out.println("Table Server already exist. Proceeding...");
			}
		}
		
		
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(fssSQL);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			if(e.getErrorCode() == -5504){
				System.out.println("Table FileSystem already exist. Proceeding...");
			}
		}
		
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(fsSQL);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			if(e.getErrorCode() == -5504){
				System.out.println("Table FileSystem Data already exist. Proceeding...");
			}
		}
	}
	
	/**
	 *
	 * @return the connection to the database server
	 */
	public static Connection connectToDatabaseServer(){
		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(LOCATION, USERNAME, PASSWORD);
			return conn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage() + "\n\n"+"Error cannot connect to database!");
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage() + "\n\n"+"Error class not found!");
			return null;
		}
	}
}