package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Message;
import com.qrequest.ui.PopupUI;

/**Class for processing the messaging functionality
 */
public class MessagingControl {
	
	/**Sends the message object to the DataManager to try to log in. 
	 * If successful, saves the new message information in the <code>Message</code> object.
	 * @param message The message object
	 */
	public void processSendMessage(Message message) {
		try {
			new DataManager().createMessage(message);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
	}
	
	/**Sends the Recipient's username to DataManager to filter messagaes
	 * @param userID <code>ArrayList&lt;String&gt;</code> the complete returned list of filtered Messages
	 */
	public ArrayList<Message> processAllFilteredMessages(String userID) {
		try {
			return new DataManager().getAllFilteredMessages(userID);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
		return null;
	}
}