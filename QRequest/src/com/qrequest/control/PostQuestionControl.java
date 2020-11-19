package com.qrequest.control;

import com.qrequest.object.Question;

/**Class for posting questions to the database.
 */
public abstract class PostQuestionControl {
	
	/**Sends a question to the DataManager to be put into the database.
	 * @param question The question being asked.
	 */
	public static void processPostQuestion(Question question) {
		DataManager.createQuestion(question);
	}
}
