package com.qrequest.control;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Question;
import com.qrequest.ui.PopupUI;

/**Class for posting questions to the database.
 */
public class AskQuestionControl {
	
	/**Saves a question into the database from a question object.
	 * @param question The question being saved.
	 */
	public void processPostQuestion(Question question) {
		try {
			new DataManager().createQuestion(question);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
	}
}
