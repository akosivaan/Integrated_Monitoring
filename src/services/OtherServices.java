package services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import ch.ethz.ssh2.Connection;
import entity.Server;
import utilities.DatabaseUtil;
import utilities.ServerUtil;

public class OtherServices {

	public OtherServices() {
	}
	
	/**
	 * Description: Check the filesystem of /disks/stag* and magclean_list
	 * @return - the report on /disks/stag* and magclean_list
	 */
	public String imgprdService(){
		System.out.println("Starting imgprd");
		String report = "";
		StringBuilder sb = new StringBuilder();
		Iterator<Server> it_s = DatabaseUtil.getServers("OTHERS").iterator();
		while(it_s.hasNext()){
			Server srv = it_s.next();
			if(srv.getAddress().contains("imgprd")){
				Connection conn = ServerUtil.createSession(srv);
				ArrayList<String> res1 = new ArrayList<String>();
				
				res1 = ServerUtil.executeCommands(conn, "df -h /disks/stag* | grep /disks/stag"); //Check the /disks/stag*
				boolean ok1 = stagProcess(res1);
				ArrayList<String> res2 = ServerUtil.executeCommands(conn, "cd /opt/imaging_ha/conf && ls -ltr magclean_list"); //Check the magclean_list
				boolean ok2 = magcleanProcess(res2);
				
				//The report on HTML format
				sb.append("<h5>IMGPRD:</h5>");
				sb.append("<table style=\"border:1px solid black; text-align:center; border-collapse: collapse;\" rules=\"all\">");
				if(ok1){
					sb.append("<tr>"
							+ "<td style=\"padding: 6px;border:1px solid black\">/disks/stag*</td>"
							+ "<td style=\"background-color:seagreen;padding: 6px;border:1px solid black\">OK</td>"
							+ "</tr>");
				}
				else{
					sb.append("<tr>"
							+ "<td style=\"padding: 6px;border:1px solid black\">/disks/stag*</td>"
							+ "<td style=\"background-color:red;padding: 6px;border:1px solid black\">NO</td>"
							+ "</tr>");
				}
				if(ok2){
					sb.append("<tr>"
							+ "<td style=\"padding: 6px;border:1px solid black\">magclean_list</td>"
							+ "<td style=\"background-color:seagreen;padding: 6px;border:1px solid black\">OK</td>"
							+ "</tr>");
				}
				else{
					sb.append("<tr>"
							+ "<td style=\"padding: 6px;border:1px solid black\">magclean_list</td>"
							+ "<td style=\"background-color:red;padding: 6px;border:1px solid black\">NO</td>"
							+ "</tr>");
				}
				
				sb.append("</table>");
			}
		}
		report = sb.toString();
		return report;
	}
	
	/**
	 * Description: Check if the /disks/stag* is ok
	 * @param result - result to be process
	 * @return - if the result is ok or not
	 */
	private boolean stagProcess(ArrayList<String> result){
		Iterator<String> it_r = result.iterator();
		while(it_r.hasNext()){
			String r = it_r.next();
			StringTokenizer tokenizer = new StringTokenizer(r," %");
			int counter = 0;
			while(tokenizer.hasMoreTokens()){
				String s = tokenizer.nextToken();
				if(counter == 4){
					int val = Integer.parseInt(s);
					if(val > 97){
						return false;
					}
				}
				counter++;
			}
		}
		return true;
	}
	
	/**
	 * Description: Check if the magclean_list is ok 
	 * @param result - result to be processed
	 * @return - if the result is ok or not
	 */
	private boolean magcleanProcess(ArrayList<String> result){
		Iterator<String> it_r = result.iterator();
		while(it_r.hasNext()){
			String r = it_r.next();
			StringTokenizer tok = new StringTokenizer(r," ");
			int counter = 0;
			while(tok.hasMoreTokens()){
				String s = tok.nextToken();
				if(counter == 4){
					int val = Integer.parseInt(s);
					if(val != 5076){
						return false;
					}
				}
				counter++;
			}
		}
		return true;
	}

}
