package com.qrequest.objects;

/**Represents a tag on a Question.*/
public enum Tag {
	SCIENCE("Science", "\u269B"),
	COMPUTER_SCIENCE("Computer Science", "\uD83D\uDCBB"),
	LITERATURE("Literature", "\uD83D\uDD6E"),
	SPORTS("Sports", "\u26BD"),
	MUSIC("Music", "\uD834\uDD60"),
	FITNESS("Fitness", "\uD83D\uDCAA"),
	ANATOMY("Human Anatomy", "\u267F"),
	FOOD("Food", "\uD83C\uDF4C"),
	VIDEO_GAMES("Video Games", "\uD83C\uDFAE"),
	NO_TAG("No tag", "");
	
	/**The tag's description.*/
	private String description;
	
	/**The tag's symbol.*/
	private String symbol;
	
	/**Creates a new tag.
	 * @param description The tag's description.
	 * @param symbol The tag's symbol.
	 */
	private Tag(String description, String symbol) {
		this.description = description;
		this.symbol = symbol;
	}
	
	/**Returns the tag's description.
	 * @return The tag's description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**Returns the tag's symbol.
	 * @return The tag's symbol.
	 */
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String toString() {	

		return symbol + " " + description;
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
