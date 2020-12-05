package com.qrequest.objects;

import java.util.ArrayList;

/**Represents a user.
 */
public class User {
	
	/**true if user is an administrator, otherwise false.
	 */
	private boolean isAdmin;
	
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
	public User(String username, boolean isAdmin) {
		this.username = username;
		this.isAdmin = isAdmin;
	}
	
	/**Returns the user's username
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	
	@Override
	public String toString() {
	    return username;
	}
}
