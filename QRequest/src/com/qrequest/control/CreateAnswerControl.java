package com.qrequest.control;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Answer;
import com.qrequest.ui.PopupUI;

/**Class for putting an answer into the database*/
public class CreateAnswerControl {
		
	/**Posts an answer into the answer.
	 * @param answer The <code>Answer</code> that is to be added into the database.
	 */
	public void processPostAnswer(Answer answer) {
		try {
			new DataManager().postAnswer(answer);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
	}
}
