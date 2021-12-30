package com.qrequest.control;

import com.qrequest.objects.Message;
import com.qrequest.control.Connector.Method;
import com.qrequest.ui.PopupUI;
import org.eclipse.jetty.client.api.ContentResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class MessageController {
	private MessageController() {}

	public static boolean create(Message message) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("recipient", message.getReceiver())
				.put("message", message.getText())
			);

		ContentResponse res = Connector.sendQRequest(Method.POST, "/messages", body);
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

	public static Message[] get(String recipient) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("recipient", recipient)
			);

		ContentResponse res = Connector.sendQRequest(Method.GET, "/messages", body);
		if (res == null) return null;
		switch (res.getStatus()) {
			case 200:
				JSONArray jsonArr = new JSONArray(res.getContentAsString()); 
				Message[] arr = new Message[jsonArr.length()];
				for (int i = 0; i < arr.length; i++) {
					arr[i] = Message.fromJson((JSONObject) jsonArr.get(i));
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
}