package com.qrequest.control;

import com.qrequest.objects.Answer;


public abstract class PostAnswerControl {
	
	public static void processPostAnswer(Answer answer) {
		DataManager.postAnswer(answer);
	}
}
