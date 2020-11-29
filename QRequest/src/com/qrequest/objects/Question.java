package com.qrequest.objects;

import java.util.Date;
import java.util.UUID;

/**Represents a question.
 */
public class Question extends Post {
	
	/**The question or the question's title.*/
	private String title;
	
	/**The number of answers this question has.*/
	private int answerCount;
	
	/**Builds a Question object without a date for putting into the database, which assigns the time posted.
	 * @param title The question or the question's title.
	 * @param description The question or the question's title.
	 * @param author The user who asked the question.
	 * @param id Unique question identifier
	 */
	public Question(String title, String description, User author, UUID id) {
		super(description, author, id);
		this.title = title;
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
	public Question(String title, String description, User author, UUID id, Date timePosted, int votes, int currentUserVote, int answerCount) {
		super(description, author, id, timePosted, votes, currentUserVote);
		this.title = title;
		this.answerCount = answerCount;
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
}
