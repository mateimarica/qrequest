package com.qrequest.control;

import com.qrequest.objects.Report;
import com.qrequest.control.Connector;
import com.qrequest.control.Connector.Method;
import com.qrequest.ui.PopupUI;
import org.eclipse.jetty.client.api.ContentResponse;
import org.json.JSONObject;

public class ReportController {
	private ReportController() {}

	public static boolean create(Report report) {
		JSONObject params = new JSONObject()
			.put("type", report.getReportType().name())
			.put("description", report.getDesc());

		if (report.getReportedPost().isQuestion()) 
			params.put("questionId", report.getReportedPost().getID());
		else
			params.put("answerId", report.getReportedPost().getID());

		JSONObject body = new JSONObject()
			.put("params", params) ;

		ContentResponse res = Connector.sendQRequest(Method.POST, "/reports", body);
		if (res == null) return false;
		switch (res.getStatus()) {
			case 201:
				return true;
			case 401:
				PopupUI.displayErrorDialog("Error", "Session has expired. Please log in again.");
				return false;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return false;
		}
	}

}