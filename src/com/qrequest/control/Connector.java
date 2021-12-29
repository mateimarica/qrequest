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
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.json.JSONObject;

/** Connection wrapper */
public class Connector {

	private static final String QR_API_URL = (MainUI.getEnv() != Environment.PROD ? 
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

	static ContentResponse sendQRequest(Method method, String endpoint, JSONObject body) {
		return send(method, QR_API_URL + endpoint, body, "application/json", "application/json", false);
	}

	 static ContentResponse send(Method method, String endpoint, JSONObject body, String acceptHeader, String contentTypeHeader, boolean silentlyFail) {
		if (body != null && UserController.getSession() != null)
			body.put("session", UserController.getSession());

		Request request = httpClient.newRequest(endpoint)
            .method(method.name())
			.timeout(5, TimeUnit.SECONDS);

		if (acceptHeader != null)
			request.header(HttpHeader.ACCEPT, acceptHeader);

		if (body != null && contentTypeHeader != null) {
			request.content(new StringContentProvider(body.toString()), acceptHeader)
			       .header(HttpHeader.CONTENT_TYPE, "application/json");
		} else if (body == null ^ contentTypeHeader == null) {
			throw new NullPointerException("Both or neither the body and contentTypeHeader must be provided");
		}

		try {
			ContentResponse res = request.send();
			switch(res.getStatus()) {
				case 401:
					Credentials creds = Credentials.getSavedCredentials();
					if (creds != null && UserController.login(creds)) {
						return send(method, endpoint, body, acceptHeader, contentTypeHeader, silentlyFail);
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
			if (!silentlyFail)
				PopupUI.displayErrorDialog("Error", "The request could not be completed. Message: " + e.getMessage());
			return null;
		} catch (TimeoutException e) {
			if (!silentlyFail)
				PopupUI.displayErrorDialog("Error", "The request timed out. Message: " + e.getMessage());
			return null;
		} 
	}
}

