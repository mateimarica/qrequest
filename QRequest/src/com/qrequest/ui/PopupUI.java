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
		if(ThemeHelper.darkModeEnabled()) {
			Scene scene = dialog.getDialogPane().getScene();
			scene.getStylesheets().add(ThemeHelper.darkThemeFileURL);
		}
			
		
		//Set the icon for the popup
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
		    new Image(new PopupUI().getClass().getResource(MainUI.ICON_URL).toString()));
		
		dialog.show();
	}
	
	
	
	static void displayConfirmDeletionDialog(ForumUI forumUI, Post post) {
		Alert dialog = new Alert(AlertType.CONFIRMATION);
		dialog.setHeaderText(null);
		dialog.setTitle("Confirm Deletion");
		dialog.setContentText("Are you sure you want to delete this " + post.getClass().getSimpleName() + "?"
							+ " This cannot be undone.");
		
		((Button)dialog.getDialogPane().lookupButton(ButtonType.OK)).setOnAction(e -> {
			DeletePostControl.processDeletePost(post);
			
			if(post.getClass().equals(Question.class)) {
				forumUI.processQuestionDeleted();
			} else {
				forumUI.refresh();
			}
		});	
		
		//set dark theme for pop-up
		if(ThemeHelper.darkModeEnabled()) {
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
		
		if(ThemeHelper.darkModeEnabled())
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
				forumUI.refresh();
			}
		});

	}
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
		
		if(ThemeHelper.darkModeEnabled())
			dialogPane.getStylesheets().add(ThemeHelper.darkThemeFileURL);
		
		
		dialogPane.setContent(gridPane);
		dialog.showAndWait();
		
		
	}
	
	private static void searchButtonPress(ListView<String> usersView, TextField searchField) {
		usersView.getItems().clear();
		ArrayList<User> list = SearchUsersControl.getUsers(searchField.getText());
		for(int i =0; i<list.size(); i++) {
			usersView.getItems().add(list.get(i).getUsername());
		}
	}
	
	public static void displayPostAnswerDialog(ForumUI forumUI, Question question) {
		// Create the custom dialog.
			Dialog<Pair<String, String>> dialog = new Dialog<>();
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
			
			if(ThemeHelper.darkModeEnabled())
				dialogPane.getStylesheets().add(ThemeHelper.darkThemeFileURL);
			
			
			dialogPane.setContent(gridPane);

			// Convert the result to a title-description pair when the postQuestion button is clicked.
			dialog.setResultConverter(button -> {
				
				if(button == postAnswerBtnType) {
					return new Pair<>(answerField.getText(), "");
				}
				return null;
				

			});
			
			dialog.showAndWait().ifPresent(result -> {
				if(result != null) {
					String answer = result.getKey();
					
					Answer newAnswer = new Answer(answer, LoginControl.getUser(), question, UUID.randomUUID());
					
					PostAnswerControl.processPostAnswer(newAnswer);
					forumUI.refresh();
				}
			});
	}
	
	public static void displayEditQuestionDialog(ForumUI forumUI, Question question) {

		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
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

		if(ThemeHelper.darkModeEnabled())
			dialogPane.getStylesheets().add(ThemeHelper.darkThemeFileURL);


		dialogPane.setContent(gridPane);

		// Convert the result to a title-description pair when the postQuestion button is clicked.
		dialog.setResultConverter(button -> {

			if(button == confirmBtnType) {
				return new Pair<>(titleField.getText(), descField.getText());
			}
			return null;


		});

		dialog.showAndWait().ifPresent(result -> {
			if(result != null) {
				String title = result.getKey();
				String desc = result.getValue();

				question.updateDescription(title);
				question.updateDescription(desc);
				EditQuestionControl.processEditQuestion(question);
				forumUI.refresh();
			}
		});

	}
	
	

}
