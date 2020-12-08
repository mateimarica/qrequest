package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.objects.Message;

/**Class for processing the messaging functionality
 */
public abstract class MessageControl {
	
	/**Sends the message object to the DataManager to try to log in. 
	 * If successful, saves the new message information in the <code>Message</code> object.
	 * @param message The message object
	 */
	public static void processSendMessage(Message message) {
		DataManager.createMessage(message);
	}
	
	/**Sends the Recipient's username to DataManager to filter messagaes
	 * @param ArrayList<String> the complete returned list of filtered Messages
	 */
	public static ArrayList<String> processAllFilteredMessages(String userID) {
		 return DataManager.getAllFilteredMessages(userID);
	}
}