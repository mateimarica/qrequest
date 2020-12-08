package com.qrequest.objects;

import java.util.Date;

public class Message {
	private String sender;
	private String receiver;
	private String body;
	private TeiTime timeSent = new TeiTime();
	
	public Message (String sender, String receiver, String body, Date timeSent) {
		this.sender = sender;
		this.receiver = receiver;
		this.body = body;
		if(timeSent != null) {
			this.timeSent.setTimeInMillis(timeSent.getTime());
		}
		
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getReceiver() {
		return receiver;
	}
	
	public String getText() {
		return body;
	}
	
	public TeiTime getTimeSent() {
		return timeSent;
	}
}