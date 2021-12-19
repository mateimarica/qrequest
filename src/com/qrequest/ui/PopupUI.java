package com.qrequest.ui;

import java.util.NoSuchElementException;

import com.qrequest.helpers.LanguageManager;
import com.qrequest.helpers.ThemeManager;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**Class for reducing the amount of boilerplate code when displaying pop-ups.
 * Everything is static because this class has no state. No instance variables. 
 * This is an appropriate use of the static keyword.*/
public class PopupUI {
	
	//Not Javadocing these variables.. Pretty self-explanatory.
	
	static final int MIN_ANSWER_DESC_LENGTH = 1;
	
	static final int MAX_DESC_LENGTH = 65535;
	
	static final int MIN_NAME_LENGTH = 3;
	static final int MAX_NAME_LENGTH = 10;
	
	static final int MIN_PASSWORD_LENGTH = 3;
	static final int MAX_PASSWORD_LENGTH = 40;
	
	static final int MIN_QUESTION_LENGTH = 8;
	static final int MAX_QUESTION_LENGTH = 200;
	
	/**Displays a warning dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window. Should describe the warning.
	 * @return <code>true</code> if the <b>OK</b> button is clicked, <code>false</code> if any other button is clicked.
	 */
	public static boolean displayWarningDialog(String title, String text) {
		return displayDialog(AlertType.WARNING, title, text);
	}
	
	/**Displays a information dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window. Should give the user some information.
	 * @return <code>true</code> if the <b>OK</b> button is clicked, <code>false</code> if any other button is clicked.
	 */
	public static boolean displayInfoDialog(String title, String text) {
		return displayDialog(AlertType.INFORMATION, title, text);
	}

	/**Displays an error dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window. Should describe the error.
	 * @return <code>true</code> if the <b>OK</b> button is clicked, <code>false</code> if any other button is clicked.
	 */
	public static boolean displayErrorDialog(String title, String text) {
		return displayDialog(AlertType.ERROR, title, text);
	}

	/**Displays an confirmation dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window. Should ask something like "Are you sure...".
	 * @return <code>true</code> if the <b>OK</b> button is clicked, <code>false</code> if any other button is clicked.
	 */
	public static boolean displayConfirmationDialog(String title, String text) {
		return displayDialog(AlertType.CONFIRMATION, title, text);
	}
	
	/**General method for displaying a dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window.
	 * @return <code>true</code> if the <b>OK</b> button is clicked, <code>false</code> if any other button is clicked.
	 */
	private static boolean displayDialog(AlertType alert, String title, String text) {
		Alert dialog = new Alert(alert);
		dialog.setHeaderText(null);
		dialog.setTitle(title);
		dialog.setContentText(text);
		
		//set dark theme for pop-up
		Scene scene = dialog.getDialogPane().getScene();
		scene.getStylesheets().add(ThemeManager.getCurrentThemeURL());			
		
		//Set the icon for the popup
		Stage stage = (Stage) scene.getWindow();
		stage.getIcons().add(
		    new Image(new PopupUI().getClass().getResource(MainUI.ICON_URL).toString()));
		
		try {
			return (dialog.showAndWait().get().equals(ButtonType.OK));
		} catch (NoSuchElementException e) {
			return false;
		}
		
	}
	
	/**Shortcut method for displaying the database connection dialog. Used in every Control class.
	 * @return <code>true</code> if the <b>OK</b> button is clicked, <code>false</code> if any other button is clicked.
	 */
	public static boolean displayDatabaseConnectionErrorDialog() {
		return displayErrorDialog(LanguageManager.getLangBundle().getString("connectionErrorTitle"), LanguageManager.getLangBundle().getString("connectionError"));
	}
	
	/**Sets the icon and the theme for the dialog window.
	 * @param dialog The dialog window.
	 */
	static void setupDialogStyling(Dialog dialog) {
		//Set the icon for the popup
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
		    new Image(new PopupUI().getClass().getResource(MainUI.ICON_URL).toString()));
		
		//sets the theme
		dialog.getDialogPane().getStylesheets().add(ThemeManager.getCurrentThemeURL());
	}
	
}
