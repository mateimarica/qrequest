package com.qrequest.objects;

import java.util.UUID;

import com.qrequest.helpers.LanguageManager;

/**Represents a user's report of a qusetion or answer.
 */
public class Report {

	public enum ReportType {
		NOT_ACTUAL_POST("reportType0"),
		DISRESPECTFUL("reportType1"),
		INCITES_VIOLENCE("reportType2"),
		FALSE_MISLEADING_INFO("reportType3"),
		I_DONT_LIKE_IT("reportType4");
		
		private String propertyName;
		
		private ReportType(String propertyName) {
			this.propertyName = propertyName;
		}
		
		public String getPropertyName() {
			return propertyName;
		}
		
		@Override
		public String toString() {
			return LanguageManager.getString(propertyName);
		}
	}
		
	/**The post being reported.*/
	private Post reportedPost;
	
	/**The user who submitted the report.*/
	private User reporter;
	
	/**The user-submitted reason for the report.*/
	private String reportDesc;
	
	/**A predefined report type chosen in the report dropdown menu.*/
	private ReportType reportType;
	
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
	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}
	
	/**Returns the report's type.
	 * @return The report's type.
	 */
	public ReportType getReportType() {
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
	
	
}
