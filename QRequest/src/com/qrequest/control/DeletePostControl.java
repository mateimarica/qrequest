package com.qrequest.control;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Post;
import com.qrequest.ui.PopupUI;

/**Class for deleting questions from the database.
 */
public class DeletePostControl {
	
	/**Mark an answer as having solved a question.
	 * @param question The <code>Question</code> in question
	 * @param answer The <code>Answer</code> that solved/answered the <code>Question</code>.
	 */
	public void processDeletePost(Post post) {
		try {
			new DataManager().deletePost(post);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
	}
}  