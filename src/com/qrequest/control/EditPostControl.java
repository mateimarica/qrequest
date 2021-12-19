package com.qrequest.control;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Post;
import com.qrequest.ui.PopupUI;

/**Class for editing questions in the database.
 */
public class EditPostControl {
	
	/**Sends a question to the DataManager to be edited from the database.
	 * @param question The question being edited.
	 */
	public void processEditPost(Post post) {
		try {
			new DataManager().editPost(post);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
	}
}  