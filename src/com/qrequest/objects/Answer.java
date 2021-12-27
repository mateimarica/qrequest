package com.qrequest.objects;

import org.json.JSONObject;

/**Represents an Answer object*/
public class Answer extends Post{
	
	/**The question that this answer is posted to.*/
	private Question question;
	
	/**Create a question with only an ID.
	 * @param id The unique UUID.
	 */
	public Answer(String id) {
		super(id);
	}
	
	/**Create an Answer object for the first time, used in PopupUI when creating a question.
	 * @param answer The answer as a <code>String</code>.
	 * @param answerer The <code>User</code> who created this answer.
	 * @param question The <code>Question</code> that this <code>Answer</code> belongs to.
	 * @param id The <code>UUID</code> unique identifier.
	 */
	public Answer(String answer, User author, Question question) {
		super(answer, author);
		this.question = question;
	}
	
	/**Create an Answer object when retrieving from the database, used in DataManager.
	 * @param answer The answer as a <code>String</code>.
	 * @param answerer The <code>User</code> who created this answer.
	 * @param question The <code>Question</code> that this <code>Answer</code> belongs to.
	 * @param id The <code>UUID</code> unique identifier.
	 * @param timePosted The <code>java.util.Date</code> that represents when the <code>Answer</code> was posted.
	 * @param votes The number of votes this Answer has.
	 * @param currentUserVote The current user's vote on this <code>Answer</code>, can be -1, 0, +1
	 */
	public Answer(String answer, User author, Question question, String id, TeiTime timePosted, int votes, int currentUserVote) {
		super(answer, author, id, timePosted, votes, currentUserVote);
		this.question = question;
	}

	public static Answer fromJson(JSONObject json, Question question) {
		return new Answer(
			(String) json.get("answer"),
			new User((String) json.get("author")), 
			question,
			(String) json.get("id"),
			new TeiTime((String) json.get("dateCreated")), 
			(int) json.get("votes"), 
			(int) json.get("currentUserVote")
		);
	}
	
	/**Returns the <code>question</code> that this is the answer to.
	 * @return The <code>question</code> that this is the answer to.
	 */
	public Question getQuestion() {
		return question;
	}	
	
}