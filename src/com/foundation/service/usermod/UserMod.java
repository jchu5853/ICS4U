package com.foundation.service.usermod;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.UUID;
import java.io.IOException;
import java.security.MessageDigest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

@Path("/usermod")
public class UserMod {

	@Path("/register")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response makeUser(String userInput) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.readValue(userInput,User.class);
		
		//VALIDATE THE AVAILABILITY AND VALIDITY OF CREDENTIALS
		if(user.exists()){
			//RETURN USER ALREADY EXISTS
			return Response.status(4001).build();
		}
		if(!isValid(user.getID())){
			//RETURN USER INVALID
			return Response.ok().build();
		}
		if(user.getPassword().length() < 8 || user.getPassword().length()>32){
			//RETURN PASSWORD LONG OR TOO SHORT
			if(user.getPassword().length()<8){
				//RETURN PASSWORD TOO SHORT
				return Response.ok().build();
			}
			if(user.getPassword().length()>32){
				//RETURN PASSWORD TOO LONG
				return Response.ok().build();
			}
		}
		//END OF VALIDATION
		
		//SET USER ACCOUNT VARIABLES
			try{
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				String salt = UUID.randomUUID().toString();
				user.setSalt(salt);
				String passUser = user.getPassword() + user.getSalt();
				md.update(passUser.getBytes());
				user.setHash(md.digest());
				System.out.println(new String(md.digest()));
			} catch(Exception e){
				return Response.serverError().build();
			}
		
		//RETURN SUCCESSFUL CREATION
		if(user.saveUser()){
			return Response.ok().build();
		}
		return Response.status(4000).build();
	}

	@Path("/check")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response checkUser(String userInput) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		User inputUser = mapper.readValue(userInput,User.class);
		if(inputUser.exists()){
			if(inputUser.isUser()){
				return Response.ok().build();
			}
		}
		return Response.status(4000).build();
	}
	
	private boolean isValid(String username){
		if(username.length() > 16 || username.length() < 4){
			return false;
		}
		for(int i=0;i<username.length();i++){
			if(!Character.isLetterOrDigit(username.charAt(i)) && (username.charAt(i) != '_' || username.charAt(i) != '-')){
				return false;
			}
		}
		return true;
	}
}

