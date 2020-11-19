package com.qrequest.object;

/**Represents a vote on a question or answer (not both).
 */
public class Vote {
	
	/**The question that was voted on.
	 */
	Question question;
	
	/**The answer that was voted on.
	 */
	Answer answer;
	
	/**The upvote (1) or downvote (-1).
	 */
	int vote;
}
