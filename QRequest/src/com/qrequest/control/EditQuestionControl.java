package com.qrequest.control;

import com.qrequest.objects.Question;

/**Class for deleting questions to the database.
 */
public abstract class EditQuestionControl {

	/**Sends a question to the DataManager to be removed from the database.
	 * @param question The question being removed.
	 */
	public static void processEditQuestion(Question question) {
		DataManager.editQuestion(question);
	}
} 