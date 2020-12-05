package com.qrequest.ui;

import com.qrequest.control.PreferenceManager;

/**Starts the MainUI. For some reason, Java doesn't like it if the main method is in MainUI.
 */
public abstract class UIStarter {
	public static void main(String[] args) {
		  //PreferenceManager.clearAllPreferences();
		  new MainUI().begin(args);
	}
}