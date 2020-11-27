package com.qrequest.control;

import com.qrequest.objects.Vote;

public abstract class VoteControl {
	public static void addVote(Vote vote) {
		DataManager.addVote(vote);
	}
}
