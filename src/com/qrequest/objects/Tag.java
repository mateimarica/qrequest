package com.qrequest.objects;

import com.qrequest.managers.LanguageManager;

/**Represents a tag on a Question.*/

public enum Tag {
	
	SCIENCE("\u269B"),
	COMPUTER_SCIENCE("\uD83D\uDCBB"),
	LITERATURE("\uD83D\uDD6E"),
	SPORTS("\u26BD"),
	MUSIC("\uD834\uDD60"),
	FITNESS("\uD83D\uDCAA"),
	ANATOMY("\u267F"),
	FOOD("\uD83C\uDF4C"),
	VIDEO_GAMES("\uD83C\uDFAE"),
	EIGHTEEN_PLUS("\uD83C\uDF46"),
	CRIME("\uD83D\uDD2B"),
	NO_TAG("");
	
	/**The tag's symbol.*/
	private String symbol;
	
	/**Creates a new tag.
	 * @param description The tag's description.
	 * @param symbol The tag's symbol.
	 */
	private Tag(String symbol) {
		this.symbol = symbol;
	}
	
	/**Returns the tag's symbol.
	 * @return The tag's symbol.
	 */
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String toString() {	

		return symbol + " " + LanguageManager.getString(name());
	}
	
	/**Returns the Tag corresponding to the input string.
	 * Use this method instead of <code>Tag.valueOf(String arg0)</code>
	 * @param tagName The actual tag name. Example: "<b>VIDEO_GAMES</b>"
	 * @return The corresponding tag.
	 */
	public static Tag getEnum(String tagName) {
		if(tagName == null) {
			return Tag.NO_TAG;
		}
		return valueOf(tagName);
	}
	
}
