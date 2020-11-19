package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.object.Question;

/**Class for getting questions from the database.
 */
public abstract class GetQuestionControl {
	
	/**Retrieves all the questions from the database.
	 * @return All the questions from the database.
	 */
	public static ArrayList<Question> getAllQuestions() {
		return DataManager.getAllQuestions();
	}
	
	public static void refreshQuestion(Question question) {
		DataManager.refreshQuestion(question);
	}
}
