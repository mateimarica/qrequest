package com.qrequest.control;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.qrequest.objects.Credentials;
import com.qrequest.ui.MainUI;
import com.qrequest.ui.PopupUI;
import com.qrequest.ui.MainUI.Environment;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.json.JSONObject;

/** Connection wrapper */
public class Connector {

	private static final String API_URL = (MainUI.getEnv() == Environment.PROD ? 
	                                      "https://qr.mateimarica.dev/api" : 
	                                      "https://qr.mateimarica.local:5000/api");
	
	/** Request methods for the HTTP request */
	public enum Method {
		GET, 
		POST,
		PATCH,
		DELETE
	}

	private static HttpClient httpClient;

	static {
		SslContextFactory sslContextFactory = new SslContextFactory();
        httpClient = new HttpClient(sslContextFactory);
        httpClient.setFollowRedirects(false);
	
        try {
			httpClient.start();
		} catch (Exception e) {
			PopupUI.displayErrorDialog("Error", "Could not connect to the QRequest server.");
		}
	}

	
	public static ContentResponse send(Connector.Method method, String endpoint, JSONObject body) {
		body.put("session", UserController.getSession());

		org.eclipse.jetty.client.api.Request request = httpClient.newRequest(API_URL + endpoint)
            .method(method.name())
            .header(HttpHeader.ACCEPT, "application/json")
        	.header(HttpHeader.CONTENT_TYPE, "application/json")
			.content(new StringContentProvider(body.toString()), "application/json")
			.timeout(5, TimeUnit.SECONDS);

		try {
			ContentResponse res = request.send();
			switch(res.getStatus()) {
				case 401:
					Credentials creds = Credentials.getSavedCredentials();
					if (creds != null && UserController.login(creds)) {
						return send(method, endpoint, body);
					}
					UserController.forceLogout();
					PopupUI.displayErrorDialog(
						"Error", 
						"Your session is invalid and the automatic re-login failed.\nPlease log back in manually."
					);
					return null;
				default:
					return res;
			}
		} catch (InterruptedException | ExecutionException e) {
			PopupUI.displayErrorDialog("Error", "The request could not be completed. Message: " + e.getMessage());
			return null;
		} catch (TimeoutException e) {
			PopupUI.displayErrorDialog("Error", "The request timed out. Message: " + e.getMessage());
			return null;
		} 
	}
}

