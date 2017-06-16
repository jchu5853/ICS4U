package com.foundation.service.usermod;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
				tempPass = input.next();
				
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
	
	public boolean delete(String acctLine) {
        try {
            File db = new File("database.txt");
            if(!db.exists()){
            	return false;
            }

        	File tempFile = new File(db.getAbsolutePath() + ".tmp");
            BufferedReader br = new BufferedReader(new FileReader("database.txt"));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(acctLine)) {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();
            
            if (!db.delete()) {
                return false;
            }
            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(db)){
                return false;
            }
 
        } catch (Exception e){
        	return false;
        }
        
        return true;
    }
}
