package com.qrequest.ui;
import com.qrequest.control.LoginControl;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginUI {
	private Button loginButton;
	private Label titleLabel;
	private Stage window;
	private VBox layout;
	private TextField usernameField;
	private PasswordField passwordField;
	
	public void startScene(Stage stage) {
		window = stage;
		
		titleLabel = new Label();
		titleLabel.setText("QRequest");
		titleLabel.setStyle("-fx-font: 80 \"Lucida Console\";");
		
		usernameField = new TextField();
		usernameField.setMaxWidth(150);
		usernameField.setPromptText("Username");
		
		
		passwordField = new PasswordField();
		passwordField.setMaxWidth(150);
		passwordField.setPromptText("Password");
		
		loginButton = new Button();
		loginButton.setText("Login");
		loginButton.setOnAction(this::loginButtonPress);
		
		//The layout determines how controls are laid out
		layout = new VBox(titleLabel, usernameField, passwordField, loginButton);
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(20);

		Scene loginScene = new Scene(layout, 1000, 700);
		stage.setScene(loginScene);
		stage.show();
	}
	
	void loginButtonPress(ActionEvent event) {
		boolean loginSuccessful = new LoginControl().processLogin(usernameField.getText(), passwordField.getText());
		if(loginSuccessful) {
			System.out.println("Login successful");

			new ForumUI().startScene(window);
		} else {
			System.out.println("Login fail");
		}
		
	}
}
