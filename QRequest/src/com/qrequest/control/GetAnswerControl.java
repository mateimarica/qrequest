package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.object.Answer;
import com.qrequest.object.Question;

public class GetAnswerControl {
	public static ArrayList<Answer> getAllAnswers(Question question) {
		return DataManager.getAllAnswers(question);
	}
}
