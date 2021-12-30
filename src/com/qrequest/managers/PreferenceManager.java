package com.qrequest.managers;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**Class for simple getting/saving/clearing preferences.
 * Preferences are saved as strings.
 * Class is final because it shouldn't be extended.
 */
public final class PreferenceManager {
	
	/**Private constructor to prevent instantiation.*/
	private PreferenceManager() {}
	
	/**Preferences instance.
	*/
	private final static Preferences PREFS = Preferences.userNodeForPackage(com.qrequest.managers.PreferenceManager.class);
	
	/**Returns the value associated with the given preference key.
	 * @param key The preference's key
	 * @param def The value to be return if the preference can't be retrieved.
	 * @return The saved preference as a <code>String</code>
	 */
	public static String getPreference(String key, String def) {
		return PREFS.get(key, def);
	}
	
	/**Returns the value associated with the given preference key.
	 * @param key The preference's key
	 * @return The saved preference as a <code>String</code>, or <code>null</code> if the preference can't be retrieved.
	 */
	public static String getPreference(String key) {
		return PREFS.get(key, null);
	}
	
	/**Save a preference into the database. If no preference exists with the given key, one is created automatically.
	 * @param key The preference's key.
	 * @param value The preference's value.
	 */
	public static void savePreference(String key, String value) {
		PREFS.put(key, value);
	}
	
	/**Clears all the preferences with the given key(s).
	 * @param keys The key(s) of the preference(s) that are to be cleared.
	 */
	public static void clearPreference(String ...keys) {
		for(int i = 0; i < keys.length; i++) {
			PREFS.remove(keys[i]);
		}
	}
	
	/**Clears all the saved preferences*/
	public static void clearAllPreferences() {
		try {
			PREFS.clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

}
