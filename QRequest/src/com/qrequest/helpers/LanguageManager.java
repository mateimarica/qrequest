package com.qrequest.helpers;


import java.util.Locale;
import java.util.ResourceBundle;

import com.qrequest.objects.Language;
import com.qrequest.ui.MainUI;

public final class LanguageManager {
	
	private LanguageManager() {}
	
	private static ResourceBundle langBundle;
	
	public static ResourceBundle getLangBundle() {
		return langBundle;
	}
	
	public static String getString(String key) {
		return langBundle.getString(key);
	}
	
	public static void loadSavedLanguage() {
		langBundle = getSavedLanguageBundle();
	}
	
	public static ResourceBundle getLanguageBundle(Language lang) {
		return ResourceBundle.getBundle("com.qrequest.resources.lang.labels", lang.getLocale());
	}
	
	private static final String LANGUAGE_PREF = "lang";
	
	public static ResourceBundle getSavedLanguageBundle() {
		String currentLangStr = PreferenceManager.getPreference(LANGUAGE_PREF, "ENGLISH");
		
		Language currentLang = Language.valueOf(currentLangStr);
		
		return getLanguageBundle(currentLang);
	}
	
	
	
	public static void setLanguage(Language lang) {
		PreferenceManager.savePreference(LANGUAGE_PREF, lang.name());
		langBundle = getLanguageBundle(lang);
	}
	
	public static Language getSavedLanguage() {
		return Language.valueOf(
					PreferenceManager.getPreference(LANGUAGE_PREF, "ENGLISH")
				);
	}
	
	
}