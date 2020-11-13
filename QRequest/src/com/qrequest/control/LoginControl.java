package com.qrequest.control;

import com.qrequest.object.User;

public class LoginControl {
	User user;
	
	public boolean processLogin(String username, String password) {
		user = new DataManager().getAccount(username, password);
		
		return (user != null);
	}
	
	public void processCreateAccount(String username, String password) {
		new DataManager().createAccount(username, password);
		
		//return (user != null);
	}
}
