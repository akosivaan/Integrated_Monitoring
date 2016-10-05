package services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import ch.ethz.ssh2.Connection;
import entity.FSRecord;
import entity.FileSystem;
import entity.Server;
import utilities.DatabaseUtil;
import utilities.ServerUtil;

public class FilesystemService {
	/**
	 * Description: Main constructor of the filesystem service
	 * @param mode - the type of filesystem to create a report on. I.E. FULFILLMENT AND IMAGING and CLAIMS AND ADJUDICATION
	 */
	public FilesystemService(String mode) {
		// TODO Auto-generated constructor stub
		executeFSMonitoring(mode);
	}
	
	/**
	 * Description: Process the filesystem monitoring through SSH in server and executing the necessary commands.
	 * @param s_type - - the type of filesystem to create a report on. I.E. FULFILLMENT AND IMAGING and CLAIMS AND ADJUDICATION
	 */
	public void executeFSMonitoring(String s_type){
		
		ArrayList<Server> svr_l = new ArrayList<Server>();
		//If the type of server is FULFILLMENT AND IMAGING
		if(s_type.equals("FULFILLMENT AND IMAGING")){
			svr_l = DatabaseUtil.getServers("FILESYSTEM - FULFILLMENT AND IMAGING");
		}
		else{ // If the type of server is Claims and Adjudication
			//Create a databaseutil that get base on the mode. I.E claims or efulfill
			svr_l = DatabaseUtil.getServers("FILESYSTEM - CLAIMS AND ADJUDICATION");
		}
		
		Iterator<Server> s_it = svr_l.iterator(); // Iterator for all the servers.
		while(s_it.hasNext()){
			Server srv = s_it.next();
			Connection connection = ServerUtil.createSession(srv); //Create a connection with the server
			Iterator<FileSystem> fs_it = DatabaseUtil.getFileSystem(srv).iterator(); //Get all the filesystem under the server
			ArrayList<String> ans = new ArrayList<String>();
			System.out.println("****************START SERVER****************");
			System.out.println("Starting monitoring for server "+srv.getName());
			int os_code = checkOS(connection); //Checking the OS of the server.
			switch(os_code){
			case 1:System.out.println("OS of server: AIX");
				   break;
			case 2:System.out.println("OS of server: SunOS");
				   break;
			case 3:System.out.println("OS of server: LINUX");
				   break;
			default:System.out.println("OS not found");
				   break;
			}
			while(fs_it.hasNext()){ //Iterate all the filesystem under the server
				FileSystem fs = fs_it.next();
				System.out.println("****************START FILESYSTEM "+fs.getFileSystem()+"****************");
				ans = executeFileSystem(connection,fs,os_code); //Execute the filesystem and return the result
				Iterator<String> as = ans.iterator();
				while(as.hasNext()){
					String res = as.next();
					processResult(fs,res,os_code); //processing of the result
				}
				System.out.println("*******************END FILESYSTEM*******************");
				
			}
			System.out.println("****************END SERVER****************");
			System.out.println("");
			connection.close();
		}
	}
	
	/**
	 * Description: Processing of the filesystem monitoring results base on the executed commands
	 * @param fs - the filesystem that was monitored
	 * @param result - the result to be processed
	 * @param os_code - the type of OS
	 */
	public void processResult(FileSystem fs,String result,int os_code){
		System.out.println("Processing result for "+fs.getFileSystem());
		StringTokenizer tokenizer;
		BigDecimal total_space = null;
		BigDecimal used_space = null;
		BigDecimal percent_used = null;
		String file_system = "";
		Date c_date = new Date();
		Timestamp ts = new Timestamp(c_date.getTime()); //Create the date today
		//If OS is AIX
		if(os_code == 1){
			tokenizer = new StringTokenizer(result, " ");
			int ct = 0;
			while (tokenizer.hasMoreElements()){
				String x = tokenizer.nextElement().toString();
		        x = x.trim();
		        switch (ct){
		          case 0: break;
		          case 1:total_space = new BigDecimal(x);
		             	 break;
			      case 2:used_space = new BigDecimal(x);
			             break;
			      case 3:x = x.replace("%", "");
			             percent_used = new BigDecimal(x);
			             break;
			      case 4:break;
			      case 5:break;
			      case 6:if (x.contains("\n")) {
			                 x = x.split("\n")[0];
			             }else if (x.contains("\r")) {
			                 x = x.split("\r")[0];
			             }
			               x = x.trim();
			               file_system = x;
			               break;
			             }
			            ct++;
		    }
		}
		//If OS is SunOS or Linux
		else if(os_code == 2 || os_code == 3){
			tokenizer = new StringTokenizer(result, " ");
			int ct = 0;
			while (tokenizer.hasMoreElements())
			{
			  String x = tokenizer.nextElement().toString();
			  x = x.trim();
			  switch (ct)
			  {
			  case 0: 
			    break;
			  case 1: 
			    if (x.contains("G"))
			    {
			      x = x.replace("G", "");
			      total_space = new BigDecimal(x);
			    }
			    else if (x.contains("M"))
			    {
			      x = x.replace("M", "");
			      total_space = new BigDecimal(x);
			      total_space = total_space.divide(new BigDecimal(1024));
			    }
			    else if (x.contains("K"))
			    {
			      x = x.replace("K", "");
			      total_space = new BigDecimal(x);
			      total_space = total_space.divide(
			        new BigDecimal(1024)).divide(
			        new BigDecimal(1024));
			    }
			    break;
			  case 2: 
			    if (x.contains("G"))
			    {
			      x = x.replace("G", "");
			      used_space = new BigDecimal(x);
			      used_space = total_space.subtract(used_space);
			    }
			    else if (x.contains("M"))
			    {
			      x = x.replace("M", "");
			      used_space = new BigDecimal(x);
			      used_space = used_space.divide(new BigDecimal(
			        1024));
			      used_space = total_space.subtract(used_space);
			    }
			    else if (x.contains("K"))
			    {
			      x = x.replace("K", "");
			      used_space = new BigDecimal(x);
			      used_space = used_space.divide(
			        new BigDecimal(1024)).divide(
			        new BigDecimal(1024));
			      used_space = total_space.subtract(total_space);
			    }
			    break;
			  case 3: 
			    break;
			  case 4: 
			    x = x.replace("%", "");
			    percent_used = new BigDecimal(x);
			    //percent_used = new BigDecimal(percent_used);
			      //.subtract(percent_used);
			    break;
			  case 5: 
			    if (x.contains("\n")) {
			      x = x.split("\n")[0];
			    } else if (x.contains("\r")) {
			      x = x.split("\r")[0];
			    }
			    file_system = x;
			    break;
			  case 6: 
			    break;
			  }
			  ct++;
			}
		}
		FSRecord old = DatabaseUtil.getFSLastRecord(fs.getId());
		BigDecimal b;
		if(old == null){
			b = new BigDecimal(0);
		}
		else{
			BigDecimal c = new BigDecimal(percent_used.toBigInteger());
			b = c.subtract(old.getPerc_used()).abs();
		}
		FSRecord fsr = new FSRecord(fs.getId(),file_system,used_space,total_space,percent_used,b,ts);
		fsr.saveRecord(); //Save the record in database
		if(old != null)
			System.out.println("Last time checked:"+old.getDate_checked().toString());
		System.out.println("Split result:");
		System.out.println("Total Space:"+total_space);
		System.out.println("Used Space:"+used_space);
		System.out.println("Percent Used(%):"+percent_used);
		System.out.println("Date:"+ts);
	}
	/**
	 * Description: Execute the commands in filesystem and get the result.
	 * @param s - SSH connection to the server
	 * @param fs - the filesystem to make the monitoring on
	 * @param os_code - the OS type
	 * @return - return the results of the execution
	 */
	public ArrayList<String> executeFileSystem(Connection s, FileSystem fs,int os_code){
		ArrayList<String> ans = new ArrayList<String>();
		String command = "";
		//The commands depends on the os of the server
		if(os_code == 1){
			command = "df -g ";
		}
		else if(os_code == 2){
			command = "df -h ";
		}
		else if(os_code == 3){
			command = "df -h ";
		}
		//Creating the commands
		command = command.concat(fs.getFileSystem());
		command = command.concat(" | grep ");
		command = command.concat(fs.getFileSystem());
		
		System.out.println("Command: "+ command);
		System.out.println("Executing command..."); // Command execution in the server.
		ans = ServerUtil.executeCommands(s,command);
		return ans;
	}
	/**
	 * Description: Check the OS of the server.
	 * @param s - the SSH connection to the server
	 * @return - the OS code for type of OS
	 */
	public int checkOS(Connection s){
		int os_code = 0;
		System.out.println("Checking OS...");
		String os = ServerUtil.executeCommands(s, "uname").get(0);
		if(os.contains("AIX")){ // If the OS is AIX
			os_code = 1;
		}
		else if(os.contains("SunOS")){ // If the OS is SunOS
			os_code = 2;
		}
		else if(os.contains("Linux")){ // If the OS is Linux
			os_code = 3;
		}
		return os_code;
	}
	
	/**
	 * Description: Build the filesystem report.
	 * @param type - the type of filesystem to create a report on. I.E. FULFILLMENT AND IMAGING and CLAIMS AND ADJUDICATION
	 * @return - reports created in html format
	 */
	public String buildReport(String type){
		String report = "";
		StringBuilder sb = new StringBuilder();
		Iterator<Server> it_s = DatabaseUtil.getServers(type).iterator();
		Timestamp ts = new Timestamp(new Date().getTime());
		sb.append("<h2 style=\"margin:0px 0px 0px 50px\">Filesystem Monitoring as of "+ts.toString()+"</h2>");
		sb.append("<table style=\"border:1px solid black; text-align:center; border-collapse: collapse;\" rules=\"all\">");
		sb.append("<tr style=\"background-color: seagreen\">"
				+ "<th style=\"padding: 6px;border:1px solid black\">Server</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">Filesystem</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">Free Space(GB)</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">Total Space(GB)</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">% USED</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">% Difference From Last Check</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">Previous Date Checked</th>"
				+ "</tr>");
		while(it_s.hasNext()){
			Server svr = it_s.next();
			sb.append("<tr style=\"background-color: green; border:1px solid black\"><td style=\"border:1px solid black\"><b>"+svr.getName()+"</b></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "</tr>");
			Iterator<FileSystem> it_fs = DatabaseUtil.getFileSystem(svr).iterator();
			
			while(it_fs.hasNext()){
				FileSystem fs = it_fs.next();
				FSRecord fsr = DatabaseUtil.getFSLastRecord(fs.getId());
				FSRecord fsr_2 = DatabaseUtil.getSecondToLast(fs.getId());
				sb.append("<tr style=\"border:1px solid black\">");
				sb.append("<td style=\"border:1px solid black\"></td>");
				sb.append("<td style=\"border:1px solid black\">"+fsr.getFilesystem()+"</td>");
				sb.append("<td style=\"border:1px solid black\">"+fsr.getFree_space()+"</td>");
				sb.append("<td style=\"border:1px solid black\">"+fsr.getTotal_space()+"</td>");
				if(!svr.getName().equals("IMGAPRD1")){
					if(Integer.parseInt(fsr.getPerc_used().toString()) < 95)
						sb.append("<td style=\"border:1px solid black\">"+fsr.getPerc_used()+"</td>");
					else
						sb.append("<td style=\"border:1px solid black;background-color:red;\">"+fsr.getPerc_used()+"</td>");
				}
				else{
					sb.append("<td style=\"border:1px solid black\">"+fsr.getPerc_used()+"</td>");
				}
				
				sb.append("<td style=\"border:1px solid black\">"+fsr.getDiff_from_last()+"</td>");
				if(fsr_2 == null)
					sb.append("<td style=\"border:1px solid black\"></td>");
				else
					sb.append("<td style=\"border:1px solid black\">"+fsr_2.getDate_checked()+"</td>");
				sb.append("</tr>");
			}
		}
		sb.append("</table>");
		sb.append("</br></br>");
		report = sb.toString();
		return report;
	}
}
