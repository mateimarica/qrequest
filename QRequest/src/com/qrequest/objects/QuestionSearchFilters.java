package com.qrequest.objects;

public class QuestionSearchFilters {
	private String keywords;
	private Tag tag;
	private Boolean hasSolvedAnswer;
	
	public QuestionSearchFilters(String keywords, Tag tag, String hasSolvedAnswer) {
		this.keywords = keywords;
		this.tag = tag;
		
		if(hasSolvedAnswer == null) {
			this.hasSolvedAnswer = null;
		} else {
			switch(hasSolvedAnswer) {
				case "Yes":
					this.hasSolvedAnswer = true;
					break;
				case "No":
					this.hasSolvedAnswer = false;
					break;
				case "Either":
					this.hasSolvedAnswer = null;
			}
		}
	}
	
	public String getKeywords() {
		return keywords;
	}
	
	public Tag getTag() {
		return tag;
	}
	
	public Boolean hasSolvedAnswer() {
		return hasSolvedAnswer;
	}
}
