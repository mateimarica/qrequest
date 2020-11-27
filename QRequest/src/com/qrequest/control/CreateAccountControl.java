package com.qrequest.control;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Credentials;

/**Control class for creating an account.
 */
public abstract class CreateAccountControl {
	
	/**Sends a username & password to the DataManager to create an account.
	 * @param username
	 * @param password
	 * @return <code>true</code> if account is created, <code>false</code> if an account with that username already exists.
	 * @throws DatabaseConnectionException If there is no connection to the database.
	 */
	public static boolean processCreateAccount(Credentials creds) throws DatabaseConnectionException {
		return DataManager.createAccount(creds);
	}
		
}
