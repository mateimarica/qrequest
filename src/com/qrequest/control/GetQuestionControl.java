package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Question;
import com.qrequest.ui.PopupUI;

/**Class for getting questions from the database.
 */
public class GetQuestionControl {
	
	/**Retrieves all the questions from the database.
	 * @return All the questions from the database.
	 */
	public ArrayList<Question> getAllQuestions() {		
		try {
			return new DataManager().getAllQuestions();
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
		
		return null;
	}
	
	/**Refreshes a question's voteCount, description, etc. in case they changed.
	 * @param question The <code>Question</code> to be refreshed.
	 */
	public void refreshQuestion(Question question) {
		try {
			 new DataManager().refreshQuestion(question);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
	}
}
