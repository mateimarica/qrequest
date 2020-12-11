package com.qrequest.objects;

import java.util.Date;
import java.util.UUID;

import com.qrequest.objects.Vote.VoteType;

/**Generic class for a post, could be a question or answer*/
public abstract class Post {
	
	/**Unique ID, invisible to user*/
	private UUID id; 
	
	/**The time at which the question was posted.*/
	private TeiTime timePosted;
	
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
		this.timePosted = new TeiTime();
		this.timePosted.setTimeInMillis(timePosted.getTime());
		this.votes = votes;
		this.currentUserVote = currentUserVote;
	}
	
	/**Builds a post with a description, author, and id.
	 * @param description The question or the question's title.
	 * @param author The user who asked the question.
	 * @param id Unique question identifier
	 */
	protected Post(String description, User author) {
		this.description = description;
		this.author = author;
		this.id = UUID.randomUUID();
	}
	
	/**Returns time when the answer was posted.
	 * @return time when the answer was posted.
	 */
	public TeiTime getTimePosted() {
		return timePosted;
	}
	
	/**Returns the User that created this post.
	 * @return The User that created this post.
	 */
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
	
	/**Sets the description of the post.
	 * @param The new description of the post.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**Returns this post current vote count all added together.<br>
	 * Example: 3 upvotes and 2 downvotes will return a vote of 1.
	 * @param This post's current vote count.
	 */
	public int getVotes() {
		return votes;
	}
	
	/**Returns the vote on this post of the user that is logged in. Possible return values: -1, 0, 1
	 * @return The vote the vote on this post of the user that is logged in.
	 */
	public int getCurrentUserVote() {
		return currentUserVote;
	}
	
	/**Sets the current user's vote on this post.
	 * @param newCurrentUserVote The current user's new vote on the post.
	 */
	public void setCurrentUserVote(VoteType newCurrentUserVote) {
		currentUserVote = newCurrentUserVote.getValue();
	}
	
	/**Whether or not this <code>Post</code> is a <code>Question</code>.
	 * @return <code>true</code> if is a <code>Question</code>, <code>false</code> if not.
	 */
	public boolean isQuestion() {
		return getClass().equals(Question.class);
	}
	
	/**Whether or not this <code>Post</code> is an <code>Answer</code>.
	 * @return <code>true</code> if is an <code>Answer</code>, <code>false</code> if not.
	 */
	public boolean isAnswer() {
		return getClass().equals(Answer.class);
	}
	
	/**Return a string of the subclass's name.
	 * @return "Question" if this post is a <code>Question</code>, "Answer" if this post is an <code>Answer</code>.
	 */
	public String getPostType() {
		return getClass().getSimpleName();
	}
	
	
}
