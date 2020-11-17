package com.qrequest.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PopupUI {

	static Alert displayWarningDialog(String title, String text) {
		return displayDialog(AlertType.WARNING, title, text);
	}
	
	static Alert displayInfoDialog(String title, String text) {
		return displayDialog(AlertType.INFORMATION, title, text);
	}
	
	static Alert displayErrorDialog(String title, String text) {
		return displayDialog(AlertType.ERROR, title, text);
	}
	
	private static Alert displayDialog(AlertType alert, String title, String text) {
		Alert dialog = new Alert(alert);
		dialog.setHeaderText(null);
		dialog.setTitle(title);
		dialog.setContentText(text);
		dialog.show();
		return dialog;
	}

}
