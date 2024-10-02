package com.qrequest.control;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

	private static final String QR_API_URL = "https://qr.mateimarica.dev/api";
	
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
		return send(method, QR_API_URL + endpoint, body, null, "application/json", "application/json", false);
	}

	static ContentResponse sendQRequest(Method method, String endpoint, Params params) {
		return send(method, QR_API_URL + endpoint, null, params, "application/json", null, false);
	}

	static ContentResponse sendQRequest(Method method, String endpoint) {
		return send(method, QR_API_URL + endpoint, null, null, "application/json", null, false);
	}

	static ContentResponse send(Method method, String endpoint, JSONObject body, Params params, String acceptHeader, String contentTypeHeader, boolean silentlyFail) {

		Request request = httpClient.newRequest(endpoint)
			.method(method.name())
			.timeout(5, TimeUnit.SECONDS);

		if (UserController.getSession() != null) {
			request
				.header("SessionId", UserController.getSession().getString("sessionId"))
				.header("Username", UserController.getSession().getString("username"));
		}

		if (params != null) {
			Iterator<SimpleEntry<String, String>> it = params.getIterator();
			while (it.hasNext()) {
				SimpleEntry<String, String> param = it.next();
				request.param(param.getKey(), param.getValue());
			}
		}

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
						return send(method, endpoint, body, params, acceptHeader, contentTypeHeader, silentlyFail);
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

	public static class Params {
		private List<SimpleEntry<String, String>> params = new ArrayList<>();

		public Params add(String key, String value) {
			params.add(new SimpleEntry<String,String>(key, value));
			return this;
		}

		public Iterator<SimpleEntry<String, String>> getIterator() {
			return params.iterator();
		}
	}
}