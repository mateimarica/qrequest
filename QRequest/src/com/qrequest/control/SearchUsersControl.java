package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.User;
import com.qrequest.ui.PopupUI;

/**Control class for searching for users*/
public class SearchUsersControl {
	
	/**Gets all the Users whose usernames contain the given string.
	 * @param username A string that will be matched to usernames in the database.
	 * @return <code>ArrayList&ltUser&gt</code>
	 */
	public ArrayList<User> getUsers(String username) {
		try {
			return new DataManager().getUsers(username);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
		
		return null;
	}
}
