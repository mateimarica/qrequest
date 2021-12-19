package com.qrequest.control;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Vote;
import com.qrequest.ui.PopupUI;

/**Class for adding votes.*/
public class VoteControl {
	
	/**Add a vote the database. Note: a <code>Vote</code> object contains the <code>Post</code> that it was voted on.
	 * @param vote The vote to be added.
	 */
	public void addVote(Vote vote) {
		try {
			new DataManager().addVote(vote);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
	}
}
