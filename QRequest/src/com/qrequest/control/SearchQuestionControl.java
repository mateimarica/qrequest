package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.objects.Question;
import com.qrequest.objects.QuestionSearchFilters;
import com.qrequest.objects.Report;

public abstract class SearchQuestionControl {
	
	/**Pins a selected question.
	 */
	public static ArrayList<Question> searchQuestions(QuestionSearchFilters filters) {
		return DataManager.getFilteredQuestions(filters);
	}
	
}
