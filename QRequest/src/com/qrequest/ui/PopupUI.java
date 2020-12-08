package com.qrequest.ui;

import java.time.Instant;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.qrequest.control.GetQuestionControl;
import com.qrequest.control.MessageControl;
import com.qrequest.control.LoginControl;
import com.qrequest.control.SearchUsersControl;
import com.qrequest.control.ThemeHelper;
import com.qrequest.objects.Answer;
import com.qrequest.objects.Post;
import com.qrequest.objects.Question;
import com.qrequest.objects.QuestionSearchFilters;
import com.qrequest.objects.Report;
import com.qrequest.objects.Tag;
import com.qrequest.objects.User;
import com.qrequest.objects.Message;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import sun.net.www.protocol.mailto.Handler;

/**Class for reducing the amount of boilerplate code when displaying pop-ups.
 */
public class PopupUI {
	
	private static final int MAX_DESC_LENGTH = 65535;
	private static final int MAX_NAME_LENGTH = 10;

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
		
		try {
			return (dialog.showAndWait().get().equals(ButtonType.OK));
		} catch (NoSuchElementException e) {
			return false;
		}
		
	}
		
	/**The dialog for asking a question. Allows user to enter the title and an optional description.<br>
	 * Posts questions upon pressing "OK" and refreshes the question table.
	 * @return The created <code>Question</code> object, or <code>null</code> if the user canceled.
	 */
	public static Question displayAskQuestionDialog() {

		// Create the custom dialog.
		Dialog dialog = new Dialog();
		dialog.setTitle("Post a Question");
		
		setupDialogStyling(dialog);		

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
		descField.setWrapText(true);
		gridPane.add(descField, 0, 1);
		
		ComboBox<Tag> tagTypeBox = new ComboBox<>();	
		tagTypeBox.setPromptText("Select a tag for your question");
		Tag[] tagTypes = Tag.values();
		for(int i = 0; i < tagTypes.length; i++) {
			tagTypeBox.getItems().add(tagTypes[i]);
		}
		gridPane.add(tagTypeBox, 0, 2);
		
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
			}
			if (descField.getText().length() > MAX_DESC_LENGTH) {
			   displayWarningDialog("Error Posting Question", "Questions must be " + MAX_DESC_LENGTH + " characters or fewer.");
			   event.consume(); //make it so the dialog does not close
			   return;
		   }
			
			if(tagTypeBox.getSelectionModel().isEmpty()) {
				 displayWarningDialog("Error Posting Question", "Must select a tag.");
				event.consume(); //make it so the dialog does not close
				return;
			}
		});
		
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContent(gridPane);
		
		if(dialog.showAndWait().get().equals(postQuestionBtnType)) {
			
			return new Question(titleField.getText(), descField.getText(), LoginControl.getUser(), tagTypeBox.getValue());
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
		
		setupDialogStyling(dialog);		

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
		    		System.out.println(usersView.getSelectionModel());
		    	}
		    });
		
		GridPane.setHalignment(searchUsersBtn, HPos.RIGHT);
		gridPane.add(searchUsersBtn, 0, 0);
		gridPane.add(searchField, 0, 0);
		gridPane.add(usersView, 0,1);
		
		
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
		
		DialogPane dialogPane = dialog.getDialogPane();
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
	
	/**Displays a pop-up window that gives the user the option to edit their own post <i>description</i>.
	 * @param post The post being edited.
	 * @return <code>true</code> if the <code>Post</code> object's description was edited, <code>false</code> if the user canceled.
	 */
	public static boolean displayEditPostDialog(Post post) {

		String postType = post.getPostType();

		// Create the custom dialog.
		Dialog dialog = new Dialog();
		dialog.setTitle("Edit " + postType);

		setupDialogStyling(dialog);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		// top, right, bottom, left padding
		gridPane.setPadding(new Insets(20, 10, 10, 10));

		TextArea descField = new TextArea();
		descField.setText(post.getDescription());
		descField.setMaxSize(400, 400);
		descField.setWrapText(true);
		gridPane.add(descField, 0, 0);
		
		ComboBox<Tag> tagTypeBox = null;
		if(post.isQuestion()) {
			tagTypeBox = new ComboBox<>();	
			tagTypeBox.getSelectionModel().select(((Question)post).getTag());
			Tag[] tagTypes = Tag.values();
			for(int i = 0; i < tagTypes.length; i++) {
				tagTypeBox.getItems().add(tagTypes[i]);
			}
			gridPane.add(tagTypeBox, 0, 1);
		}


		// Set the button types.
		ButtonType confirmBtnType = new ButtonType("Confirm", ButtonData.RIGHT);
		dialog.getDialogPane().getButtonTypes().addAll(confirmBtnType, ButtonType.CANCEL);

		final Button confirmBtn = (Button)dialog.getDialogPane().lookupButton(confirmBtnType);
		confirmBtn.addEventFilter(ActionEvent.ACTION, event -> {

			if (descField.getText().length() > MAX_DESC_LENGTH) {
			   displayWarningDialog("Error Editing " + postType, "Description is too long.");
			   event.consume(); //make it so the dialog does not close
			   return;
		   }
		});

		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContent(gridPane);

		if(dialog.showAndWait().get().equals(confirmBtnType)) {
			post.setDescription(descField.getText());
			
			if(post.isQuestion()) {
				((Question)post).setTag(tagTypeBox.getValue());
			}
			
			return true;
		}
		return false;
	}
	

	public static QuestionSearchFilters displaySearchQuestionsDialog() {

		// Create the custom dialog.
		Dialog dialog = new Dialog();
		dialog.setTitle("Search Questions");

		setupDialogStyling(dialog);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		// top, right, bottom, left padding
		gridPane.setPadding(new Insets(20, 10, 10, 10));

		TextField titleField = new TextField();
		titleField.setPromptText("Keywords");
		titleField.setPrefWidth(300);
		gridPane.add(titleField, 0, 0);
		
		ComboBox<Tag> tagTypeBox = new ComboBox<>();	
		tagTypeBox.setPromptText("Tag...");
		Tag[] tagTypes = Tag.values();
		for(int i = 0; i < tagTypes.length; i++) {
			tagTypeBox.getItems().add(tagTypes[i]);
		}
		gridPane.add(tagTypeBox, 0, 1);
		
		
		ComboBox<String> hasBeenAnsweredBox = new ComboBox<>();
		hasBeenAnsweredBox.setPromptText("Has been answered?");
		hasBeenAnsweredBox.getItems().addAll("Either", "Yes", "No");
		gridPane.add(hasBeenAnsweredBox, 0, 2);
		/*CheckBox hasBeenAnsweredBox = new CheckBox("Has solved answer");
		gridPane.add(hasBeenAnsweredBox, 0, 2);*/

		// Set the button types.
		ButtonType searchBtnType = new ButtonType("Search Questions", ButtonData.RIGHT);
		dialog.getDialogPane().getButtonTypes().addAll(searchBtnType, ButtonType.CANCEL);

		final Button searchBtn = (Button)dialog.getDialogPane().lookupButton(searchBtnType);
		searchBtn.addEventFilter(ActionEvent.ACTION, event -> {

			if (titleField.getText().length() == 0 && tagTypeBox.getSelectionModel().isEmpty() && hasBeenAnsweredBox.getSelectionModel().isEmpty()) {
			   displayErrorDialog("Error Searching", "Must enter title or select a tag.");
			   event.consume(); //make it so the dialog does not close
			   return;
		   }
		});

		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContent(gridPane);

		if(dialog.showAndWait().get().equals(searchBtnType)) {
			return new QuestionSearchFilters(titleField.getText(), tagTypeBox.getValue(), hasBeenAnsweredBox.getValue());
		}
		return null;
	}

	
	/**Displays a pop-up window that gives the option for the user to post an answer to a question.
	 * @param question The question being answered.
	 * @return The created <code>Answer</code> object, or <code>null</code> if the user canceled.
	 */
	public static Answer displayPostAnswerDialog(Question question) {
		// Create the custom dialog.
			Dialog dialog = new Dialog();
			dialog.setTitle("Post an Answer");
			
			setupDialogStyling(dialog);

			GridPane gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			// top, right, bottom, left padding
			gridPane.setPadding(new Insets(20, 10, 10, 10));
			
			TextArea answerField = new TextArea();
			answerField.setPromptText("Your answer");
			answerField.setMaxSize(400, 400);
			answerField.setWrapText(true);
			gridPane.add(answerField, 0, 0);
			
			// Set the button types.
			ButtonType postAnswerBtnType = new ButtonType("Post Answer", ButtonData.RIGHT);
			dialog.getDialogPane().getButtonTypes().addAll(postAnswerBtnType, ButtonType.CANCEL);
			
			final Button postAnswerBtn = (Button)dialog.getDialogPane().lookupButton(postAnswerBtnType);
			postAnswerBtn.addEventFilter(ActionEvent.ACTION, event -> {
				int answerFieldLength = answerField.getText().length();
				
				if (answerFieldLength < 1 || answerFieldLength > MAX_DESC_LENGTH) {
				   displayWarningDialog("Did not post answer", "Answer must be 1 to " + MAX_DESC_LENGTH +  " characters in length.");
				   event.consume(); //make it so the dialog does not close
				   return;
				}
			});
			
			DialogPane dialogPane = dialog.getDialogPane();
			dialogPane.setContent(gridPane);
	
			if(dialog.showAndWait().get().equals(postAnswerBtnType)) {
				return new Answer(answerField.getText(), LoginControl.getUser(), question);
			}
			return null;
	}

	/**Displays a pop-up window that gives the user the option to edit their own post <i>description</i>.
	 * @param post The post being edited.
	 * @return <code>true</code> if the <code>Post</code> object's description was edited, <code>false</code> if the user canceled.
	 */
	public static Report displayReportPostDialog(Post post) {

		Report report = new Report(LoginControl.getUser(), post);
		
		String postType = post.getPostType();
		
		// Create the custom dialog.
		Dialog dialog = new Dialog();
		dialog.setTitle("Report " + postType);

		setupDialogStyling(dialog);
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		// top, right, bottom, left padding
		gridPane.setPadding(new Insets(20, 10, 10, 10));

		
		ComboBox<String> reportTypeBox = new ComboBox<>();	
		reportTypeBox.setPromptText("Reason for report");
		GridPane.setConstraints(reportTypeBox, 0, 0);
		String[] reportTypes = report.getReportTypes();
		for(int i = 0; i < reportTypes.length; i++) {
			reportTypeBox.getItems().add(reportTypes[i]);
		}
		
		TextArea reportField = new TextArea();
		reportField.setPromptText("Detailed report (Optional)");
		reportField.setMaxSize(300, 200);
		reportField.setWrapText(true);
		GridPane.setConstraints(reportField, 0, 1);
		
		
		gridPane.getChildren().addAll(reportTypeBox, reportField);

		// Set the button types.
		ButtonType confirmBtnType = new ButtonType("Send Report", ButtonData.RIGHT);
		dialog.getDialogPane().getButtonTypes().addAll(confirmBtnType, ButtonType.CANCEL);

		final Button confirmBtn = (Button)dialog.getDialogPane().lookupButton(confirmBtnType);
		confirmBtn.addEventFilter(ActionEvent.ACTION, event -> {

			if (reportField.getText().length() > 1000) {
			   displayWarningDialog("Error Reporting " + postType, "Report is too long.");
			   event.consume(); //make it so the dialog does not close
			   return;
			}
			
			if(reportTypeBox.getSelectionModel().isEmpty()) {
				 displayWarningDialog("Error Reporting " + postType, "Must select reason for report.");
				event.consume(); //make it so the dialog does not close
				return;
			}
		});

		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContent(gridPane);
		
		if(dialog.showAndWait().get().equals(confirmBtnType)) {
			report.setReportType(reportTypeBox.getValue());
			report.setDesc(reportField.getText());
			return report;
		}
		return null;
	}
	
	
	
	/**The dialog for sending/receiving message.
	 */
	public static void displayMessageDialog() {
		Dialog dialog = new Dialog();
		dialog.setTitle("Private Messaging");
		
		setupDialogStyling(dialog);
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 10, 10, 10));
		
		/*ListView<String> usersView = new ListView<String>();
		usersView.setMinSize(200, 200);
		usersView.setMaxSize(200, 200);*/
		
		ListView<BorderPane> allMessages = new ListView<>();
		allMessages.setMinSize(200, 200);
		allMessages.setMaxSize(400, 400);
		
		//Button searchUsersBtn = new Button("Search");
		
		TextField searchField = new TextField();
		searchField.setOnKeyReleased(e-> {
			
			String key = e.getCode().toString();
			
			if(searchField.getLength() != 0 && !key.equals("BACK_SPACE") && !key.equals("DELETE")) {
				ArrayList<User> userList = SearchUsersControl.getUsers(searchField.getText());
				
				if(userList.size() != 0) {
					int caretPosition = searchField.getCaretPosition();
					searchField.setText(userList.get(0).getUsername());
					e.consume();
					searchField.selectPositionCaret(caretPosition);
					searchField.selectRange(searchField.getLength(), caretPosition);
				}
			}
			
			
		});
		searchField.setOnAction(e-> {
			refreshMessaging(allMessages, searchField);
   		 	//System.out.println(textList.toString());
   		 	///allMessages.refresh();
		});
		searchField.setPromptText("Search Users");
		searchField.setMinWidth(190);
		searchField.setMaxWidth(190);
		/*Runnable runnable =
			    () -> { 
			    	refreshMessaging(allMessages, searchField); 
			    }
			;
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor() ;
		ses.scheduleWithFixedDelay( runnable , 0L , 3L , TimeUnit.SECONDS ) ;
*/
		
		
		/*class RefreshMessagingTask extends TimerTask {
		    public void run() {
		    	refreshMessaging(allMessages, searchField);
		    }
		}

		// And From your main() method or any other method
		Timer timer = new Timer();
		timer.schedule(, 0, 5000);*/
		
		
		BorderPane composeBox = new BorderPane();
		
		TextField composeField = new TextField();
		composeField.setOnAction(e->{});
		composeField.setPromptText("Compose Message:");
		composeField.setMinWidth(150);
		
		composeBox.setLeft(composeField);
		//searchUsersBtn.setOnAction(e-> {
	    		
	    	
		    //});
		
		Button sendMessageBtn = new Button("Send Message");
		sendMessageBtn.addEventFilter(ActionEvent.ACTION, event -> {
			Message message = new Message(LoginControl.getUser().getUsername(), searchField.getText(), composeField.getText(), null);
			MessageControl.processSendMessage(message);
			Label newLabel = new Label(message.getText());
			BorderPane box = new BorderPane();
			box.setRight(newLabel);
			
			allMessages.getItems().add(box);
		});
		composeBox.setRight(sendMessageBtn);
		
		gridPane.add(searchField, 0, 0);
		//gridPane.add(searchUsersBtn, 1, 0);
		gridPane.add(allMessages, 0, 1);
		gridPane.add(composeBox, 0, 2);
		
		Button refreshbtn = new Button("\uD83D\uDDD8 Refresh");
		refreshbtn.setOnAction(e -> refreshMessaging(allMessages, searchField));
		gridPane.add(refreshbtn, 0, 3);
		//gridPane.add(sendMessageBtn, 1, 2);
		
		//ButtonType postAnswerBtnType = new ButtonType("Post Answer", ButtonData.RIGHT);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
		
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContent(gridPane);
		
		dialog.showAndWait();
		//ses.shutdownNow();
	}

	static void refreshMessaging(ListView<BorderPane> allMessages, TextField searchField) {
		allMessages.getItems().clear();
		ArrayList<Message> messageList = MessageControl.processAllFilteredMessages(searchField.getText());
		
		 	for (int i = 0; i < messageList.size(); i++) {
		 		
		 		Label messageLabel = new Label(messageList.get(i).getText());
		 		messageLabel.setTooltip(new Tooltip(messageList.get(i).getTimeSent().toString()));
		 		//messageLabel.setTextAlignment(TextAlignment.RIGHT);
		 		//messageLabel.setAlignment(Pos.CENTER_RIGHT);
		 		
		 		//FlowPane messagePane = new FlowPane(messageLabel);
		 		//messageLabel.setAlignment(
		 		//		(LoginControl.getUser().getUsername().equals(messageList.get(i).getSender())) ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
		 		
		 		BorderPane box = new BorderPane();
		 		
		 		if((LoginControl.getUser().getUsername().equals(messageList.get(i).getSender()))) {
		 			box.setRight(messageLabel);
		 		} else {
		 			box.setLeft(messageLabel);
		 		}
		 		allMessages.getItems().add(box);
		 		
		 	} 	
	}
	
	
	private static void setupDialogStyling(Dialog dialog) {
		//Set the icon for the popup
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
		    new Image(new PopupUI().getClass().getResource(MainUI.ICON_URL).toString()));
		
		//sets the theme
		dialog.getDialogPane().getStylesheets().add(ThemeHelper.getCurrentThemeURL());
	}
	
}
