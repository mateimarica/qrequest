package com.qrequest.objects;

import com.qrequest.ui.SearchQuestionsUI.HasAnswerOption;

/**Represents a set of filters chosen by the user when searching for questions.*/
public class QuestionSearchFilters {
	
	/**The keywords of the search. <code>null</code> if no keywords entered.*/
	private String keywords;
	
	/**The tag selected by the user. <code>null</code> if no tag selected.*/
	private Tag tag;
	
	/**<code>true</code> if the user selected a preferences for having a solved answer, <code>false</code> if not.<br>
	 * <code>null</code> if no keywords entered.*/
	private HasAnswerOption hasAnswerOption;
	
	/**Create a set of filters for a question search.
	 * @param keywords The keywords of the search. <code>null</code> if no keywords entered.
	 * @param tag The tag selected by the user. <code>null</code> if no tag selected.
	 * @param hasSolvedAnswer <code>true</code> if the user selected a preferences for having a solved answer, <code>false</code> if not.
	 * <code>null</code> if no keywords entered.
	 */
	public QuestionSearchFilters(String keywords, Tag tag, HasAnswerOption hasAnswerOption) {
		this.keywords = keywords;
		this.tag = tag;
		this.hasAnswerOption = hasAnswerOption;
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
	 * @return <code>HasAnswerOption.YES</code> if the user selected a preferences for having a solved answer, <code>HasAnswerOption.NO</code> if not.
	 * <code>HasAnswerOption.EITHER</code> if no option selected or if user selected EITHER.
	 */
	public HasAnswerOption hasSolvedAnswer() {
		return (hasAnswerOption != null ? hasAnswerOption : HasAnswerOption.EITHER);
	}
}
