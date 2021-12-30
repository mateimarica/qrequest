package com.qrequest.control;

import com.qrequest.control.Connector.Method;
import com.qrequest.ui.PopupUI;
import com.qrequest.objects.Vote;
import org.eclipse.jetty.client.api.ContentResponse;
import org.json.JSONObject;

public class VoteController {
	private VoteController() {}

	public static boolean createOrUpdate(Vote vote) {
		JSONObject params = new JSONObject()
			.put("vote", vote.getVote().getValue());

		if (vote.getPost().isQuestion()) 
			params.put("questionId", vote.getPost().getID());
		else 
			params.put("answerId", vote.getPost().getID());

		JSONObject body = new JSONObject()
			.put("params", params);

		ContentResponse res = Connector.sendQRequest(Method.POST, "/votes", body);
		if (res == null) return false;
		switch (res.getStatus()) {
			case 201:
				return true;
			case 404:
				PopupUI.displayErrorDialog("Error", "The associated post could not be found.");
				return false;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return false;
		}
	}

	public static boolean delete(Vote vote) {
		JSONObject params = new JSONObject();

		if (vote.getPost().isQuestion()) 
			params.put("questionId", vote.getPost().getID());
		else 
			params.put("answerId", vote.getPost().getID());

		JSONObject body = new JSONObject()
			.put("params", params);

		ContentResponse res = Connector.sendQRequest(Method.DELETE, "/votes", body);
		if (res == null) return false;
		switch (res.getStatus()) {
			case 201:
				return true;
			case 404:
				PopupUI.displayErrorDialog("Error", "The associated post could not be found.");
				return false;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return false;
		}
	}
}