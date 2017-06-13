package com.foundation.service.usermod;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

public class JSONResponse {
	private String error;
	private String message;
	
	public void setError(String errorMessage){
		error = errorMessage;
	}
	
	public void setMessage(String deliverMessage){
		message = deliverMessage;
	}
	
	public String getError(){
		return error;
	}
	
	public String getMessage(){
		return message;
	}
	
	public String JSONString() throws Exception{
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String message = ow.writeValueAsString(this);
		return message;
	}
}
