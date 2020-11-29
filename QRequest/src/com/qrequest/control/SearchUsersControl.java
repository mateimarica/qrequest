package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.objects.User;

/**Control class for searching for users*/
public abstract class SearchUsersControl {

	/**Gets all the Users whose usernames contain the given string.
	 * @param username A string that will be matched to usernames in the database.
	 * @return <code>ArrayList&ltUser&gt</code>
	 */
	public static ArrayList<User> getUsers(String username) {
		return DataManager.getUsers(username);
	}
}
