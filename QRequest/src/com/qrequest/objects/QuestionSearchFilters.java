package com.qrequest.objects;

/**Represents a set of filters chosen by the user when searching for questions.*/
public class QuestionSearchFilters {
	
	/**The keywords of the search. <code>null</code> if no keywords entered.*/
	private String keywords;
	
	/**The tag selected by the user. <code>null</code> if no tag selected.*/
	private Tag tag;
	
	/**<code>true</code> if the user selected a preferences for having a solved answer, <code>false</code> if not.<br>
	 * <code>null</code> if no keywords entered.*/
	private Boolean hasSolvedAnswer;
	
	/**Create a set of filters for a question search.
	 * @param keywords The keywords of the search. <code>null</code> if no keywords entered.
	 * @param tag The tag selected by the user. <code>null</code> if no tag selected.
	 * @param hasSolvedAnswer <code>true</code> if the user selected a preferences for having a solved answer, <code>false</code> if not.
	 * <code>null</code> if no keywords entered.
	 */
	public QuestionSearchFilters(String keywords, Tag tag, String hasSolvedAnswer) {
		this.keywords = keywords;
		this.tag = tag;
		
		//Bug if you use other language... Yes/No/Either will be something else. String hasSolvedAnswer is a bad implemenation... should fix
		if(hasSolvedAnswer == null) {
			this.hasSolvedAnswer = null;
		} else {
			switch(hasSolvedAnswer) {
				case "Yes":
					this.hasSolvedAnswer = true;
					break;
				case "No":
					this.hasSolvedAnswer = false;
					break;
				case "Either":
				default:
					this.hasSolvedAnswer = null;
			}
		}
	}
	
	/**Returns the search filter's keywords. 
	 * @return The search filter's keywords. <code>null</code> if no keywords entered.
	 */
	public String getKeywords() {
		return keywords;
	}
	
	/**Returns the search filter's tag.
	 * @return The search filter's tag. <code>null</code> if no tag selected.
	 */
	public Tag getTag() {
		return tag;
	}
	
	/**Returns the state of the user having selected a preference for a solved answer in a search.
	 * @return <code>true</code> if the user selected a preferences for having a solved answer, <code>false</code> if not.
	 * <code>null</code> if no keywords entered.
	 */
	public Boolean hasSolvedAnswer() {
		return hasSolvedAnswer;
	}
}
