package com.qrequest.control;

import com.qrequest.objects.Answer;

/**Class for putting an answer into the database*/
public abstract class CreateAnswerControl {
	
	/**Posts an answer into the answer.
	 * @param answer The <code>Answer</code> that is to be added into the database.
	 */
	public static void processPostAnswer(Answer answer) {
		DataManager.postAnswer(answer);
	}
}
