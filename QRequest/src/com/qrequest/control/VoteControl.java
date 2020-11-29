package com.qrequest.control;

import com.qrequest.objects.Vote;

/**Class for adding votes.*/
public abstract class VoteControl {
	
	/**Add a vote the database. Note: a <code>Vote</code> object contains the <code>Post</code> that it was voted on.
	 * @param vote The vote to be added.
	 */
	public static void addVote(Vote vote) {
		DataManager.addVote(vote);
	}
}
