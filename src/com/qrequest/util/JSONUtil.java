package com.qrequest.util;

import org.json.JSONArray;

public class JSONUtil {
	public static String[] toStringArray(JSONArray jsonArr) {
		String[] strArr = new String[jsonArr.length()];
		for (int i = 0; i < strArr.length; i++) {
			strArr[i] = jsonArr.getString(i);
		}
		return strArr;
	}
}
