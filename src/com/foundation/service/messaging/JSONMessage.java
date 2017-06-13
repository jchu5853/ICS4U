package com.foundation.service.messaging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class JSONMessage {
	private String sender;
	private String senderPass;
	private String receiver;
	private String msgBody;
	
	public String getMsgBody() {
		return msgBody;
	}
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	public String getSenderPass() {
		return senderPass;
	}
	public void setSenderPass(String senderPass) {
		this.senderPass = senderPass;
	}
	public boolean saveMsg() throws Exception{
		String filename = this.sender + "$$" + this.receiver + ".txt";
		File convo = new File(filename);
		String dataAdd = null;
		boolean successful = true;
		
		if(!convo.exists()){
			convo.createNewFile();
		}
		
		try(FileWriter fw = new FileWriter(filename, true);BufferedWriter bw = new BufferedWriter(fw);PrintWriter out = new PrintWriter(bw)){
			dataAdd = "$ " + this.msgBody;
			out.println(dataAdd);
		} catch (Exception e) {
			successful = false;
		}

		if(successful){
			return true;
		}
		return false;
	}
}
