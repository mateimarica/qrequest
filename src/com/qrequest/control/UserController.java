package com.qrequest.control;

import com.qrequest.objects.Credentials;
import com.qrequest.control.Connector.Method;
import com.qrequest.objects.User;
import com.qrequest.ui.ForumUI;
import com.qrequest.ui.PopupUI;
import com.qrequest.util.JSONUtil;

import org.eclipse.jetty.client.api.ContentResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserController {
	
	/**The current logged-in user.<br><b>null</b> if no user logged-in.*/
	private static User user;

	private static JSONObject session;

	private static ForumUI forumUI;

	private UserController() {}

	/**Saves a question into the database from a question object.
	 * @param question The question being saved.
	 */
	public static boolean register(Credentials creds) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("username", creds.getUsername())
				.put("password", creds.getPassword())
			);

		ContentResponse res = Connector.sendQRequest(Method.POST, "/users/register", body);
		if (res == null) return false;
		switch (res.getStatus()) {
			case 201:
				JSONObject responseSession = (JSONObject) new JSONObject(res.getContentAsString()).get("session");
				String username = (String) responseSession.get("username");
				boolean isAdmin = (boolean) responseSession.get("isAdmin");
				String sessionId = (String) responseSession.get("sessionId");
				setSession(username, sessionId);
				setUser(username, isAdmin);
				return true;
			case 409:
				PopupUI.displayErrorDialog("Error", "A user with that username already exists.");
				return false;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return false;
		}
	}

	public static boolean login(Credentials creds) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("username", creds.getUsername())
				.put("password", creds.getPassword())
			);

		ContentResponse res = Connector.sendQRequest(Method.POST, "/users/login", body);
		if (res == null) return false;
		switch (res.getStatus()) {
			case 200:
				JSONObject responseSession = (JSONObject) new JSONObject(res.getContentAsString()).get("session");
				String username = (String) responseSession.get("username");
				boolean isAdmin = (boolean) responseSession.get("isAdmin");
				String sessionId = (String) responseSession.get("sessionId");
				setSession(username, sessionId);
				setUser(username, isAdmin);
				return true;
			case 404:
				PopupUI.displayErrorDialog("Error", "Username or password is incorrect.");
				return false;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return false;
		}
	}

	public static String[] search(String keyword) {
		return search(keyword, 15);
	}
	
	public static String[] search(String keyword, int maxResultCount) {
		JSONObject body = new JSONObject()
			.put("params", new JSONObject()
				.put("username", keyword)
				.put("maxResultCount", maxResultCount))
			.put("session", getSession());

		ContentResponse res = Connector.sendQRequest(Method.GET, "/users/search", body);
		if (res == null) return null;
		switch (res.getStatus()) {
			case 200:
				JSONArray usernameJsonArray = new JSONArray(res.getContentAsString());
				return JSONUtil.toStringArray(usernameJsonArray);
			case 401:
				PopupUI.displayErrorDialog("Error", "Session has expired. Please log in again.");
				return null;
			default:
				PopupUI.displayErrorDialog("Error", "Status code: " + res.getStatus() + "\nMessage: " + res.getContentAsString());
				return null;
		}
	}

	/**Returns the logged-in user's username.
	 * @return The logged-in user's username.
	 */
	public static User getUser() {
		return user;
	}

	private static void setSession(String username, String sessionId) {
		session = new JSONObject()
			.put("username", username)
			.put("sessionId", sessionId);
	}

	static JSONObject getSession() {
		return session;
	}

	private static void setUser(String username, boolean isAdmin) {
		user = new User(username, isAdmin);
	}

	/**Sets the logged-in user and their session to null. For use when logging out.
	 */
	public static void resetUser() {
		user = null;
		session = null;
	}

	/** Gives the UserController a reference to the forumUI so that it has the ability to force a login. */
	public static void saveForumUI(ForumUI forumUI) {
		UserController.forumUI = forumUI;
	}

	static void forceLogout() {
		Credentials.removeCredentials();
		forumUI.logout();
		forumUI = null;
	}
}