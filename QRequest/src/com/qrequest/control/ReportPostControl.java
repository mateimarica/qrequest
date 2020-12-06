package com.qrequest.control;



import com.qrequest.objects.Report;

public abstract class ReportPostControl {
	
	/**Pins a selected question.
	 */
	public static void reportPost(Report report) {
		DataManager.reportPost(report);
	}
	
}
