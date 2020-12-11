package com.qrequest.control;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Question;
import com.qrequest.ui.PopupUI;

/**Pin a question*/
public class PinQuestionControl {
	
	/**Pins a selected question.
	 * @param question The question being pinned.
	 */
	public void pinQuestion(Question question) {
		try {
			new DataManager().pinQuestion(question);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
	}
	
}
