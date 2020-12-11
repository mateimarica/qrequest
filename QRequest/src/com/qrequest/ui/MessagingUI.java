package com.qrequest.ui;

import java.util.ArrayList;

import com.qrequest.control.LoginControl;
import com.qrequest.control.MessageControl;
import com.qrequest.control.SearchUsersControl;
import com.qrequest.objects.Message;
import com.qrequest.objects.User;

import javafx.event.ActionEvent;
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
	
	/**Create a MessagingUI. Creates a messagingBtn.*/
	public MessagingUI() {
		messagingBtn = new Button("\u2709 Send a Message");
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
		dialog.setTitle("Private Messaging");
		
		PopupUI.setupDialogStyling(dialog);
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 10, 10, 10));
		
		ListView<BorderPane> allMessages = new ListView<>();
		allMessages.setPrefHeight(200);
		//allMessages.setMaxSize(400, 400);
		
		TextField searchField = new TextField();
		searchField.setOnKeyReleased(e-> {
			
			String key = e.getCode().toString();
			
			if(searchField.getLength() != 0 && !key.equals("BACK_SPACE") && !key.equals("DELETE")) {
				ArrayList<User> userList = new SearchUsersControl().getUsers(searchField.getText());
				
				if(userList.size() != 0) {
					int caretPosition = searchField.getCaretPosition();
					searchField.setText(userList.get(0).getUsername());
					e.consume();
					searchField.selectPositionCaret(caretPosition);
					searchField.selectRange(searchField.getLength(), caretPosition);
				}
			}
			
			
		});
		searchField.setOnAction(e-> refreshMessaging(allMessages, searchField));		
		searchField.setPromptText("Search Users");
		searchField.setMinWidth(190);
		searchField.setMaxWidth(190);BorderPane composeBox = new BorderPane();
		
		TextField composeField = new TextField();
		composeField.setOnAction(e->{});
		composeField.setPromptText("Compose Message:");
		composeField.setMinWidth(150);
		
		composeBox.setLeft(composeField);
		
		Button sendMessageBtn = new Button("Send Message");
		sendMessageBtn.addEventFilter(ActionEvent.ACTION, event -> {
			if(!composeField.getText().isEmpty()) {
				Message message = new Message(LoginControl.getUser().getUsername(), searchField.getText(), composeField.getText());
				new MessageControl().processSendMessage(message);
				Label newLabel = new Label(message.getText());
				BorderPane box = new BorderPane();
				box.setRight(newLabel);
				
				allMessages.getItems().add(box);
				
				composeField.setText("");
			}
		});
		composeBox.setRight(sendMessageBtn);
		
		gridPane.add(searchField, 0, 0);
		gridPane.add(allMessages, 0, 1);
		gridPane.add(composeBox, 0, 2);
		
		Button refreshbtn = new Button("\uD83D\uDDD8 Refresh");
		refreshbtn.setOnAction(e -> refreshMessaging(allMessages, searchField));
		gridPane.add(refreshbtn, 0, 3);
		
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
		
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContent(gridPane);
		
		dialog.showAndWait();
	}
	
	/**Triggered when the refresh button is clicked.
	 * @param allMessages The ListView where all the messages are displays.
	 * @param searchField The searchField containing the name of the person you're sending the messages to.
	 */
	private void refreshMessaging(ListView<BorderPane> allMessages, TextField searchField) {
		allMessages.getItems().clear();
		ArrayList<Message> messageList = new MessageControl().processAllFilteredMessages(searchField.getText());
		
		 	for (int i = 0; i < messageList.size(); i++) {
		 		
		 		Label messageLabel = new Label(messageList.get(i).getText());
		 		messageLabel.setTooltip(new Tooltip(messageList.get(i).getTimeSent().toString()));
		 		BorderPane box = new BorderPane();
		 		
		 		if((LoginControl.getUser().getUsername().equals(messageList.get(i).getSender()))) {
		 			box.setRight(messageLabel);
		 		} else {
		 			box.setLeft(messageLabel);
		 		}
		 		allMessages.getItems().add(box);
		 		
		 	} 	
	}
	
	
}
