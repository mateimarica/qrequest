package com.qrequest.objects;

import com.qrequest.helpers.PreferenceManager;

/**Represents a username and password pair.*/
public class Credentials {
	
	/**The username.*/
	private String username;
	
	/**The password.*/
	private String password;
	/**Create a <code>Credentials</code> object.
	 * @param username The username.
	 * @param password The password.
	 */
	public Credentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/**Returns the username.
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}
	
	/**Returns the password.
	 * @return The password.
	 */
	public String getPassword() {
		return password;
	}
	
	/* ---------------------------------------------------------------------------
	 * STATIC PART OF THE CLASS BELOW v
	 * ---------------------------------------------------------------------------
	 */
		
	/**The static preference names.
	 */
	private final static String SAVED_USERNAME_PREF = "savedUsername",
								SAVED_PASSWORD_PREF = "savedPassword";
	
	
	/**Retrieves the credentials saved in the preferences and returns them as a <code>Credentials</code> object.<br>
	 * If no credentials saved, returns <code>null</code>.
	 * @return The credentials.
	*/
	public static Credentials getSavedCredentials() {
		String username = "";
		String password = "";
		
		username = PreferenceManager.getPreference(SAVED_USERNAME_PREF);
		if(username != null) {
			password = PreferenceManager.getPreference(SAVED_PASSWORD_PREF);
			if(password != null) {
				return (new Credentials(username, password));
			}
		}
		
		return null;
	}
	
	/**Saves the <code>Credentials</code> arguments into the preferences.
	 * @param creds The <code>Credentials</code>  objects that will be saved in the preferences
	*/
	public static void saveCredentials(Credentials creds) {
		PreferenceManager.savePreference(SAVED_USERNAME_PREF, creds.getUsername());
		PreferenceManager.savePreference(SAVED_PASSWORD_PREF, creds.getPassword());
	}
	
	
	/**Deletes any saved credentials.*/
	public static void removeCredentials() {
		PreferenceManager.clearPreference(SAVED_USERNAME_PREF, SAVED_PASSWORD_PREF);
	}
}
