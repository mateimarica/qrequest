package com.qrequest.control;

import java.util.ArrayList;

import com.qrequest.objects.User;

public abstract class SearchUsersControl {

	public static ArrayList<User> getUsers(String username) {
		return DataManager.getUsers(username);
	}
}
