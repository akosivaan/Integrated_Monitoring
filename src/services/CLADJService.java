package services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CLADJService {

	public CLADJService() {
		// TODO Auto-generated constructor stub
		
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
		CLADJService cs = new CLADJService();
	}
	
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
				if(fi.getName().equals("clmadj.html") ){
					fi.delete();
					break;
				}
			}
	}

}
