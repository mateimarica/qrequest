package com.qrequest.control;

import com.qrequest.objects.Answer;
import com.qrequest.objects.Question;
import com.qrequest.control.Connector.Method;
import com.qrequest.ui.PopupUI;
import org.eclipse.jetty.client.api.ContentResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class AnswerController {
	private AnswerController() {}

	public static boolean create(Answer answer) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("answer", answer.getDescription())
				.put("questionId", answer.getQuestion().getID())
			);

		ContentResponse res = Connector.sendQRequest(Method.POST, "/answers", body);
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

	public static Answer[] get(Question question) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("questionId", question.getID())
			);

		ContentResponse res = Connector.sendQRequest(Method.GET, "/answers", body);
		if (res == null) return null;
		switch (res.getStatus()) {
			case 200:
				JSONArray jsonArr = new JSONArray(res.getContentAsString()); 
				Answer[] arr = new Answer[jsonArr.length()];
				for (int i = 0; i < arr.length; i++) {
					arr[i] = Answer.fromJson((JSONObject) jsonArr.get(i), question);
				}
				return arr;
			case 404:
				PopupUI.displayErrorDialog("Error", "The question couldn't be found.");
				return null;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return null;
		}
	}

	public static boolean update(Answer answer) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("id", answer.getID())
				.put("answer", answer.getDescription()));

		ContentResponse res = Connector.sendQRequest(Method.PATCH, "/answers", body);
		if (res == null) return false;
		switch (res.getStatus()) {
			case 204:
				return true;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return false;
		}
	}

	public static boolean delete(Answer answer) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("id", answer.getID()));

		ContentResponse res = Connector.sendQRequest(Method.DELETE, "/answers", body);
		if (res == null) return false;
		switch (res.getStatus()) {
			case 204:
				return true;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return false;
		}
	}
}