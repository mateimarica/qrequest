package com.qrequest.objects;

import java.util.Date;
import java.util.UUID;

import com.qrequest.objects.Vote.VoteType;

/**Generic class for a post, could be a question or answer*/
public abstract class Post {
	
	/**unique ID, invisible to user*/
	private UUID id; 
	
	/**The time at which the question was posted.*/
	private TeiTime timePosted = new TeiTime();
	
	/**The description of the post*/
	private String description;
	
	/**The user who created the post.*/
	private User author;
	
	/**The number of votes this post has.*/
	private int votes;
	
	/**The current user's vote on this object, can be -1, 0, +1.*/
	private int currentUserVote;
	
	/**Create a post with only an ID.*/
	protected Post(UUID id) {
		this.id = id;
	}
	
	/**Builds a Post object for use when retrieving from the database.
	 * @param description The question or the question's title.
	 * @param author The user who asked the question.
	 * @param id Unique question identifier
	 * @param timePosted The time when the question was posted.
	 * @param votes The overall vote count of this post.
	 * @param currentUserVote The current user's vote on this <code>Post</code>, can be -1, 0, +1.
	 * @param answerCount The number of answers this post has.
	 */
	protected Post(String description, User author, UUID id, Date timePosted, int votes, int currentUserVote) {
		this.description = description;
		this.author = author;
		this.id = id;
		this.timePosted.setTimeInMillis(timePosted.getTime());
		this.votes = votes;
		this.currentUserVote = currentUserVote;
	}
	
	/**Builds a post with a description, author, and id.
	 * @param description The question or the question's title.
	 * @param author The user who asked the question.
	 * @param id Unique question identifier
	 */
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
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getVotes() {
		return votes;
	}
	
	public int getCurrentUserVote() {
		return currentUserVote;
	}
	
	public void setCurrentUserVote(VoteType newCurrentUserVote) {
		currentUserVote = newCurrentUserVote.value();
	}
	
	public boolean isQuestion() {
		return getClass().equals(Question.class);
	}
	
	public boolean isAnswer() {
		return getClass().equals(Answer.class);
	}
	
	
}
