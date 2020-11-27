package com.qrequest.objects;

/**Represents a vote on a question or answer (not both).
 */
public class Vote {
	
	
	public enum VoteType {
		UPVOTE(1), RESET_VOTE(0), DOWNVOTE(-1);
		
		private final int value;
		
		private VoteType(int value) {
			this.value = value;
		}
		
		public int value() {
			return value;
		}
	};
	
	/**The post that was voted on.
	 */
	private Post post;
	
	private User voter;
	
	
	private VoteType vote;
	
	public Vote(Post post, User voter, VoteType vote) {
		this.post = post;
		this.voter = voter;
		this.vote = vote;
	}
	
	
	public Post getPost() {
		return post;
	}
	
	public User getVoter() {
		return voter;
	}
	
	public VoteType getVote() {
		return vote;
	}
}