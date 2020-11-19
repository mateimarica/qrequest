package com.qrequest.control;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.object.User;

/**Class for processing the login
 */
public abstract class LoginControl {
	
	/**The current logged-in user.<br><b>null</b> if no user logged-in.
	 */
	private static User user;
	
	/**Sends the username & password to the DataManager to try to log in. 
	 * If successful, saves the newly logged-in user in the <code>user</code> object.
	 * @param username The username.
	 * @param password The password.
	 * @return <code>True</code> if successfully logged in, <code>false</code> if failed.
	 * @throws DatabaseConnectionException If the database cannot be connected to.
	 */
	public static boolean processLogin(String username, String password) throws DatabaseConnectionException {
		user = DataManager.getAccount(username, password);
		return (user != null);
	}
	
	/**Returns the logged-in user.
	 * @return The logged-in user.
	 */
	public static User getUser() {
		return user;
	}
	

	/**Sets the logged-in user to null. For use when logged out.
	 */
	public static void resetUser() {
		user = null;
	}

	
}
