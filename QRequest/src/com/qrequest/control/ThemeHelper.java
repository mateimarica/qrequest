package com.qrequest.control;

/**This class sets and retrieves the current set theme. Also provides shortcuts to the themes.
*/
public class ThemeHelper {
	/**Relative URL for the theme within the project.
	*/
	public final static String lightThemeFileURL = "/com/qrequest/resources/css/light-theme.css",
							   darkThemeFileURL  = "/com/qrequest/resources/css/dark-theme.css";
	
	
	/**The preference key code for the dark mode setting. Used to retrieve the value.
	*/
	private final static String DARK_MODE_ENABLED_PREF = "darkModeEnabled";
	
	public static boolean darkModeEnabled() {
		String value = PreferenceManager.getPreference((DARK_MODE_ENABLED_PREF));
		return (value.equals("true") ? true : false);
	}
	
	/**Save the saved theme to the settings and sets the <code>darkModeEnabled</code> static boolean.
	 * @param isDarkModeEnabled New dark mode state.
	*/
	public static void saveTheme(boolean isDarkModeEnabled) {
		String newValue = isDarkModeEnabled + "";
		PreferenceManager.savePreference(DARK_MODE_ENABLED_PREF, newValue);
	}

	
}