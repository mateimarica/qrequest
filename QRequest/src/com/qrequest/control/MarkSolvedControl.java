package com.qrequest.control;

import com.qrequest.objects.Answer;
import com.qrequest.objects.Question;

public abstract class MarkSolvedControl {
	public static void markSolved(Question question, Answer answer) {
		DataManager.markSolved(question, answer);
	}
}
