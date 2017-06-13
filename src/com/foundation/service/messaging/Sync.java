package com.foundation.service.messaging;

import com.foundation.service.usermod.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Scanner;

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

	@Path("/fetch")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadMsg(String userInput)throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<JSONMessage> msg = new ArrayList<JSONMessage>();
		UserMod mod = new UserMod();
		User receiver = mapper.readValue(userInput, User.class);
		
		if(receiver.exists()){
			if(receiver.isUser()){
				File[] convos = finder(".",receiver.getID());
				for(int i=0;i<convos.length;i++){
					Scanner input = new Scanner(convos[i]);
					String msgBody = null;
					int delim = (convos[i].getName()).indexOf("$");
					String sender = (convos[i].getName()).substring(0,delim);
					while(input.hasNext()){
						msgBody = input.nextLine();
						JSONMessage temp = new JSONMessage();
						temp.setMsgBody(msgBody);
						temp.setSender(sender);
						msg.add(temp);
						System.out.println(temp.getMsgBody());
					}
					input.close();
				}
			}
		}
		
		
		String feedback = mod.responseString("INP","Please try entering your credentials again.");
		return Response.status(4000).entity(feedback).build();
	}
	
	public File[] finder(String dirName,String receiver){
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
        	public boolean accept(File dir, String filename){
        		return filename.endsWith(receiver + ".txt"); }
        });
    }
}
