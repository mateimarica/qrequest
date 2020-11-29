package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.objects.Question;

/**Class for getting questions from the database.
 */
public abstract class GetQuestionControl {
	
	/**Retrieves all the questions from the database.
	 * @return All the questions from the database.
	 */
	public static ArrayList<Question> getAllQuestions() {
		return DataManager.getAllQuestions();
	}
	
	/**Refreshes a question's voteCount, description, etc. in case they changed.
	 * @param question The <code>Question</code> to be refreshed.
	 */
	public static void refreshQuestion(Question question) {
		DataManager.refreshQuestion(question);
	}
}
