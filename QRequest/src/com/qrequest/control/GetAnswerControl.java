package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.objects.Answer;
import com.qrequest.objects.Question;

/**Class for retrieving answers from the database
 */
public class GetAnswerControl {
	/**Retrieves all the answers associated with a question.
	 * @param question The <code>Question</code> object.
	 * @return <code>ArrayList&ltAnswer&gt</code>
	 */
	public static ArrayList<Answer> getAllAnswers(Question question) {
		return DataManager.getAllAnswers(question);
	}
}
