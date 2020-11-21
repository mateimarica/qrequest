package com.qrequest.ui;

import java.util.prefs.Preferences;

import com.qrequest.object.Credentials;

/**This class sets and retrieves the user's saved credentials, so that a user can save their username & password to automatically log in. 
*/
public class CredentialsHelper {
	
	
	/**Preferences instance.
	*/
	private final static Preferences prefs = Preferences.userNodeForPackage(com.qrequest.ui.CredentialsHelper.class);

	/**The static preference names
	 */
	private final static String SAVED_USERNAME_PREF_NAME = "savedUsername",
								SAVED_PASSWORD_PREF_NAME = "savedPassword";
	
	/**Retrieves the credentials saved in the preferences and returns them as a <code>Credentials</code> object.<br>
	 * If no credentials saved, returns <code>null</code>.
	 * @return The credentials.
	*/
	static Credentials retrieveCredentials() {
		String username = "";
		String password = "";
		
		username = prefs.get(SAVED_USERNAME_PREF_NAME, username);
		if(!username.equals("")) {
			password = prefs.get(SAVED_PASSWORD_PREF_NAME, password);
			
			if(!password.equals("")) {
				return (new Credentials(username, password));
			}
		}
		
		return null;
	}
	
	/**Saves the <code>Credentials</code> arguments into the preferences.
	 * @param creds The <code>Credentials</code>  objects that will be saved in the preferences
	*/
	static void saveCredentials(Credentials creds) {
		prefs.put(SAVED_USERNAME_PREF_NAME, creds.getUsername());
		prefs.put(SAVED_PASSWORD_PREF_NAME, creds.getPassword());
	}
	
	
	/**Deletes any saved credentials.
	 */
	static void removeCredentials() {
		prefs.put(SAVED_USERNAME_PREF_NAME, "");
		prefs.put(SAVED_PASSWORD_PREF_NAME, "");
	}
}