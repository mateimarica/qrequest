package com.qrequest.ui;

/**Starts the MainUI. For some reason, Java doesn't like it if the main method is in MainUI.
 */
public final class UIStarter {
	
	private UIStarter() {}
	
	public static void main(String[] args) {
		  new MainUI().begin(args);
	}
}