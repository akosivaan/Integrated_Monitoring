package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JOptionPane;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import entity.Server;

public class ServerUtil {

	public ServerUtil() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Description: Create a connection to the server
	 * @param srv - the server object to connect with. See entity.Server
	 * @return - return the connection to the server
	 */
	public static Connection createSession(Server srv){
		Connection connect = new Connection(srv.getAddress());
		
		try {
			connect.connect();
			connect.authenticateWithPassword(srv.getUser().getUsername(), srv.getUser().getPassword());
		    return connect;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Description: Execute a command through SSH to the server, it will return the results of the executed commands.
	 * @param conn - connection to the server
	 * @param commands - command to be executed
	 * @return - return the results of the executed commands
	 */
	public static ArrayList<String> executeCommands(Connection conn, String commands){
		ArrayList<String> result = new ArrayList<String>();
		try {
			if(conn == null){
				System.out.println("Server is null");
			}
			Session s = conn.openSession();
			s.execCommand(commands);
			InputStream stdout = new StreamGobbler( s.getStdout() );
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
			String line;
			while((line = reader.readLine()) != null){
					result.add(line);
			}
			s.close();
			return result;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return result;
		}
	}

}
