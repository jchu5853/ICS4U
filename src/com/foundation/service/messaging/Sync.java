package com.foundation.service.messaging;

import com.foundation.service.usermod.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

@Path("/messaging")
public class Sync {
	@Path("/send")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadMsg(String userInput)throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		JSONMessage msg = mapper.readValue(userInput, JSONMessage.class);
		User sender = new User();
		User receiver = new User();
		UserMod mod = new UserMod();
		sender.setUser(msg.getSender());
		sender.setPasswd(msg.getSenderPass());
		receiver.setUser(msg.getReceiver());
		if(sender.exists()){
			if(receiver.exists()){
				if(sender.isUser()){
					msg.saveMsg();
					String feedback = mod.responseString("Success", "");
					return Response.ok().entity(feedback).build();
				}
			}
			else{
				String feedback = mod.responseString("DNE", "Please enter a valid recipient.");
				return Response.status(4001).entity(feedback).build();
			}
		}
		
		String feedback = mod.responseString("INP","Please try entering your credentials again.");
		return Response.status(4000).entity(feedback).build();
	}
}
