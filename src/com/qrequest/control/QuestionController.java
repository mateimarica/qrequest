package com.qrequest.control;

import com.qrequest.objects.Question;
import com.qrequest.objects.QuestionSearchFilters;
import com.qrequest.control.Connector.Method;
import com.qrequest.control.Connector.Params;
import com.qrequest.ui.PopupUI;
import org.eclipse.jetty.client.api.ContentResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class QuestionController {
	private QuestionController() {}

	public static boolean create(Question question) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("title", question.getTitle())
				.put("description", question.getDescription())
				.put("tag", question.getTag().name())
			);

		ContentResponse res = Connector.sendQRequest(Method.POST, "/questions", body);
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

	public static Question get(String id) {
		Params params = new Params().add("id", id);

		ContentResponse res = Connector.sendQRequest(Method.GET, "/questions", params);
		if (res == null) return null;
		switch (res.getStatus()) {
			case 200:
				return Question.fromJson(new JSONObject(res.getContentAsString()));
			case 404:
				PopupUI.displayErrorDialog("Error", "That question couldn't be found.");
				return null;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return null;
		}
	}

	public static Question[] list() {
		ContentResponse res = Connector.sendQRequest(Method.GET, "/questions/list");
		if (res == null) return null;
		switch (res.getStatus()) {
			case 200:
				JSONArray jsonArr = new JSONArray(res.getContentAsString()); 
				Question[] arr = new Question[jsonArr.length()];
				for (int i = 0; i < arr.length; i++) {
					arr[i] = Question.fromJson((JSONObject) jsonArr.get(i));
				}
				return arr;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return null;
		}
	}

	public static Question[] search(QuestionSearchFilters filters) {
		Params params = new Params().add("hasSolvedAnswer", filters.hasSolvedAnswer().name());

		if (filters.getKeywords() != null)
			params.add("keywords", filters.getKeywords());

		if (filters.getTag() != null)
			params.add("tag", filters.getTag().name());

		ContentResponse res = Connector.sendQRequest(Method.GET, "/questions/search", params);
		if (res == null) return null;
		switch (res.getStatus()) {
			case 200:
				JSONArray jsonArr = new JSONArray(res.getContentAsString()); 
				Question[] arr = new Question[jsonArr.length()];
				for (int i = 0; i < arr.length; i++) {
					arr[i] = Question.fromJson((JSONObject) jsonArr.get(i));
				}
				return arr;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return null;
		}
	}

	public static boolean togglePin(Question question) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("id", question.getID())
			);

		ContentResponse res = Connector.sendQRequest(Method.PATCH, "/questions/toggle-pin", body);
		if (res == null) return false;
		switch (res.getStatus()) {
			case 204:
				return true;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return false;
		}
	}

	public static boolean update(Question question) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("id", question.getID())
				.put("description", question.getDescription())
				.put("tag", question.getTag().name()));

		ContentResponse res = Connector.sendQRequest(Method.PATCH, "/questions", body);
		if (res == null) return false;
		switch (res.getStatus()) {
			case 204:
				return true;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return false;
		}
	}

	public static boolean delete(Question question) {
		Params params = new Params().add("id", question.getID());

		ContentResponse res = Connector.sendQRequest(Method.DELETE, "/questions", params);
		if (res == null) return false;
		switch (res.getStatus()) {
			case 204:
				return true;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return false;
		}
	}

	public static boolean markSolved(Question question, String answerId) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("id", question.getID())
				.put("solvedAnswerId", (answerId == null ? JSONObject.NULL : answerId)));

		ContentResponse res = Connector.sendQRequest(Method.PATCH, "/questions/mark-solved", body);
		if (res == null) return false;
		switch (res.getStatus()) {
			case 204:
				return true;
			default:
				try {
					PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				} catch (Exception e) {}
				return false;
		}
	}
}
