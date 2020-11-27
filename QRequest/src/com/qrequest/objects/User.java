package com.qrequest.objects;

import java.util.ArrayList;

/**Represents a user.
 */
public class User {
	
	/**The unique 3 to 10 character username. This is the primary key.
	 */
	private String username;
	
	/**The questions that the user has asked.
	 */
	private ArrayList<Question> questions;
	
	/**The answers that the user has made.
	 */
	private ArrayList<Answer> answers;
	
	/**Creates a User object with its username.
	 * @param username The username.
	 */
	public User(String username) {
		this.username = username;
	}
	
	/**Returns the user's username
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}
	
	/**Adds a questions to the user. Called when a user asks a new question.
	 * @param question The question object.
	 */
	public void addQuestion(Question question) {
		if(questions == null) {
			questions = new ArrayList<>();
		}
		
		questions.add(question);
	}
	
	@Override
	public String toString() {
	    return username;
	}
}
