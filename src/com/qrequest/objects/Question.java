package com.qrequest.objects;

import java.util.Date;
import java.util.UUID;

/**Represents a question.*/
public class Question extends Post {
	
	/**The question or the question's title.*/
	private String title;
	
	/**The number of answers this question has.*/
	private int answerCount;
	
	/**True if question is pinned, false if not.*/
	private boolean isPinned;
	
	/**This question's <code>Tag</code>.*/
	private Tag tag;
	
	/**The answer of this question that is marked as having solved it.<br>
	 * <code>null</code> if no answer has been marked as solved.
	 */
	private Answer solvedAnswer;
	
	/**Builds a Question object without a date for putting into the database, which assigns the time posted.
	 * @param title The question or the question's title.
	 * @param description The question or the question's title.
	 * @param author The user who asked the question.
	 * @param id Unique question identifier
	 */
	public Question(String title, String description, User author, Tag tag) {
		super(description, author);
		this.title = title;
		this.tag = tag;
	}
	
	/**Builds a Question object with a date for when retrieving the question from the database.
	 * @param title The question or the question's title.
	 * @param description The question or the question's title.
	 * @param author The user who asked the question.
	 * @param id Unique question identifier
	 * @param timePosted The time when the question was posted.
	 * @param votes The overall vote count of this question.
	 * @param currentUserVote The current user's vote on this <code>Answer</code>, can be -1, 0, +1.
	 * @param answerCount The number of answers this question has.
	 */
	public Question(String title, String description, User author, UUID id, Date timePosted, int votes, int currentUserVote, int answerCount, boolean isPinned, String tagName, Answer solvedAnswer) {
		super(description, author, id, timePosted, votes, currentUserVote);
		this.title = title;
		this.answerCount = answerCount;
		this.isPinned = isPinned;
		this.tag = Tag.getEnum(tagName);
		this.solvedAnswer = solvedAnswer;
	}
	
	/**Builds a question with just an ID
	 * @param id The UUID unique identifier.
	 */
	public Question(UUID id) {
		super(id);
	}
	
	/**Returns number of answers this post has.
	 * @return The number of answers this post has.
	 */
	public int getAnswerCount() {
		return answerCount;
	}
	
	/**Returns the question's title.
	 * @return The question's title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**Whether or not this question is pinned.
	 * @return <code>true</code> if pinned, <code>false</code> if not.
	 */
	public boolean isPinned() {
		return isPinned;
	}
	
	/**Returns this question's tag.
	 * @return This question's tag.
	 */
	public Tag getTag() {
		return tag;
	}
	
	/**Set this question's tag.
	 * @param tag This question's new tag.
	 */
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	/**Get the answer of this question that is marked as solved.
	 * @return The answer of this question that is marked as having solved it.<br>
	 * <code>null</code> if no answer has been marked as solved.
	 */
	public Answer getSolvedAnswer() {
		return solvedAnswer;
	}
	
	/**Whether or not this question has a solved answer.
	 * @return <code>true</code> if this question has a solved answer, <code>false</code> if it doesn't.
	 */
	public boolean hasSolvedAnswer() {
		return solvedAnswer != null;
	}
	
	/**Set the answer of this question that is marked as solved.
	 * @param solvedAnswer The answer of this question that is to be set to having solved this question..
	 */
	public void setSolvedAnswer(Answer solvedAnswer) {
		this.solvedAnswer = solvedAnswer;
	}
}
