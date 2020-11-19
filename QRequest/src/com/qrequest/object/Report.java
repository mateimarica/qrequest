package com.qrequest.object;

/**Represents a user's report of a qusetion or answer.
 */
public class Report {
	
	/**The question being reported.
	 */
	Question question;
	
	/**The answer being reported.
	 */
	Answer answer;
	
	/**The user who submitted the report.
	 */
	User reporter;
	
	/**The user-submitted reason for the report.
	 */
	String description;
	
	/**Unique ID for identifying the report. Probably should be a UUID.
	 */
	String id;

}
