package com.qrequest.ui;

import java.util.prefs.Preferences;

import com.qrequest.object.Credentials;

/**This class sets and retrieves the current set theme. Also provides shortcuts to the themes.
*/
public class CredentialsHelper {
	
	
	/**Preferences instance.
	*/
	private final static Preferences prefs = Preferences.userNodeForPackage(com.qrequest.ui.CredentialsHelper.class);
	
	
	private final static String ARE_CREDS_SAVED_PREF_NAME = "areCredsSaved";
	private final static String SAVED_USERNAME_PREF_NAME = "savedUsername";
	private final static String SAVED_PASSWORD_PREF_NAME = "savedPassword";
	
	

	
	/**Retrieves the saved theme from the settings and sets the <code>darkModeEnabled</code> static boolean.
	*/
	static Credentials retrieveCredentials() {
		String username = "";
		String password = "";
		String areCredsSaved = "";
		areCredsSaved = prefs.get(ARE_CREDS_SAVED_PREF_NAME, areCredsSaved);
		
		if(areCredsSaved.equals("true")) {
			username = prefs.get(SAVED_USERNAME_PREF_NAME, username);
			password = prefs.get(SAVED_PASSWORD_PREF_NAME, password);
			
			return (new Credentials(username, password));
		}
		return null;
	}
	
	/**Save the saved theme to the settings and sets the <code>darkModeEnabled</code> static boolean.
	 * @param isDarkModeEnabled New dark mode state.
	*/
	static void saveCredentials(Credentials creds) {
		
		prefs.put(ARE_CREDS_SAVED_PREF_NAME, "true");
		prefs.put(SAVED_USERNAME_PREF_NAME, creds.getUsername());
		prefs.put(SAVED_PASSWORD_PREF_NAME, creds.getPassword());
	}
	
	static void removeCredentials(Credentials creds) {
		
		prefs.put(ARE_CREDS_SAVED_PREF_NAME, "false");
	}
}