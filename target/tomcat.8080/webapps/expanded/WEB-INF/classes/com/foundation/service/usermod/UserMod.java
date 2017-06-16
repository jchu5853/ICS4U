package com.foundation.service.usermod;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
			String message = responseString("UAE","Please try a different username");
			return Response.status(4001).entity(message).build();
		}
		if(!isValid(user.getID())){
			//RETURN USER INVALID
			String message = responseString("IVU","Please try a different username");
			return Response.status(4002).entity(message).build();
		}
		if(!isValidP(user.getPassword())){
			//RETURN PASSWORD INVALID
			String message = responseString("IVP","Please try a different password.");
			return Response.status(4002).entity(message).build();
		}
		if(user.getPassword().length() < 8 || user.getPassword().length()>32){
			//RETURN PASSWORD LONG OR TOO SHORT
			if(user.getPassword().length()<8){
				//RETURN PASSWORD TOO SHORT
				String message = responseString("PTS","Please try a different password.");
				return Response.status(4002).entity(message).build();
			}
			if(user.getPassword().length()>32){
				//RETURN PASSWORD TOO LONG
				String message = responseString("PTL","Please try a different password.");
				return Response.status(4002).entity(message).build();
			}
		}
		//END OF VALIDATION
		
		//RETURN SUCCESSFUL CREATION
		if(user.saveUser()){
			String message = responseString("Success","");
			return Response.ok().entity(message).build();
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
	
	@Path("/passwd")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response chPass(String userInput) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		User inputUser = mapper.readValue(userInput, User.class);
		if(inputUser.exists()){
			if(inputUser.isUser()){
				if(inputUser.getNewPasswd()!=null){
					inputUser.delete(inputUser.getID() + " " + inputUser.getPassword());
					inputUser.setPasswd(inputUser.getNewPasswd());
					inputUser.saveUser();
					String message = responseString("Success","");
					return Response.ok().entity(message).build();
				}
				else{
					String message = responseString("NNP","Please enter a new password.");
					return Response.status(4002).entity(message).build();
				}
			}
		}
		String message = responseString("INP","Please try entering the password again.");
		return Response.status(4000).entity(message).build();
	}
	
	@Path("/delete")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delAcct(String userInput) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		User inputUser = mapper.readValue(userInput, User.class);
		if(inputUser.exists()){
			if(inputUser.isUser()){
				inputUser.delete(inputUser.getID() + " " + inputUser.getPassword());
				String message = responseString("Success","");
				return Response.ok().entity(message).build();
			}
		}
		String message = responseString("INP","Please try entering the password again.");
		return Response.status(4000).entity(message).build();
	}
	
	private boolean isValid(String username){
		if(username.length() > 16 || username.length() < 4){
			return false;
		}
		for(int i=0;i<username.length();i++){
			if(!Character.isLetterOrDigit(username.charAt(i))){
				if(username.charAt(i) == '_' || username.charAt(i) == '-') continue;
				return false;
			}
		}
		return true;
	}

	private boolean isValidP(String password) {
		if(password.length() > 32 || password.length() < 8){
			return false;
		}
		for(int i=0;i<password.length();i++){
			if(!Character.isLetterOrDigit(password.charAt(i))){
				if(password.charAt(i) == '_' || password.charAt(i) == '-') continue;
				return false;
			}
		}
		return true;
	}
	
	public String responseString(String error,String message) throws Exception{
		JSONResponse respond = new JSONResponse();
		respond.setError(error);
		respond.setMessage(message);
		
		return respond.JSONString();
	}
}

