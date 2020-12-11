package com.qrequest.control;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Report;
import com.qrequest.ui.PopupUI;

/**Send a report.*/
public class ReportPostControl {
	
	/**Sends a report to the database.
	 * @param report The report.
	 */
	public void reportPost(Report report) {
		try {
			new DataManager().reportPost(report);
		} catch (DatabaseConnectionException e) {
			PopupUI.displayDatabaseConnectionErrorDialog();
		}
	}
	
}
