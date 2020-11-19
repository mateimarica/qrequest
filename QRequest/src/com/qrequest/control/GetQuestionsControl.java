package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.object.Question;

/**Class for getting questions from the database.
 */
public abstract class GetQuestionsControl {
	
	/**Retrieves all the questions from the database.
	 * @return All the questions from the database.
	 */
	public static ArrayList<Question> getQuestions() {
		return DataManager.getQuestions();
	}
}
