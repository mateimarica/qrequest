package com.qrequest.control;

import com.qrequest.object.Answer;


public abstract class PostAnswerControl {
	
	public static void processPostAnswer(Answer answer) {
		DataManager.postAnswer(answer);
	}
}
