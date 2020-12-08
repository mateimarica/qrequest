package com.qrequest.objects;

public class Message {
	private String sender;
	private String receiver;
	private String body;
	
	public Message (String senderIn, String receiverIn, String bodyIn) {
		this.sender = senderIn;
		this.receiver = receiverIn;
		this.body = bodyIn;
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
}