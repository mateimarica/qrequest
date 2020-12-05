package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.objects.Question;

public abstract class PinQuestionControl {
	
	/**Pins a selected question.
	 */
	public static void pinQuestion(Question question) {
		DataManager.pinQuestion(question);
	}
	
}
