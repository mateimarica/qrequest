package com.qrequest.ui;

import java.util.ArrayList;
import java.util.ResourceBundle;

import com.qrequest.control.MessageController;
import com.qrequest.control.UserController;
import com.qrequest.helpers.LanguageManager;
import com.qrequest.objects.Message;
import com.qrequest.objects.User;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**The messaging UI.*/
public class MessagingUI {
	
	/**The button for displaying the messaging window.*/
	private Button messagingBtn;
	
	/**The compose field, where the user types a message.*/
	private TextField composeField;
	
	/**The search field where the user can search for other users.*/
	private TextField searchField;
	
	/**The ListView where the current conversation is displayed.*/
	private ListView<BorderPane> allMessages;
	
	/**Create a MessagingUI. Creates a messagingBtn.*/
	public MessagingUI() {
		messagingBtn = new Button("\u2709 " + LanguageManager.getString("messagingButton"));
		messagingBtn.setOnAction(e -> messagingBtnPress());
	}
	
	/**Returns the button for displaying the messaging window.
	 * @return The button for displaying the messaging window.
	 */
	public Button getMessagingButton() {
		return messagingBtn;
	}
	
	/**Called when the Send Message button is pressed. Triggers a pop-up window*/
	private void messagingBtnPress() {
		displayMessageDialog();
	}
	
	/**The dialog for sending/receiving message.*/
	private void displayMessageDialog() {
		
		Dialog dialog = new Dialog();
		dialog.setTitle(LanguageManager.getString("messagingTitle"));
		
		PopupUI.setupDialogStyling(dialog);
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 10, 10, 10));
		
		allMessages = new ListView<>();
		allMessages.setPrefHeight(200);
		//allMessages.setMaxSize(400, 400);
		
		searchField = new TextField();
		searchField.setOnKeyReleased(e -> {
			
			String key = e.getCode().toString();
			
			if(searchField.getLength() != 0 && !key.equals("BACK_SPACE") && !key.equals("DELETE")) {
				String[] usernames = UserController.search(searchField.getText(), 1);
				System.out.println("In messaging");
				if(usernames != null &&usernames.length != 0) {
					int caretPosition = searchField.getCaretPosition();
					searchField.setText(usernames[0]);
					e.consume();
					searchField.selectPositionCaret(caretPosition);
					searchField.selectRange(searchField.getLength(), caretPosition);
				}
			}
		});
		searchField.setOnAction(e -> refreshMessaging());		
		searchField.setPromptText(LanguageManager.getString("searchUsersPrompt"));
		searchField.setMinWidth(190);
		searchField.setMaxWidth(190);BorderPane composeBox = new BorderPane();
		
		composeField = new TextField();
		composeField.setOnAction(e -> sendMessage());
		composeField.setPromptText(LanguageManager.getString("composeMessagePrompt"));
		composeField.setMinWidth(150);
		
		composeBox.setLeft(composeField);
		
		Button sendMessageBtn = new Button(LanguageManager.getString("sendMessageButton"));
		sendMessageBtn.setOnAction(e -> sendMessage());
		composeBox.setRight(sendMessageBtn);
		
		gridPane.add(searchField, 0, 0);
		gridPane.add(allMessages, 0, 1);
		gridPane.add(composeBox, 0, 2);
		
		Button refreshbtn = new Button("\uD83D\uDDD8 " + LanguageManager.getString("refreshButton"));
		refreshbtn.setOnAction(e -> refreshMessaging());
		gridPane.add(refreshbtn, 0, 3);
		
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
		PopupUI.removeOpenDialog(dialog);
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContent(gridPane);
		
		dialog.showAndWait();
		PopupUI.removeOpenDialog(dialog);
	}
	
	/**Triggered when the refresh button is clicked.
	 * @param allMessages The ListView where all the messages are displays.
	 * @param searchField The searchField containing the name of the person you're sending the messages to.
	 */
	private void refreshMessaging() {
		allMessages.getItems().clear();
		Message[] messages = MessageController.get(searchField.getText());
		
		 	for (int i = 0; i < messages.length; i++) {
		 		
		 		Label messageLabel = new Label(messages[i].getText());
		 		messageLabel.setTooltip(new Tooltip(messages[i].getTimeSent().toString()));
		 		BorderPane box = new BorderPane();
		 		
		 		if((UserController.getUser().getUsername().equals(messages[i].getSender()))) {
		 			box.setRight(messageLabel);
		 		} else {
		 			box.setLeft(messageLabel);
		 		}
		 		allMessages.getItems().add(box);
		 		
		 	} 	
	}
	
	/**Triggered when the Enter key is pressed when in the compose field, or the <i>Send</i> button is clicked.*/
	private void sendMessage() {
		if(!composeField.getText().isEmpty()) {
			Message message = new Message(UserController.getUser().getUsername(), searchField.getText(), composeField.getText());
			if (MessageController.create(message)) {
				Label newLabel = new Label(message.getText());
				BorderPane box = new BorderPane();
				box.setRight(newLabel);
				
				allMessages.getItems().add(box);
				
				composeField.setText("");
			}
		}
	}
	
	
}
