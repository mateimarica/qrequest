package com.qrequest.objects;

import com.qrequest.control.LoginControl;
import com.qrequest.control.PreferenceManager;
import com.qrequest.exceptions.DatabaseConnectionException;

public class Credentials {
	private String username;
	private String password;
	private boolean isPasswordHashed;
	
	public Credentials(String username, String password, boolean isPasswordHashed) {
		this.username = username;
		this.password = password;
		this.isPasswordHashed = isPasswordHashed;
	}
	
	public Credentials(String username, String password) {
		this.username = username;
		this.password = password;
		this.isPasswordHashed = false;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	/* ---------------------------------------------------------------------------
	 * STATIC PART OF THE CLASS BELOW v
	 * ---------------------------------------------------------------------------
	 */
		
	/**The static preference names
	 */
	private final static String SAVED_USERNAME_PREF = "savedUsername",
								SAVED_PASSWORD_PREF = "savedPassword";
	
	public void hashPassword() throws DatabaseConnectionException {
			password = LoginControl.hashPassword(password);
		
	}
	
	public boolean isPasswordHashed() {
		return isPasswordHashed;
	}
	
	/**Retrieves the credentials saved in the preferences and returns them as a <code>Credentials</code> object.<br>
	 * If no credentials saved, returns <code>null</code>.
	 * @return The credentials.
	*/
	public static Credentials getSavedCredentials() {
		String username = "";
		String password = "";
		
		username = PreferenceManager.getPreference(SAVED_USERNAME_PREF);
		if(!username.equals("")) {
			password = PreferenceManager.getPreference(SAVED_PASSWORD_PREF);
			
			if(!password.equals("")) {
				return (new Credentials(username, password, true));
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
	
	
	/**Deletes any saved credentials.
	 */
	public static void removeCredentials() {
		PreferenceManager.clearPreference(SAVED_USERNAME_PREF, SAVED_PASSWORD_PREF);
	}
}
