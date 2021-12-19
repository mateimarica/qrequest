package com.qrequest.helpers;


import java.util.ResourceBundle;

import com.qrequest.objects.Language;

/**Manages the changing of languages.*/
public final class LanguageManager {
	
	/**Constructor is private is avoid instantiation. Class should be accessed statically.*/
	private LanguageManager() {}
	
	/**The ResourceBundle of the current, real-time set language.*/
	private static ResourceBundle langBundle;
	
	/**Returns the current language bundle
	 * @return The current language bundle.
	 * */
	public static ResourceBundle getLangBundle() {
		return langBundle;
	}
	
	/**Returns the value for the given key in the current language. 
	 * If there are many strings to set in your method, it's best to use <code>LanguageManager.getLangBundle()</code>
	 * and save an instance of the <code>ResourceBundle</code> to get the strings from. This avoids overhead and contributes to cleaner code. 
	 * @param key The key in the current properties file. Look in the ../resources/lang/labels.properties file for context.
	 * @return Returns the value associated with the given key.
	 */
	public static String getString(String key) {
		return langBundle.getString(key);
	}
	
	/**This is called when the program first opens. Gets the saved language preference and 
	 * loads the static <code>langBundle</code> language bundle.*/
	public static void loadSavedLanguage() {
		langBundle = getSavedLanguageBundle();
	}
	
	/**Retrieves the language bundle associated with the given <code>Language</code>.
	 * @param lang The language of the sought language bundle.
	 * @return Returns the language bundle associated with the given <code>Language</code>.
	 */
	public static ResourceBundle getLanguageBundle(Language lang) {
		return ResourceBundle.getBundle("com.qrequest.resources.lang.labels", lang.getLocale());
	}
	
	/**The key that the language preference will be saved under.*/
	private static final String LANGUAGE_PREF = "lang";
	
	/**Returns the ResourceBundle that is associated with the saved language preference.
	 * @return The ResourceBundle that is associated with the saved language preference.
	 */
	public static ResourceBundle getSavedLanguageBundle() {
		String currentLangStr = PreferenceManager.getPreference(LANGUAGE_PREF, "ENGLISH");
		
		Language currentLang = Language.valueOf(currentLangStr);
		
		return getLanguageBundle(currentLang);
	}
	
	/**Sets the current application language. Edits the language preference and updates the static language bundle.
	 * @param lang
	 */
	public static void setLanguage(Language lang) {
		PreferenceManager.savePreference(LANGUAGE_PREF, lang.name());
		langBundle = getLanguageBundle(lang);
	}
	
	/**Returns the <code>Language</code> that is saved in the preferences.
	 * @return The <code>Language</code> that is saved in the preferences.
	 */
	public static Language getSavedLanguage() {
		return Language.valueOf(
					PreferenceManager.getPreference(LANGUAGE_PREF, "ENGLISH")
				);
	}
	
	
}