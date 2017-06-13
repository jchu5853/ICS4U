package com.foundation.service.usermod;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class User {
	private String id;
	private String passwd;
	private String newPasswd = null;
	
	public void setUser(String username){
		this.id = username;
	}
	
	public void setPasswd(String hash){
		this.passwd = hash;
	}	
	
	public String getID(){
		return this.id;
	}
	
	public String getPassword(){
		return this.passwd;
	}
	
	public String getNewPasswd() {
		return newPasswd;
	}

	public void setNewPasswd(String newPasswd) {
		this.newPasswd = newPasswd;
	}

	private Scanner input;
	
	public boolean exists() throws IOException{
		File database = new File("database.txt");
		String tempUser = null;
		String tempPass = null;
		
		if(!database.exists()){
			database.createNewFile();
		}
		
		try{
			input = new Scanner(database);
			while(input.hasNext()){
				tempUser = input.next();
				System.out.println("TEMPUSER: " + tempUser);
				tempPass = input.next();
				System.out.println("TEMPPASS: " + tempPass);
				
				if(tempUser.equals(getID())){
					return true;
				}
			}
		} catch(FileNotFoundException e){
			return false;
		}
		finally{
			input.close();
		}
		return false;
	}
	
	public boolean saveUser() throws Exception{
		File database = new File("database.txt");
		String dataAdd = null;
		boolean successful = true;
		
		if(!database.exists()){
			database.createNewFile();
		}
		
		try(FileWriter fw = new FileWriter("database.txt", true);BufferedWriter bw = new BufferedWriter(fw);PrintWriter out = new PrintWriter(bw)){
			dataAdd = getID() + " " + getPassword();
			out.println(dataAdd);
		} catch (Exception e) {
			successful = false;
		}

		if(successful){
			return true;
		}
		return false;
	}
	
	public boolean isUser() throws Exception{
		File database = new File("database.txt");
		input = new Scanner(database);
		
		while(input.hasNext()){
			String tempUser = input.next();
			String tempPass = input.next();
			
			if(tempUser.equals(getID()) && tempPass.equals(getPassword())){
				return true;
			}
		}
		
		return false;
	}
}
