package services;

import java.io.Console;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import entity.Server;
import utilities.DatabaseUtil;

public class UserService {

	public UserService() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean flag = true;
		Scanner in = new Scanner(System.in);
		int ans;
		while(flag){
			System.out.println("************USER RELATED SERVICE************");
			System.out.println("[1]Change Password\n[2]Reset Password\n[3]Change User for Server\n[4]Add Server\n[5]Add Filesystem[6]Delete Server[7]Delete Filesystem\n[8]Exit\n\nPlease enter the number:");
			ans = in.nextInt();
			switch(ans){
			case 1: 
					changePassword();
					break;
			case 2:	System.out.println("Features to be added!");
					resetPassword();
					break;
			case 3: changeUserForServer();
					break;
			case 4: System.out.println("Features to be added!");
					break;
			case 5: System.out.println("Features to be added!");
					break;
			case 6: System.out.println("Features to be added!");
					break;
			case 7: System.out.println("Features to be added!");
					break;
			case 8:	System.out.println("Exiting program...");
			flag = false;
			break;
			default:System.out.println("Please select only from the choices");
					break;
			}
			
			System.out.println("");
		}
	}
	
	private static void addServer(){
		Scanner sc = new Scanner(System.in);
		Console con = System.console();
		printAllServer();
		System.out.println("ADDING A SERVER");
		System.out.println("");
		
		System.out.println("Enter the server name:");
		System.out.println("Enter the server address:");
		
	}
	
	private static void printAllServer(){
		ArrayList<Server> svr = DatabaseUtil.getServers();
		Iterator<Server> it_s = svr.iterator();
		System.out.println("");
		System.out.println("************************Server List*****************************");
		while(it_s.hasNext()){
			Server sr = it_s.next();
			
			System.out.println("* ID  |  SERVER NAME  |  ADDRESS  |  TYPE|  USERNAME *");
			System.out.println("* "+sr.getId()+" | "+sr.getName()+" | "+sr.getAddress()+" | "+sr.getType()+" | "+sr.getUser().getUsername()+" *");
			
		}
		System.out.println("****************************************************************");
	}
	
	private static void changeUserForServer(){
		Scanner sc = new Scanner(System.in);
		boolean flag = true;
		boolean flag2 = true;
		String npwd1 = "";
		String npwd2 = "";
		String username = "";
		Console con = System.console();
		ArrayList<Server> svr = DatabaseUtil.getServers();
		int id = 0;
		while(flag){
			System.out.println("");
			System.out.println("*****CHANGE USER FOR SERVER*****");
			Iterator<Server> it_s = svr.iterator();
			System.out.println("*******************************************************************");
			while(it_s.hasNext()){
				Server sr = it_s.next();
				
				System.out.println("* ID  |  SERVER NAME  |  ADDRESS  |  TYPE|  USERNAME *");
				System.out.println("* "+sr.getId()+" | "+sr.getName()+" | "+sr.getAddress()+" | "+sr.getType()+" | "+sr.getUser().getUsername()+" *");
				
			}
			System.out.println("*******************************************************************");
			
			System.out.print("Please enter the ID of the server:");
			id = sc.nextInt();
			System.out.print("Enter the new username:");
			username = sc.next();
			System.out.print("Enter the password:");
			npwd1 = new String(con.readPassword());
			System.out.print("Re-enter the password:");
			npwd2 = new String(con.readPassword());
			
			if(!npwd1.equals(npwd2)){
				System.out.println("Password and confirm password does not match!");
			}
			else if(DatabaseUtil.findServer(id) == -1){
				System.out.println("Invalid server id!");
			}
			else{
				DatabaseUtil.updateServerUser(username, npwd1, id);
				System.out.println("Server user updated!");
				flag = false;
			}
		}
	}
	
	private static void changePassword(){
		Scanner sc = new Scanner(System.in);
		boolean flag = true;
		String opwd;
		String npwd1;
		String npwd2;
		Console con = System.console();
		System.out.println("");
		
		while(flag){
			System.out.println("*****CHANGE PASSWORD*****");
			System.out.print("Please enter your username:");
			String username = sc.nextLine();
			System.out.print("Please enter your old password:");
			opwd = new String(con.readPassword());
			System.out.print("Please enter your new password:");
			npwd1 = new String(con.readPassword());
			System.out.print("Please confirm your new password:");
			npwd2 = new String(con.readPassword());
			
			int size = DatabaseUtil.findUser(username, opwd);
			if(size == -1){
				System.out.println("Invalid username and password");
			}
			else if(!npwd1.equals(npwd2)){
				System.out.println("New password and confirm password does not match!");
			}
			else{
				DatabaseUtil.updateUser(username,npwd1);
				System.out.println("Updating user complete!");
				flag = false;
			}
			System.out.println("");
		}
	}
	
	private static void resetPassword(){
		
	}
}
