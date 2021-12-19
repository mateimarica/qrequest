package com.qrequest.objects;

import java.util.Date;

/**Represents a message.*/
public class Message {
	
	/**The sender's username.*/
	private String sender;
	
	/**The receiver's username.*/
	private String receiver;
	
	/**The body/text of the message.*/
	private String body;
	
	/**The time that this message was sent.*/
	private TeiTime timeSent;
	
	/**Create a message object.
	 * @param sender The sender's username.
	 * @param receiver The receiver's username.
	 * @param body The body/text of the message.
	 * @param timeSent The time that this message was sent.
	 */
	public Message (String sender, String receiver, String body, Date timeSent) {
		this.sender = sender;
		this.receiver = receiver;
		this.body = body;
		this.timeSent = new TeiTime();
		this.timeSent.setTimeInMillis(timeSent.getTime());
	}
	
	/**Create a message without a timestamp.
	 * @param sender The sender's username.
	 * @param receiver The receiver's username.
	 * @param body The body/text of the message.
	 */
	public Message (String sender, String receiver, String body) {
		this.sender = sender;
		this.receiver = receiver;
		this.body = body;
	}
	
	/**Returns the sender's username.
	 * @return The sender's username.
	 */
	public String getSender() {
		return sender;
	}
	
	/**Returns the receiver's username.
	 * @return The receiver's username.
	 */
	public String getReceiver() {
		return receiver;
	}
	
	/**Returns the body/text of the message.
	 * @return The body/text of the message.
	 */
	public String getText() {
		return body;
	}
	
	/**Returns the time that this message was sent.
	 * @return The time that this message was sent.
	 */
	public TeiTime getTimeSent() {
		return timeSent;
	}
}