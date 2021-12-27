package com.qrequest.helpers;

import org.json.JSONArray;

public class JSONParser {
	public static String[] toStringArray(JSONArray jsonArr) {
		String[] strArr = new String[jsonArr.length()];
		for (int i = 0; i < strArr.length; i++) {
			strArr[i] = jsonArr.getString(i);
		}
		return strArr;
	}
}
