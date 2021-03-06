package services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Security;
import java.util.Iterator;
import java.util.Properties;


public class MainService {
	
	/**
	 * Description: This is the core of the whole program. It create the filesystem and printer monitoring reports. 
	 */
	public MainService() {
		// TODO Auto-generated constructor stub
		deleteFiles(); //Delete the previous reports.
		
		String report = "";
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE HTML>"
				+ "<html>"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html\"/>"
				+ "<body>");
		System.out.println("Running the monitoring...");
		System.out.println("Starting filesystem monitoring...");
		
		FilesystemService fs_s = new FilesystemService("FULFILLMENT AND IMAGING"); //A service that connects to the server and executes the commands
		
		System.out.println("Building report for filesystem");
		System.out.println("Done building report for filesystem");
		sb.append(fs_s.buildReport("FILESYSTEM - FULFILLMENT AND IMAGING"));
		
		OtherServices os = new OtherServices(); //Other services
		sb.append(os.imgprdService()); //Service for imgprd that is part of the filesystem monitoring.
		sb.append("</br></br>");
		System.out.println("Finish filesystem Monitoring...");
		System.out.println("");
		System.out.println("Starting printer monitoring...");
		
		PrinterService ps = new PrinterService(); //A service that connects to the server and executes the commands
		System.out.println("Building report for printer");
		
		sb.append(ps.buildReport());
		
		System.out.println("Done building report for printer");
		System.out.println("Finish printer Monitoring...");
		sb.append("</body>"
				+ "</html>");
		report = sb.toString();
		writeToFile(report,"fai");
		StringBuilder sb2 = new StringBuilder();
		sb2.append("<!DOCTYPE HTML>"
				+ "<html>"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html\"/>"
				+ "<body>");
		FilesystemService fs_s2 = new FilesystemService("CLAIMS AND ADJUDICATION");
		sb2.append(fs_s2.buildReport("FILESYSTEM - CLAIMS AND ADJUDICATION"));
		sb2.append("</body>"
				+ "</html>");
		writeToFile(sb2.toString(),"clmadj");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainService msrv = new MainService();
	}
	
	/**
	 * Description: Create a HTML file that contains the content.
	 * @param contents - the content of the file to be written
	 * @param report_name - name of the file in HTML
	 */
	public void writeToFile(String contents,String report_name){
		try {
			System.out.println("Writing to file...");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("result/"+report_name+".html",false)));
			pw.println(contents);
			pw.close();
			System.out.println("Done writing...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Description: Delete all the previous reports created in result folder
	 */
	public void deleteFiles(){
		File f = new File("result");
		f.mkdir();
			for(File fi: f.listFiles()){
				fi.getName();
				fi.delete();
			}
	}
}
