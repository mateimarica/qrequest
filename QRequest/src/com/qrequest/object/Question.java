package com.qrequest.object;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**Represents a question.
 */
public class Question {
	
	/**Unique question identifier.
	 */
	private UUID id; //unique ID, invisible to user
	
	/**The question or the question's title.
	 */
	private String title;
	
	/**The question's description.
	 */
	private String description;
	
	//private ArrayList<Tag> tags;
	
	/**The user who asked the question.
	 */
	private User author;
	
	//private ArrayList<Vote> votes;
	
	/**The time at which the question was posted.
	 */
	private TeiTime timePosted = new TeiTime();
	
	/**Builds a Question object without a date for putting into the database, which assigns the time posted.
	 * @param title The question or the question's title.
	 * @param description The question or the question's title.
	 * @param author The user who asked the question.
	 * @param uniqueID Unique question identifier
	 */
	public Question(String title, String description, User author, UUID uniqueID) {
		this.title = title;
		this.description = description;
		this.author = author;
		this.id = uniqueID;
	}
	
	/**Builds a Question object with a date for when retrieving the questionff from the database.
	 * @param title The question or the question's title.
	 * @param description The question or the question's title.
	 * @param author The user who asked the question.
	 * @param uniqueID Unique question identifier
	 * @param timePosted The time when the question was posted.
	 */
	public Question(String title, String description, User author, String uniqueID, Date timePosted) {
		this.title = title;
		this.description = description;
		this.author = author;
		this.id = UUID.fromString(uniqueID);
		this.timePosted.setTimeInMillis(timePosted.getTime());
	}
	
	
	/**Returns the question's title.
	 * @return The question's title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**Returns the question's description.
	 * @return The question's description.
	 */
	public String getDescription() {
		return description;
	}
	
	public void updateDescription(String newDesc) {
		description = newDesc;
	}
	
	/**Returns the question's asker.
	 * @return The question's asker.
	 */
	public User getAuthor() {
		return author;
	}
	
	/**Returns time when the question was posted.
	 * @return time when the question was posted.
	 */
	public TeiTime getTimePosted() {
		return timePosted;
	}
	
	/**Returns the question's unique ID.
	 * @return the question's unique ID.
	 */
	public String getID() {
		return id.toString();
	}
	
	
	
}
