package com.qrequest.control;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Credentials;
import com.qrequest.objects.User;
import com.qrequest.ui.PopupUI;

/**Class for processing the login
 */
public class LoginControl {
	
	/**The current logged-in user.<br><b>null</b> if no user logged-in.
	 */
	private static User user;
	
	/**Sends the username & password to the DataManager to try to log in. 
	 * If successful, saves the newly logged-in user in the <code>user</code> object.
	 * @param creds The login credentials.
	 * @return <code>True</code> if successfully logged in, <code>false</code> if failed.
	 * @throws DatabaseConnectionException If the database cannot be connected to.
	 */
	public boolean processLogin(Credentials creds) throws DatabaseConnectionException {
		user = new DataManager().getAccount(creds);
		return (user != null);
	}
	
	/**Hashes a password with SHA-1 for local storage of saved credentials.
	 * @param password The password to be hashed.
	 * @return The hashed password.
	 */
	public String hashPassword(String password) throws DatabaseConnectionException {
		return new DataManager().hashPassword(password);
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
