package com.qrequest.objects;

public class QuestionSearchFilters {
	private String title;
	private Tag tag;
	
	public QuestionSearchFilters(String title, Tag tag) {
		this.title = title;
		this.tag = tag;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Tag getTag() {
		return tag;
	}
}
