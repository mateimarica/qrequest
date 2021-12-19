package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Answer;
import com.qrequest.objects.Question;
import com.qrequest.ui.PopupUI;

/**Class for retrieving answers from the database
 */
public class GetAnswerControl {
	
	/**Retrieves all the answers associated with a question.
	 * @param question The <code>Question</code> object.
	 * @return <code>ArrayList&ltAnswer&gt</code>
	 */
	public ArrayList<Answer> getAllAnswers(Question question) {
		try {
			return new DataManager().getAllAnswers(question);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
		
		return null;
	}
}
