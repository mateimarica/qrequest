package com.qrequest.object;

import java.util.Date;
import java.util.UUID;

public class Answer {
	/**Unique answer identifier.
	 */
	private UUID id; //unique ID, invisible to user
	
	/**The answer to a question.
	 */
	private String answer;
	
	private Question question;
	
	/**The answerer.
	 */
	private User answerer;
	
	/**The time at which the question was posted.
	 */
	private TeiTime timePosted = new TeiTime();
	
	
	public Answer(String answer, User answerer, Question question, UUID uniqueID) {
		this.answer = answer;
		this.answerer = answerer;
		this.question = question;
		this.id = uniqueID;
	}
	
	
	public Answer(String answer, User answerer, Question question, String uniqueID, Date timePosted) {
		this.answer = answer;
		this.answerer = answerer;
		this.question = question;
		this.id = UUID.fromString(uniqueID);
		this.timePosted.setTimeInMillis(timePosted.getTime());
	}
	
	/**Returns the answer text.
	 * @return The answer text.
	 */
	public String getAnswer() {
		return answer;
	}
	
	
	public void updateAnswer(String newAnswer) {
		answer = newAnswer;
	}
	
	
	public User getAnswerer() {
		return answerer;
	}
	
	public Question getQuestion() {
		return question;
	}
	
	/**Returns time when the answer was posted.
	 * @return time when the answer was posted.
	 */
	public TeiTime getTimePosted() {
		return timePosted;
	}
	
	/**Returns the answer's unique ID.
	 * @return the answer's unique ID.
	 */
	public String getID() {
		return id.toString();
	}
}
