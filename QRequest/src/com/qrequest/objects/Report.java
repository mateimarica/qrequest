package com.qrequest.objects;

import java.util.UUID;

/**Represents a user's report of a qusetion or answer.
 */
public class Report {

	/**The post being reported.
	 */
	private Post reportedPost;
	
	/**The user who submitted the report.
	 */
	private User reporter;
	
	/**The user-submitted reason for the report.
	 */
	private String reportDesc;
	
	/**
	 * 
	 */
	private String reportType;
	
	/**The question being reported.
	 */
	private UUID id;
	
	private TeiTime timeReported;
	
	public Report(User reporter, Post reportedPost) {
		this.reporter = reporter;
		this.reportedPost = reportedPost;
		this.id = UUID.randomUUID();
	}
	
	public void setDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}
	
	public String getDesc() {
		return reportDesc;
	}
	
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	public String getReportType() {
		return reportType;
	}
	
	public User getReporter() {
		return reporter;
	}
	
	public String getID() {
		return id.toString();
	}
	
	public Post getReportedPost() {
		return reportedPost;
	}
	
	private final String REPORT_TYPES[] = {
			"Not an actual %s",
			"%s is disrespectful",
			"%s incites violence",
			"%s contains false or misleading info",
			"I don't like this %s"
	};
	
	public String[] getReportTypes() {
		String[] reportTypes = new String[REPORT_TYPES.length];
		
		for(int i = 0; i < reportTypes.length; i++) {
			reportTypes[i] = String.format(REPORT_TYPES[i], reportedPost.getPostType());
		}
		
		return reportTypes;
	}
	
	
	
}
