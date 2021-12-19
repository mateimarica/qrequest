package com.qrequest.helpers;

/**This class sets and retrieves the current set theme. Also provides shortcuts to the themes.
 * Class is final because it shouldn't be extended.
*/
public final class ThemeManager {
	
	/**Private constructor to prevent instantiation.*/
	private ThemeManager() {}
	
	/**Relative URL for the theme within the project.
	*/
	private final static String lightThemeFileURL = "/com/qrequest/resources/css/light-theme.css",
							    darkThemeFileURL  = "/com/qrequest/resources/css/dark-theme.css";
	
	
	/**The preference key code for the dark mode setting. Used to retrieve the value.
	*/
	private final static String DARK_MODE_ENABLED_PREF = "darkModeEnabled";
	
	public static boolean isDarkModeEnabled() {
		String value = PreferenceManager.getPreference(DARK_MODE_ENABLED_PREF, false + "");
		return (value.equals("true") ? true : false);
	}
	
	/**Get the relative URL of the currently set theme.
	 * @return The relative URL of the currently set theme.
	 */
	public static String getCurrentThemeURL() {
		String value = PreferenceManager.getPreference(DARK_MODE_ENABLED_PREF, false + "");
		return (value.equals("true") ? darkThemeFileURL  : lightThemeFileURL);
	}
	
	/**Save the saved theme to the settings and sets the <code>darkModeEnabled</code> static boolean.
	 * @param isDarkModeEnabled New dark mode state.
	*/
	public static void saveTheme(boolean isDarkModeEnabled) {
		String newValue = isDarkModeEnabled + "";
		PreferenceManager.savePreference(DARK_MODE_ENABLED_PREF, newValue);
	}
	
	
}