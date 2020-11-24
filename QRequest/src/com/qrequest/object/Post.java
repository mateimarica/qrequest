package com.qrequest.object;

import java.util.Date;
import java.util.UUID;

public abstract class Post {
	
	
	private UUID id; //unique ID, invisible to user
	
	/**The time at which the question was posted.
	 */
	private TeiTime timePosted = new TeiTime();
	
	/**The answer to a question.
	 */
	private String description;
	
	private User author;
	
	protected Post(UUID id) {
		this.id = id;
	}
	
	protected Post(String description, User author, UUID id, Date timePosted) {
		this.description = description;
		this.author = author;
		this.id = id;
		this.timePosted.setTimeInMillis(timePosted.getTime());
	}
	
	protected Post(String description, User author, UUID id) {
		this.description = description;
		this.author = author;
		this.id = id;
	}
	
	
	
	/**Returns time when the answer was posted.
	 * @return time when the answer was posted.
	 */
	public TeiTime getTimePosted() {
		return timePosted;
	}
	
	public User getAuthor() {
		return author;
	}
	
	/**Returns the answer's unique ID.
	 * @return the answer's unique ID.
	 */
	public String getID() {
		return id.toString();
	}
	
	/**Returns the answer text.
	 * @return The answer text.
	 */
	public String getDescription() {
		return description;
	}
	
	public void updateDescription(String description) {
		this.description = description;
	}
}
