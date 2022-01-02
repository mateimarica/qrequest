package com.qrequest.ui;

import com.qrequest.control.UserController;
import com.qrequest.managers.LanguageManager;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**The UI for searching for users.*/
public class SearchUsersUI {
	
	/**Button to search for users*/
	private Button searchUsersBtn;
	
	/**Create a SearchUsersUI. This creates a searchUsersBtn.*/
	public SearchUsersUI() {
		searchUsersBtn = new Button("\uD83D\uDC64 " + LanguageManager.getString("searchUsersToolbarButton"));
		searchUsersBtn.setOnAction(e -> searchUsersBtnPress());
	}
	
	/**Returns the button to search for users.
	 * @return The button to search for users
	 */
	public Button getSearchUsersButton() {
		return searchUsersBtn;
	}
	
	/**Called when the Search Users button is pressed. Triggers a pop-up window*/
	private void searchUsersBtnPress() {
		displaySearchUsersDialog();
	}
	
	/**Displays the search menu. Allows a user to find all users whose names contain a query.<br>
	 * Example: <i><b>a</b></i> will display <i><b>Almond, matei, fedora</b></i><br>
	 * Example: <i><b>ate</b></i> will display <i><b>mAtEi</b></i>
	 */
	private void displaySearchUsersDialog() {		
		// Create the custom dialog.
		Dialog dialog = new Dialog();
		dialog.setTitle(LanguageManager.getString("searchUsersPopupTitle"));
		
		PopupUI.setupDialogStyling(dialog);		

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		// top, right, bottom, left padding
		gridPane.setPadding(new Insets(20, 10, 10, 10));
		
		ListView<String> usersView = new ListView<String>();
		
		TextField searchField = new TextField();
		searchField.setOnAction(e->searchButtonPress(usersView, searchField));
		searchField.setPromptText(LanguageManager.getString("searchUsersFieldPrompt"));
		searchField.setMinWidth(190);
		searchField.setMaxWidth(190);
		
		Button searchUsersBtn = new Button(LanguageManager.getString("searchUsersButton"));
		searchUsersBtn.setOnAction(e->searchButtonPress(usersView, searchField));
		usersView.setOnMouseClicked(event -> {
		    	if (event.getClickCount() == 2) {
					// TODO - Do something when double clicking on user
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
		PopupUI.removeOpenDialog(dialog);
	}
	
	/**Fills up the ListView with the all the users whose usernames match a query.<br>
	 * Triggered by the <b>ENTER</b> key in the search field or the search button.
	 * @param usersView The <b>ListView&ltString&gt</b> that is to be filled up with usernames.
	 * @param searchField The textField containing the search query.
	 */
	private void searchButtonPress(ListView<String> usersView, TextField searchField) {
		usersView.getItems().clear();
		String[] usernameList = UserController.search(searchField.getText());
		if (usernameList != null) {
			for(int i = 0; i< usernameList.length; i++) {
				usersView.getItems().add(usernameList[i]);
			}
		}
	}
}
