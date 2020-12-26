package com.qrequest.objects;

import java.util.Locale;


public enum Language {
	ENGLISH("en", "English"),
	ROMANIAN("ro", "Român\u0103");
	//Add new languages here
	
	
	private Locale locale;
	private String acronym;
	private String name;
	
	private Language(String acronym, String name) {
		this.acronym = acronym;
		this.name = name;
		this.locale = new Locale(acronym);
		
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public String getAcronym() {
		return acronym;
	}
	
	public String getName() {
		return name;
	}	
	
	
}
