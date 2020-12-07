package com.qrequest.objects;

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
	
	private String description;
	private String symbol;
	
	private Tag(String description, String symbol) {
		this.description = description;
		this.symbol = symbol;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String toString() {	

		return symbol + " " + description;
	}
	
	//Use this instead of Tag.valueOf(Sting arg0)
	public static Tag getEnum(String tagName) {
		if(tagName == null) {
			return Tag.NO_TAG;
		}
		return valueOf(tagName);
	}
	
}
