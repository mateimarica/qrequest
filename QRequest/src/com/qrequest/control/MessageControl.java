package com.qrequest.control;

import java.util.ArrayList;

public abstract class MessageControl {
	public static ArrayList<String> processAllFilteredMessages(String userID) {
		return DataManager.getAllFilteredMessages(userID);
	}
	
	
}
