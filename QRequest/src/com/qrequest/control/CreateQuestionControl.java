package com.qrequest.control;

import com.qrequest.objects.Question;

/**Class for posting questions to the database.
 */
public abstract class CreateQuestionControl {
	
	/**Saves a question into the database from a question object.
	 * @param question The question being saved.
	 */
	public static void processPostQuestion(Question question) {
		DataManager.createQuestion(question);
	}
}
