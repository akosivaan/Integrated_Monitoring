package services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import ch.ethz.ssh2.Connection;
import entity.Server;
import utilities.DatabaseUtil;
import utilities.ServerUtil;

public class PrinterService {
    
	private ArrayList<String> server_names;
	private ArrayList<ArrayList<String>> result_server;
	/**
	 * Description: Main constructor for printer monitoring service
	 */
	public PrinterService() {
		// TODO Auto-generated constructor stub
		executePrinterMonitoring();
	}
	/**
	 * Description: Execute the printer monitoring process
	 */
	public void executePrinterMonitoring(){
		Iterator<Server> it_s = DatabaseUtil.getServers("PRINTER").iterator();
		server_names = new ArrayList<String>();
		result_server = new ArrayList<ArrayList<String>>();
		while(it_s.hasNext()){
			Server svr = it_s.next();
			System.out.println("***********START PRINTER SERVER***********");
			String commands = "lpstat -t | sed '/READY/d'; lpstat -W | sed '/READY/d'";
			Connection connect = ServerUtil.createSession(svr); //connect to the server
			StringTokenizer tokenize = new StringTokenizer(commands, ";");
			
			ArrayList<String> res; //Array list for the result
			ArrayList<ArrayList<String>> res2 = new ArrayList<ArrayList<String>>(); //array list for the list of result
			System.out.println("Executing commands...");
			while(tokenize.hasMoreTokens()){
				String comm = tokenize.nextToken().trim();
				res = ServerUtil.executeCommands(connect, comm); //execute the commands to the server
				res2.add(res);
			}
			System.out.println("Done executing commands...");
			System.out.println("Processing results...");
			processPMonitoring(res2,svr.getName()); //post process the result
			connect.close();
			System.out.println("***********END PRINTER SERVER***********");
		}
   }
	/**
	 * Description - create date base on date, date - 1 and date - 2 
	 * @param cal - the calendar to get the date on
	 * @return - the formatted date mm/dd/yyyy
	 */
	public String getDate(Calendar cal){
		String mon;
		String dy;
		String dy2;
		String dy3;
		String yr;
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DATE);
		int year = cal.get(Calendar.YEAR);
		if(month < 10)
			 mon = "0"+month;
		else
			 mon = ""+month;
		if(day < 10){
			dy = "0"+day;
		}
		else{
			dy = ""+day;
		}
		yr = (year+"").substring(2, 4);
		String date =  mon+ "/"+dy+"/"+yr;
		
		return date;
	}
	
/**
 * Description: Post process the result received from the server.
 * @param s - array list of result
 * @param servername - the name of the server
 */
public void processPMonitoring(ArrayList<ArrayList<String>> s,String servername){
		
		Iterator<String> res1 = s.get(0).iterator();
		Iterator<String> res2 = s.get(1).iterator();
		ArrayList<String> as = new ArrayList<String>();
		ArrayList<String> fres = new ArrayList<String>();
		
		//get the current date
		Calendar cal = Calendar.getInstance();
		String date1 = getDate(cal);
		cal.add(Calendar.DATE, -1);
		String date2 = getDate(cal);
		cal.add(Calendar.DATE, -1);
		String date3 = getDate(cal);
		int header = 1;
		
		System.out.println("Checking server:"+servername);
		System.out.println("Getting queued results...");
		while(res1.hasNext()){
			if(header <= 3){
				header++;
			}
			else{
				String curr = res1.next();
				if(curr.contains("@")){ //Check if those line with server also has a queued result
					if(curr.contains("STDIN")){
						String f4 = "";
						String f5 = "";
						if(res1.hasNext())
							f4 = res1.next();
						if(res1.hasNext())
							f5 = res1.next();
						
						if(f4.contains(date1)){
							as.add(curr);
						}
						else if(f4.contains(date2)){
							as.add(curr);
						}
						else if(f4.contains(date3)){
							as.add(curr);
						}
					}
				}
				else{ // Check if the current queue is within the date range
					String f1 = curr;
					String f2 = "";
					String f3 = "";
					if(res1.hasNext())
						f2 = res1.next();
					if(res1.hasNext())
						f3 = res1.next();
					if(f2.contains(date1)){
						as.add(f1);
					}
					else if(f2.contains(date2)){
						as.add(f1);
					}
					else if(f2.contains(date3)){
						as.add(f1 + " - 3 DAYS OLD!");
					}
				}
			}
		}
		
		System.out.println("Done queued results...");
		Iterator<String> g = as.iterator();
		boolean flag = true;
		String cl = "";
		String std = "";
		header = 1;
		System.out.println("Processing queued results..."); // Post process the last post-processed result
		while(res2.hasNext() && g.hasNext()){ //map the result in the next list of result
			if(header <=2){
				fres.add(res2.next());
				header++;
			}
			else{
				String curr = res2.next();
				if(curr.contains("@")){
						fres.add(curr);
						continue;
				}
				if(flag){
					cl = g.next();
					if(cl.contains("@") && cl.contains("STDIN")){
						continue;
					}
					int first_st = cl.indexOf("STDIN");
					int last_ind = 0;
					for(last_ind = first_st+1;;last_ind++){
						//System.out.println("Value of CL:"+cl);
						//System.out.println("Checking for spaces");
						//System.out.println("STD CURRENT CHAR:"+cl.charAt(last_ind));
						if(cl.charAt(last_ind) == ' '){
							break;
						}
					}
					std = cl.substring(first_st, last_ind);
			
					flag = false;
						
					if(fres.get(fres.size()-1).contains(std)){
						flag = true;
						continue;
					}
					
				}
				if(curr.contains(std)){
					fres.add(curr);
					flag = true;
				}
			}
		}
		
		System.out.println("Done processing queued results...");
		System.out.println("Post-process queued results...");
		int size = fres.size();
		boolean down = false;
		for(int i=0;i<size;i++){ //remove downed queued
			if(fres.get(i).contains("@") && fres.get(i).contains("DOWN")){
				down = true;
				fres.remove(i);
				i--;
				size--;
				continue;
			}
			if(down){
				if(fres.get(i).contains("@") && !fres.get(i).contains("DOWN")){
					down = false;
					continue;
				}
				fres.remove(i);
				i--;
				size--;
			}
		}
		
		//Final post process
		while(fres.size() > 4){
			fres.remove(3);
			size = fres.size();
		}
		System.out.println("Done post-processing queued results...");
		//Post-process
		server_names.add(servername);
		result_server.add(fres);
		//fres.add(0,servername);
		//writeToFile(fres,3);
	}
	/**
	 * Description: Create the report for printer monitoring
	 * @return - the built report on HTML format
	 */
	public String buildReport(){
		String report = "";
		StringBuilder sb = new StringBuilder();
		Timestamp ts = new Timestamp(new Date().getTime());
		sb.append("<h2 style=\"margin:0px 0px 0px 50px\">Printer Monitoring as of "+ts.toString()+"</h2>");
		sb.append("<table style=\"border:1px solid black; text-align:center; border-collapse: collapse;\" rules=\"all\">");
		sb.append("<tr style=\"background-color: seagreen\">"
				+ "<th style=\"padding: 6px;border:1px solid black\">SERVER</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">QUEUE</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">DEV</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">STATUS</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">JOB FILES</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">USER</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">PP</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">%</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">BLKS</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">CP</th>"
				+ "<th style=\"padding: 6px;border:1px solid black\">RNK</th>"
				+ "</tr>");
		int counter = 0;
		Iterator<String> it_s = server_names.iterator();
		while(it_s.hasNext()){
			sb.append("<tr style=\"background-color: green;border:1px solid black\">"
					+ "<td style=\"padding: 6px;border:1px solid black\"><b>"+it_s.next().toUpperCase()+"</b></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "<td style=\"border:1px solid black\"></td>"
					+ "</tr>");
			Iterator<String> it_res = result_server.get(counter).iterator();
			if(result_server.get(counter).isEmpty()){
				sb.append("<tr style=\"border:1px solid black\">"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"padding: 6px; border:1px solid black\"><h5>CLEAN</h5></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "</tr>");
			}
			else if(result_server.get(counter).size() == 2){
				sb.append("<tr style=\"border:1px solid black\">"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"padding: 6px;border:1px solid black\"><h5>CLEAN</h5></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "<td style=\"border:1px solid black\"></td>"
						+ "</tr>");
			}
			else{
				while(it_res.hasNext()){
					String res = it_res.next();
					if(!res.contains("Dev") && !res.contains("----")){
						StringTokenizer tok = new StringTokenizer(res," ");
						sb.append("<tr style=\"border:1px solid black\">");
						
						if(tok.countTokens() == 11){
							sb.append("<td style=\"padding: 6px;border:1px solid black\"></td>");
							for(int x=0;x<10;x++){
								if(x == 3){
									sb.append("<td style=\"padding: 6px;border:1px solid black;\">"+tok.nextToken()+" "+tok.nextToken()+"</td>");
								}
								else{
									sb.append("<td style=\"padding: 6px;border:1px solid black\">"+tok.nextToken()+"</td>");
								}
							}
						}
						else{
							sb.append("<td style=\"padding: 6px;border:1px solid black\"></td>");
							sb.append("<td style=\"padding: 6px;border:1px solid black\"></td>");
							sb.append("<td style=\"padding: 6px;border:1px solid black\"></td>");
							sb.append("<td style=\"padding: 6px;border:1px solid black\">"+tok.nextToken()+"</td>");
							sb.append("<td style=\"padding: 6px;border:1px solid black\">"+tok.nextToken()+" "+tok.nextToken()+"</td>");
							sb.append("<td style=\"padding: 6px;border:1px solid black\">"+tok.nextToken()+"</td>");
							sb.append("<td style=\"padding: 6px;border:1px solid black\"></td>");
							sb.append("<td style=\"padding: 6px;border:1px solid black\"></td>");
							sb.append("<td style=\"padding: 6px;border:1px solid black\">"+tok.nextToken()+"</td>");
							sb.append("<td style=\"padding: 6px;border:1px solid black\">"+tok.nextToken()+"</td>");
							sb.append("<td style=\"padding: 6px;border:1px solid black\">"+tok.nextToken()+"</td>");
						}
						sb.append("</tr>");
					}
				}
			}
			counter++;
		}
		sb.append("</table>");
		sb.append("</br></br>");
		report = sb.toString();
		return report;
	}
}
