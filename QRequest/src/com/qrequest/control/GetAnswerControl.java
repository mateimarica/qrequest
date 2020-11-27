package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.objects.Answer;
import com.qrequest.objects.Question;

public class GetAnswerControl {
	public static ArrayList<Answer> getAllAnswers(Question question) {
		return DataManager.getAllAnswers(question);
	}
}
