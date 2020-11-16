package com.qrequest.control;

public class CreateAccountControl {
	public void processCreateAccount(String username, String password) {
		DataManager.createAccount(username, password);
	}
}
