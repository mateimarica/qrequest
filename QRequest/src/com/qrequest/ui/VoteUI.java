package com.qrequest.ui;

import com.qrequest.control.LoginControl;
import com.qrequest.control.VoteControl;
import com.qrequest.objects.Post;
import com.qrequest.objects.Vote;
import com.qrequest.objects.Vote.VoteType;

import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

/**The UI for voting on a post.*/
public class VoteUI {

	/**The post being voted on.*/
	private Post post;
	
	/**The button for upvoting the post.*/
	private ToggleButton upvoteBtn;
	
	/**The button for downvoting the post.*/
	private ToggleButton downvoteBtn;
	
	/**The label that displays how many votes this post has.*/
	private Label votesLabel;
	
	/**Creates a VoteUI with an upvote button, downvote button, and a votes label.
	 * @param post The post being posted on.
	 */
	public VoteUI(Post post) {
		this.post = post;
		
		votesLabel = new Label(post.getVotes() + "");
		formatVotesLabel(votesLabel, 0);
		
		if(post.getVotes() == 0) {
			votesLabel.setId("votesTickerZero");
		} else if (post.getVotes() > 0) {
			votesLabel.setId("votesTickerPositive");
		} else {
			votesLabel.setId("votesTickerNegative");
		}		
		
		upvoteBtn = new ToggleButton("\u25B2");
		upvoteBtn.setId("upvoteButton");
		
		downvoteBtn = new ToggleButton("\u25BC");
		downvoteBtn.setId("downvoteButton");
		
		if(post.getCurrentUserVote() == 1) {
			upvoteBtn.setSelected(true);
		} else if (post.getCurrentUserVote() == -1) {
			downvoteBtn.setSelected(true);
		}
		
		
		upvoteBtn.setOnAction(e -> upvoteButtonPress());
		downvoteBtn.setOnAction(e -> downvoteButtonPress());
	}
	
	/**Returns the label that displays how many votes this post has.
	 * @return The label that displays how many votes this post has.
	 */
	public Label getVotesLabel() {
		return votesLabel;
	}
	
	/**Returns the button for upvoting the post.
	 * @return The button for upvoting the post.
	 */
	public ToggleButton getUpvoteButton() {
		return upvoteBtn;
	}
	
	/**Returns the button for downvoting the post.
	 * @return The button for downvoting the post.
	 */
	public ToggleButton getDownvoteButton() {
		return downvoteBtn;
	}
	
	/**Triggered when the upvote button is pressed.*/
	private void upvoteButtonPress() {
		if(upvoteBtn.isSelected()) {
			if(downvoteBtn.isSelected()) {
				downvoteBtn.setSelected(false);
				formatVotesLabel(votesLabel, +2);
			} else {
				formatVotesLabel(votesLabel, +1);
			}
			
			
			new VoteControl().addVote(new Vote(post, LoginControl.getUser(), VoteType.UPVOTE));
			post.setCurrentUserVote(VoteType.UPVOTE);
		} else {
			
			formatVotesLabel(votesLabel, -1);
			new VoteControl().addVote(new Vote(post, LoginControl.getUser(), VoteType.RESET_VOTE));
			post.setCurrentUserVote(VoteType.RESET_VOTE);
		}
		
		setVotesLabelColor();
	}
	
	/**Triggered when the downvote button is pressed.*/
	private void downvoteButtonPress() {
		if(downvoteBtn.isSelected()) {
			if(upvoteBtn.isSelected()) {
				upvoteBtn.setSelected(false);
				formatVotesLabel(votesLabel, -2);
			} else {
				formatVotesLabel(votesLabel, -1);
			}
			
			
			new VoteControl().addVote(new Vote(post, LoginControl.getUser(), VoteType.DOWNVOTE));
			post.setCurrentUserVote(VoteType.DOWNVOTE);
		} else {
			
			formatVotesLabel(votesLabel, +1);
			new VoteControl().addVote(new Vote(post, LoginControl.getUser(), VoteType.RESET_VOTE));
			post.setCurrentUserVote(VoteType.RESET_VOTE);
		}
		
		setVotesLabelColor();
	}
	
	/**Sets the votes label votes to green if positive, white if zero, red if negative.*/
	private void setVotesLabelColor() {
		int votes = getVoteCount(votesLabel);
		if(votes == 0) {
			votesLabel.setId("votesTickerZero");
		} else if (votes > 0) {
			votesLabel.setId("votesTickerPositive");
		} else {
			votesLabel.setId("votesTickerNegative");
		}
	}
	
	/**Edits the vote count label for post to display a user's vote immediately.
	 * @param label The vote count Label object.
	 * @param offset How much the vote count changed by.<br>(Example: neutral -> positive = +1)
	 */
	private void formatVotesLabel(Label label, int offset) {
		int labelAsInt = getVoteCount(label);
		label.setText(labelAsInt + offset + ((labelAsInt + offset) < 0 ? "" : " "));
	}
	
	/**Gets the votes down of a vote Label.
	 * @param label The label whose vote count is being gotten.
	 * @return The votes count.
	 */
	private int getVoteCount(Label label) {
		return Integer.parseInt(label.getText().trim());
	}
	
	
}
