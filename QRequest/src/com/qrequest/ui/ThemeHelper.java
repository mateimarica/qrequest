package com.qrequest.ui;

import java.util.prefs.Preferences;

/**This class sets and retrieves the current set theme. Also provides shortcuts to the themes.
*/
public class ThemeHelper {
	
	/**The state of the dark mode. Accessible from anywhere in UI package.
	*/
	static boolean darkModeEnabled;
	
	/**Relative URL for the theme within the project.
	*/
	final static String lightThemeFileURL = "/com/qrequest/ui/resources/light-theme.css",
						darkThemeFileURL =  "/com/qrequest/ui/resources/dark-theme.css";
	
	/**Preferences instance.
	*/
	private final static Preferences prefs = Preferences.userNodeForPackage(com.qrequest.ui.ThemeHelper.class);
	
	/**The preference key code for the dark mode setting. Used to retrieve the value.
	*/
	private final static String DARK_MODE_ENABLED_PREF_NAME = "darkModeEnabled";
	
	/**Retrieves the saved theme from the settings and sets the <code>darkModeEnabled</code> static boolean.
	*/
	static void retrieveTheme() {
		String value = "";
		value = prefs.get(DARK_MODE_ENABLED_PREF_NAME, value);
		darkModeEnabled = value.equals("true") ? true : false;
	}
	
	/**Save the saved theme to the settings and sets the <code>darkModeEnabled</code> static boolean.
	 * @param isDarkModeEnabled New dark mode state.
	*/
	static void saveTheme(boolean isDarkModeEnabled) {
		darkModeEnabled = isDarkModeEnabled;
		String newValue = isDarkModeEnabled + "";
		prefs.put(DARK_MODE_ENABLED_PREF_NAME, newValue);
	}
}