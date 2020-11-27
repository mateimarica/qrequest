package com.qrequest.control;

import java.util.prefs.BackingStoreException;
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
	
	public static void clearPreference(String ...keys) {
		for(int i = 0; i < keys.length; i++) {
			PREFS.remove(keys[i]);
		}
	}
	
	public static void clearAllPreferences() {
		try {
			PREFS.clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

}
