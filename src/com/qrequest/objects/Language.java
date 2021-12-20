package com.qrequest.objects;

import java.util.Locale;

/**Represents a language.*/
public enum Language {
	/**Represents the English language.*/
	ENGLISH("en", "English"),
	
	/**Represents the Romanian language.*/
	ROMANIAN("ro", "Român\u0103");
	
	//Add new languages here	
	
	/**The <code>Language</code>'s locale. 
	 * Used when retrieving the <code>.properties</code> file for the current <code>Language</code>.
	 */
	private Locale locale;
	
	/**The language's acronym. Example: "en".<br>
	 * This acronym has to line up with the acronyms 
	 * <a href="https://www.oracle.com/java/technologies/javase/jdk8-jre8-suported-locales.html">listed here.</a>.
	 * See the <b>Language (ISO 639)</b> column.*/
	private String acronym;
	
	/**The language's name. Example: "English".*/
	private String name;
	
	/**Creates a language object.
	 * @param acronym The language's acronym.
	 * @param name The language's name.
	 */
	private Language(String acronym, String name) {
		this.acronym = acronym;
		this.name = name;
		this.locale = new Locale(acronym);
		
	}
	
	/**Returns the language's locale.
	 * @return The language's locale.
	 */
	public Locale getLocale() {
		return locale;
	}
	
	/**Returns the language's acronym.
	 * @return The language's acronym.
	 */
	public String getAcronym() {
		return acronym;
	}
	
	/**Returns the language's name.
	 * @return The language's name.
	 */
	public String getName() {
		return name;
	}	
}