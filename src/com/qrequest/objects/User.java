package com.qrequest.objects;


/**Represents a user.*/
public class User {
	
	/**<code>true</code> if user is an administrator, otherwise <code>false</code>*/
	private boolean isAdmin;
	
	/**The unique 3 to 10 character username. This is the primary key.*/
	private String username;
	
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
	
	/**Returns whether or not the user is an admin.
	 * @return <code>true</code> if user is an administrator, otherwise <code>false</code>
	 */
	public boolean isAdmin() {
		return isAdmin;
	}
	
	@Override
	public String toString() {
	    return username;
	}
}
