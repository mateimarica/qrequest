package com.qrequest.object;

import java.util.Date;
import java.util.UUID;

public class Answer extends Post{
	
	private Question question;
	
	public Answer(String answer, User answerer, Question question, UUID id) {
		super(answer, answerer, id);
		this.question = question;
	}
	
	
	public Answer(String answer, User answerer, Question question, UUID id, Date timePosted) {
		super(answer, answerer, id, timePosted);
		this.question = question;
	}
	

	public Question getQuestion() {
		return question;
	}
	
	
}