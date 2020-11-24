package com.qrequest.control;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.object.Credentials;

/**Control class for creating an account.
 */
public abstract class CreateAccountControl {
	
	/**Sends a username & password to the DataManager to create an account.
	 * @param username
	 * @param password
	 */
	public static void processCreateAccount(Credentials creds) {
		DataManager.createAccount(creds);
	}
	
	/**Sends a username to the DataManager to check if the given username exists as a User in the database.
	 * @param username
	 * @return <code>True</code> if the account exists, <code>false</code> if not.
	 * @throws DatabaseConnectionException If there is no connection to the database.
	 */
	public static boolean processDoesAccountExist(String username) throws DatabaseConnectionException {
		return DataManager.checkIfAccountExists(username);
	}

	
}
