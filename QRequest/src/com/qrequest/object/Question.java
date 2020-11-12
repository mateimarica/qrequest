package com.qrequest.object;

import java.sql.Date;
import java.util.ArrayList;

public class Question {
	String id; //unique ID, invisible to user
	String title;
	String description;
	ArrayList<Tag> tags;
	
	User author;
	ArrayList<Vote> votes;
	Date timePosted;
	
	
}
