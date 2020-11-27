package com.qrequest.objects;

import java.util.Date;
import java.util.UUID;

/**Represents a question.
 */
public class Question extends Post {
	
	/**The question or the question's title.
	 */
	private String title;
	
	
	/**Builds an Answer object without a date for putting into the database, which assigns the time posted.
	 * @param title The question or the question's title.
	 * @param description The question or the question's title.
	 * @param author The user who asked the question.
	 * @param uniqueID Unique question identifier
	 */
	public Question(String title, String description, User author, UUID id) {
		super(description, author, id);
		this.title = title;
	}
	
	/**Builds an Answer object with a date for when retrieving the questionff from the database.
	 * @param title The question or the question's title.
	 * @param description The question or the question's title.
	 * @param author The user who asked the question.
	 * @param uniqueID Unique question identifier
	 * @param timePosted The time when the question was posted.
	 */
	public Question(String title, String description, User author, UUID id, Date timePosted, int votes, int currentUserVote) {
		super(description, author, id, timePosted, votes, currentUserVote);
		this.title = title;
	}
	
	public Question(UUID id) {
		super(id);
	}
	
	
	/**Returns the question's title.
	 * @return The question's title.
	 */
	public String getTitle() {
		return title;
	}
}
