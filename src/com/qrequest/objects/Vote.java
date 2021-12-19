package com.qrequest.objects;

/**Represents a vote on a <code>Post</code>.
 */
public class Vote {
	
	/**Represents a vote's value.
	 */
	public enum VoteType {
		/**A vote that represents the value +1*/
		UPVOTE(1), 
		
		/**A vote that represents the value 0*/
		RESET_VOTE(0), 
		
		/**A vote that represents the value -1*/
		DOWNVOTE(-1);
		
		/**The VoteType's value.*/
		private final int value;
		
		/**Creates a VoteType
		 * @param value The value of the vote.
		 */
		private VoteType(int value) {
			this.value = value;
		}
		
		/**Returns the VoteType's value. Possible values: -1, 0, 1
		 * @return The VoteType's value.
		 */
		public int getValue() {
			return value;
		}
		
	};
	
	/**The post that was voted on.*/
	private Post post;
	
	/**The user that did the voting.*/
	private User voter;
	
	/**The type of vote that this is.*/
	private VoteType vote;
	
	/**Creates a <code>Vote</code> object.
	 * @param post The post that was voted on.
	 * @param voter The user that did the voting.
	 * @param vote The type of vote that this is.
	 */
	public Vote(Post post, User voter, VoteType vote) {
		this.post = post;
		this.voter = voter;
		this.vote = vote;
		
	}
	
	/**Returns the user that did the voting.
	 * @return the user that did the voting.
	 */
	public Post getPost() {
		return post;
	}
	
	/**Returns the user that did the voting.
	 * @return The user that did the voting.
	 */
	public User getVoter() {
		return voter;
	}
	
	/**Returns the type of vote.
	 * @return The type of vote.
	 */
	public VoteType getVote() {
		return vote;
	}
}