package com.qrequest.control;

import java.util.prefs.Preferences;

public final class PreferenceManager {
	
	//Do not instantiate this class
	private PreferenceManager() {}
	
	/**Preferences instance.
	*/
	private final static Preferences PREFS = Preferences.userNodeForPackage(com.qrequest.control.PreferenceManager.class);
	
	public static String getPreference(String key) {
		return PREFS.get(key, "");
	}
	
	public static void savePreference(String key, String value) {
		PREFS.put(key, value);
	}

}
