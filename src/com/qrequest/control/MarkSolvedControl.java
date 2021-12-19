package com.qrequest.control;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Answer;
import com.qrequest.objects.Question;
import com.qrequest.ui.PopupUI;

/**Class for marking a question as solved/answered.*/
public class MarkSolvedControl {
	
	/**Mark a question as solved/answered.
	 * @param question The questi
	 * @param answer
	 */
	public void markSolved(Question question, Answer answer) {
		try {
			new DataManager().markSolved(question, answer);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
	}
}
