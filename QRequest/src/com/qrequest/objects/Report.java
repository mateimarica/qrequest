package com.qrequest.objects;

import java.util.UUID;

/**Represents a user's report of a qusetion or answer.
 */
public class Report {

	/**The post being reported.*/
	private Post reportedPost;
	
	/**The user who submitted the report.*/
	private User reporter;
	
	/**The user-submitted reason for the report.*/
	private String reportDesc;
	
	/**A predefined report type chosen in the report dropdown menu.*/
	private String reportType;
	
	/**The report's id.*/
	private UUID id;
	
	/**The time of the report being sent. Currently usused.*/
	//private TeiTime timeReported;
	
	/**Create a report.
	 * @param reporter The user who submitted the report.
	 * @param reportedPost The post being reported.
 	 */
	public Report(User reporter, Post reportedPost) {
		this.reporter = reporter;
		this.reportedPost = reportedPost;
		this.id = UUID.randomUUID();
	}
	
	/**Set the report's description.
	 * @param reportDesc The report's new description.
	 */
	public void setDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}
	
	/**Return. the report's description.
	 * @return The report's description.
	 */
	public String getDesc() {
		return reportDesc;
	}
	
	/**Set the report's type.
	 * @param reportType The report's new type.
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	/**Returns the report's type.
	 * @return The report's type.
	 */
	public String getReportType() {
		return reportType;
	}
	
	/**Returns the user who sent the report.
	 * @return The user who sent the report.
	 */
	public User getReporter() {
		return reporter;
	}
	
	/**Returns the report's unique id.
	 * @return The report's unique id.
	 */
	public String getID() {
		return id.toString();
	}
	
	/**Returns the post that this report is reporting.
	 * @return The post that this report is reporting.
	 */
	public Post getReportedPost() {
		return reportedPost;
	}
	
	/**A generic list of all the report types.*/
	private final String REPORT_TYPES[] = {
			"Not an actual %s",
			"%s is disrespectful",
			"%s incites violence",
			"%s contains false or misleading info",
			"I don't like this %s"
	};
	
	/**Returns a list of the report types relative to the type of post.
	 * @return A list of the report types relative to the type of post.
	 */
	public String[] getReportTypes() {
		String[] reportTypes = new String[REPORT_TYPES.length];
		
		for(int i = 0; i < reportTypes.length; i++) {
			reportTypes[i] = String.format(REPORT_TYPES[i], reportedPost.getPostType());
		}
		
		return reportTypes;
	}
	
	
	
}
