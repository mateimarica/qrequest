package com.qrequest.ui;

import java.util.ArrayList;

import java.util.UUID;

import com.qrequest.control.DeletePostControl;
import com.qrequest.control.EditQuestionControl;
import com.qrequest.control.LoginControl;
import com.qrequest.control.PostAnswerControl;
import com.qrequest.control.PostQuestionControl;
import com.qrequest.control.SearchUsersControl;
import com.qrequest.control.ThemeHelper;
import com.qrequest.objects.Answer;
import com.qrequest.objects.Post;
import com.qrequest.objects.Question;
import com.qrequest.objects.User;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
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
		scene.getStylesheets().add(ThemeHelper.getCurrentThemeURL());			
		
		//Set the icon for the popup
		Stage stage = (Stage) scene.getWindow();
		stage.getIcons().add(
		    new Image(new PopupUI().getClass().getResource(MainUI.ICON_URL).toString()));
		
		return (dialog.showAndWait().get().equals(ButtonType.OK) ? true : false);
	}
		
	/**The dialog for asking a question. Allows user to enter the title and an optional description.<br>
	 * Posts questions upon pressing "OK" and refreshes the question table.
	 * @return The created <code>Question</code> object, or <code>null</code> if the user canceled.
	 */
	public static Question displayAskQuestionDialog() {

		// Create the custom dialog.
		Dialog dialog = new Dialog();
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
			
			if (titleFieldLength < 8 || titleFieldLength > 250) {
			   displayWarningDialog("Error Posting Question", "Questions must be 8 to 200 characters in length.");
			   event.consume(); //make it so the dialog does not close
			   return;
			} else if (descField.getText().length() > 65535) {
			   displayWarningDialog("Error Posting Question", "Questions must be 65535 characters or fewer.");
			   event.consume(); //make it so the dialog does not close
			   return;
		   }
		});
		
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add(ThemeHelper.getCurrentThemeURL());
		dialogPane.setContent(gridPane);
		
		if(dialog.showAndWait().get().equals(postQuestionBtnType)) {
			return new Question(titleField.getText(), descField.getText(), LoginControl.getUser(), UUID.randomUUID());
		}
		return null;
	}
	
	/**Displays the search menu. Allows a user to find all users whose names contain a query.<br>
	 * Example: <i><b>a</b></i> will display <i><b>Almond, matei, fedora</b></i><br>
	 * Example: <i><b>ate</b></i> will display <i><b>mAtEi</b></i>
	 */
	public static void displaySearchUsersDialog() {

		// Create the custom dialog.
		Dialog dialog = new Dialog();
		dialog.setTitle("Search Users");
		
		//Set the icon for the popup
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
		    new Image(new PopupUI().getClass().getResource(MainUI.ICON_URL).toString()));
		

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		// top, right, bottom, left padding
		gridPane.setPadding(new Insets(20, 10, 10, 10));
		
		ListView<String> usersView = new ListView<String>();
		
		TextField searchField = new TextField();
		searchField.setOnAction(e->searchButtonPress(usersView, searchField));
		searchField.setPromptText("Search Users");
		searchField.setMinWidth(190);
		searchField.setMaxWidth(190);
		
		Button searchUsersBtn = new Button("Search");
		searchUsersBtn.setOnAction(e->searchButtonPress(usersView, searchField));
		usersView.setOnMouseClicked(event -> {
		    	if(event.getClickCount() == 2){
		    		System.out.println("lmao");
		    		System.out.println(usersView.getSelectionModel());
		    	}
		    });
		
		GridPane.setHalignment(searchUsersBtn, HPos.RIGHT);
		gridPane.add(searchUsersBtn, 0, 0);
		gridPane.add(searchField, 0, 0);
		gridPane.add(usersView, 0,1);
		
		
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
		
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add(ThemeHelper.getCurrentThemeURL());
		dialogPane.setContent(gridPane);
		dialog.showAndWait();
	}
	
	/**Fills up the ListView with the all the users whose usernames match a query.<br>
	 * Triggered by the <b>ENTER</b> key in the search field or the search button.
	 * @param usersView The <b>ListView&ltString&gt</b> that is to be filled up with usernames.
	 * @param searchField The textField containing the search query.
	 */
	private static void searchButtonPress(ListView<String> usersView, TextField searchField) {
		usersView.getItems().clear();
		ArrayList<User> list = SearchUsersControl.getUsers(searchField.getText());
		for(int i =0; i<list.size(); i++) {
			usersView.getItems().add(list.get(i).getUsername());
		}
	}
	
	
	/**Displays a pop-up window that gives the option for the user to post an answer to a question.
	 * @param question The question being answered.
	 * @return The created <code>Answer</code> object, or <code>null</code> if the user canceled.
	 */
	public static Answer displayPostAnswerDialog(Question question) {
		// Create the custom dialog.
			Dialog dialog = new Dialog();
			dialog.setTitle("Post an Answer");
			
			//Set the icon for the popup
			Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
			stage.getIcons().add(
			    new Image(new PopupUI().getClass().getResource(MainUI.ICON_URL).toString()));
			

			GridPane gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			// top, right, bottom, left padding
			gridPane.setPadding(new Insets(20, 10, 10, 10));
			
			TextField answerField = new TextField();
			answerField.setPromptText("Answer");
			answerField.setMinWidth(300);
			answerField.setMaxWidth(300);
			gridPane.add(answerField, 0, 0);
			
			// Set the button types.
			ButtonType postAnswerBtnType = new ButtonType("Post Answer", ButtonData.RIGHT);
			dialog.getDialogPane().getButtonTypes().addAll(postAnswerBtnType, ButtonType.CANCEL);
			
			final Button postAnswerBtn = (Button)dialog.getDialogPane().lookupButton(postAnswerBtnType);
			postAnswerBtn.addEventFilter(ActionEvent.ACTION, event -> {
				int answerFieldLength = answerField.getText().length();
				
				if (answerFieldLength < 1 || answerFieldLength > 50) {
				   displayWarningDialog("Did not post answer", "Answer must be 1 to 65535 characters in length.");
				   event.consume(); //make it so the dialog does not close
				   return;
				}
			});
			
			DialogPane dialogPane = dialog.getDialogPane();
			dialogPane.getStylesheets().add(ThemeHelper.getCurrentThemeURL());
			dialogPane.setContent(gridPane);
	
			if(dialog.showAndWait().get().equals(postAnswerBtnType)) {
				return new Answer(answerField.getText(), LoginControl.getUser(), question, UUID.randomUUID());
			}
			return null;
	}
	
	/**Displays a pop-up window that gives the user the option to edit their own question's <i>description</i>.
	 * @param question The question being edited.
	 * @return <code>true</code> if the Question object's description was edited, </code>false</code> if the user canceled.
	 */
	public static boolean displayEditQuestionDialog(Question question) {

		// Create the custom dialog.
		Dialog dialog = new Dialog();
		dialog.setTitle("Edit Question");

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
		titleField.setText(question.getTitle());
		titleField.setMinWidth(300);
		titleField.setMaxWidth(300);
		titleField.setDisable(true);
		gridPane.add(titleField, 0, 0);

		TextArea descField = new TextArea();
		descField.setText(question.getDescription());
		descField.setMaxSize(300, 200);
		gridPane.add(descField, 0, 1);


		// Set the button types.
		ButtonType confirmBtnType = new ButtonType("Confirm", ButtonData.RIGHT);
		dialog.getDialogPane().getButtonTypes().addAll(confirmBtnType, ButtonType.CANCEL);

		final Button confirmBtn = (Button)dialog.getDialogPane().lookupButton(confirmBtnType);
		confirmBtn.addEventFilter(ActionEvent.ACTION, event -> {

			int titleFieldLength = titleField.getText().length();

			if (titleFieldLength < 10 || titleFieldLength > 50) {
			   displayWarningDialog("Error Posting Question", "Questions must be 10 to 50 characters in length.");
			   event.consume(); //make it so the dialog does not close
			   return;
			} else if (descField.getText().length() > 255) {
			   displayWarningDialog("Error Posting Question", "Questions must be 50 characters or fewer.");
			   event.consume(); //make it so the dialog does not close
			   return;
		   }
		});

		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add(ThemeHelper.getCurrentThemeURL());
		dialogPane.setContent(gridPane);
		
		if(dialog.showAndWait().get().equals(confirmBtnType)) {
			question.setDescription(descField.getText());
			return true;
		}
		return false;
	}
	
}
