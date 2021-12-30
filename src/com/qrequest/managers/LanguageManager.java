package com.qrequest.managers;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import com.qrequest.objects.Language;

/**Manages the changing of languages.*/
public final class LanguageManager {
	
	/**Constructor is private is avoid instantiation. Class should be accessed statically.*/
	private LanguageManager() {}
	
	/**The ResourceBundle of the current, real-time set language.*/
	private static ResourceBundle langBundle;
	
	/**Returns the current language bundle. Don't use this for accessing strings.
	 * Use {@link LanguageManager#getString(String)} instead.
	 * @return The current language bundle.
	 * */
	public static ResourceBundle getLangBundle() {
		return langBundle;
	}
	
	/**Returns the value for the given key in the current language.
	 * @param key The key in the current properties file. Look in the ../resources/lang/labels.properties file for context.
	 * @return Returns the value associated with the given key.
	 */
	public static String getString(String key) {
		try {
			return langBundle.getString(key);
		} catch (MissingResourceException e1) {
			Language currentLang = getSavedLanguage();
			System.err.println("Can't find key " + key + " in " + currentLang.name());
			if (currentLang != Language.ENGLISH) {
				try {
					return ResourceBundle.getBundle("lang/labels", Language.ENGLISH.getLocale()).getString(key);
				} catch (MissingResourceException e2) {
					System.err.println("Can't find key " + key + " in " + Language.ENGLISH.name());
				}
			}
		}
		return "MISSING";
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
		return ResourceBundle.getBundle("lang/labels", lang.getLocale());
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