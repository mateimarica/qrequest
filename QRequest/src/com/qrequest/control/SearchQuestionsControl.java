package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Question;
import com.qrequest.objects.QuestionSearchFilters;
import com.qrequest.objects.Report;
import com.qrequest.ui.PopupUI;

/**Search for questions.*/
public class SearchQuestionsControl {
	
	/**Get a list of questions using a list of filters specified by the user. 
	 * @param filters The <code>QuestionSearchFilters</code> object.
	 * @return An <code>ArrayList<code> of questions.
	 */
	public ArrayList<Question> searchQuestions(QuestionSearchFilters filters) {
		try {
			return new DataManager().getFilteredQuestions(filters);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
		
		return null;
	}
	
}
