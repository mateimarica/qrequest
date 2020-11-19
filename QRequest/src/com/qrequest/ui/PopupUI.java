package com.qrequest.ui;

import java.util.UUID;

import com.qrequest.control.LoginControl;
import com.qrequest.control.PostQuestionControl;
import com.qrequest.object.Question;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**Class for reducing the amount of boilerplate code when displaying pop-ups.
 */
public class PopupUI {

	/**Displays a warning dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window. Should describe the warning.
	 */
	public static void displayWarningDialog(String title, String text) {
		displayDialog(AlertType.WARNING, title, text);
	}
	
	/**Displays a information dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window. Should give the user some information.
	 */
	public static void displayInfoDialog(String title, String text) {
		displayDialog(AlertType.INFORMATION, title, text);
	}

	/**Displays an error dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window. Should describe the error.
	 */
	public static void displayErrorDialog(String title, String text) {
		displayDialog(AlertType.ERROR, title, text);
	}

	/**Displays an confirmation dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window. Should ask something like "Are you sure...".
	 */
	public static void displayConfirmationDialog(String title, String text) {
		displayDialog(AlertType.CONFIRMATION, title, text);
	}
	
	/**General method for displaying a dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window.
	 */
	private static void displayDialog(AlertType alert, String title, String text) {
		Alert dialog = new Alert(alert);
		dialog.setHeaderText(null);
		dialog.setTitle(title);
		dialog.setContentText(text);
		
		//set dark theme for pop-up
		if(ThemeHelper.darkModeEnabled) {
			Scene scene = dialog.getDialogPane().getScene();
			scene.getStylesheets().add(ThemeHelper.darkThemeFileURL);
		}
			
		
		//Set the icon for the popup
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
		    new Image(new PopupUI().getClass().getResource(MainUI.ICON_URL).toString()));
		
		dialog.show();
	}
	
	
	/**The dialog for asking a question. Allows user to enter the title and an optional description.<br>
	 * Posts questions upon pressing "OK" and refreshes the question table.
	 * @param forumUI A reference to the ForumUI class.
	 */
	public static void displayPostQuestionDialog(ForumUI forumUI) {

		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Post a Question");
		
		//Set the icon for the popup
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
		    new Image(new PopupUI().getClass().getResource(MainUI.ICON_URL).toString()));
		

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		// top, right, bottom, left padding
		gridPane.setPadding(new Insets(20, 10, 10, 10));
		
		
		TextField titleField = new TextField();
		titleField.setPromptText("Question");
		titleField.setMinWidth(300);
		titleField.setMaxWidth(300);
		gridPane.add(titleField, 0, 0);

		TextArea descField = new TextArea();
		descField.setPromptText("Description (Optional)");
		descField.setMaxSize(300, 200);
		gridPane.add(descField, 0, 1);
		
		
		// Set the button types.
		ButtonType postQuestionBtnType = new ButtonType("Post Question", ButtonData.RIGHT);
		dialog.getDialogPane().getButtonTypes().addAll(postQuestionBtnType, ButtonType.CANCEL);
		
		final Button postQuestionBtn = (Button)dialog.getDialogPane().lookupButton(postQuestionBtnType);
		postQuestionBtn.addEventFilter(ActionEvent.ACTION, event -> {
			
			int titleFieldLength = titleField.getText().length();
			
			if (titleFieldLength < 10 || titleFieldLength > 50) {
			   displayErrorDialog("Error Posting Question", "Questions must be 10 to 50 characters in length.");
			   event.consume(); //make it so the dialog does not close
			   return;
			} else if (descField.getText().length() > 255) {
			   displayErrorDialog("Error Posting Question", "Questions must be 50 characters or fewer.");
			   event.consume(); //make it so the dialog does not close
			   return;
		   }
		});
		
		DialogPane dialogPane = dialog.getDialogPane();
		
		if(ThemeHelper.darkModeEnabled)
			dialogPane.getStylesheets().add(ThemeHelper.darkThemeFileURL);
		
		
		dialogPane.setContent(gridPane);

		// Convert the result to a title-description pair when the postQuestion button is clicked.
		dialog.setResultConverter(button -> {
			
			if(button == postQuestionBtnType) {
				return new Pair<>(titleField.getText(), descField.getText());
			}
			return null;
			

		});
		
		dialog.showAndWait().ifPresent(result -> {
			if(result != null) {
				String title = result.getKey();
				String desc = result.getValue();
				
				Question newQuestion = new Question(title, desc, LoginControl.getUser(), UUID.randomUUID());
				LoginControl.getUser().addQuestion(newQuestion);
				PostQuestionControl.processPostQuestion(newQuestion);
				forumUI.refreshTable();
			}
		});

	}

}
