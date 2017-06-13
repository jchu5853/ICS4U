package com.foundation.service.usermod;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Scanner;
import java.util.UUID;

import javax.ws.rs.core.Response;

import java.io.File;
import java.io.FileNotFoundException;

public class User {
	private String id;
	private String passwd;
	private String salt;
	private byte[] hash;
	
	public void setUser(String username){
		id = username;
	}
	
	public void setSalt(String value){
		salt = value;
	}
	
	public void setHash(byte[] value){
		hash = value;
	}
	
	public String getID(){
		return id;
	}
	
	public String getPassword(){
		return passwd;
	}
	
	public String getSalt(){
		return salt;
	}
	
	private Scanner input;
	
	public boolean exists() throws IOException{
		File database = new File("database.txt");
		String tempUser = null;
		String tempPass = null;
		String tempSalt = null;
		
		if(!database.exists()){
			database.createNewFile();
		}
		
		try{
			input = new Scanner(database);
			while(input.hasNext()){
				tempUser = input.nextLine();
				System.out.println("TEMPUSER: " + tempUser);
				tempPass = input.nextLine();
				System.out.println("TEMPPASS: " + tempPass);
				tempSalt = input.nextLine();
				System.out.println("TEMPSALT: " + tempSalt);
				
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
	
	private Formatter formats;
	
	public boolean saveUser() throws Exception{
		File database = new File("database.txt");
		boolean successful = true;
		
		if(!database.exists()){
			database.createNewFile();
		}
		
		try{
			formats = new Formatter(database);
			//formats.format("%s\n%s\n%s\n",id,hash,salt);
		} catch(Exception e){
			successful = false;
		} finally{
			formats.close();
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
			String tempSalt = input.next();
			
			if(tempUser.equals(getID())){
				try{
					MessageDigest md = MessageDigest.getInstance("SHA-1");
					String inputPassUser = getPassword() + tempSalt;
					md.update(inputPassUser.getBytes());
					String digested = new String(md.digest());
					System.out.println(digested);
					System.out.println(tempPass);
					if(digested.equals(tempPass)){
						System.out.println("USER IS VALID");
						return true;
					}
					else{
						System.out.println("USER NOT VALID");
						return false;
					}
				} catch(Exception e){
					return false;
				}
			}
		}
		
		return false;
	}
}
