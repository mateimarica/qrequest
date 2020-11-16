package com.qrequest.ui;
import java.beans.EventHandler;

import com.qrequest.control.CreateAccountControl;
import com.qrequest.control.LoginControl;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginUI {
	private Button loginButton;
	private Button createAccountButton;
	
	private Label titleLabel;
	private Label newAccountLabel;
	
	private Stage window;
	private GridPane grid;
	private TextField usernameField;
	private PasswordField passwordField;
	
	private final int WINDOW_HEIGHT = 300;
	private final int WINDOW_WIDTH = 500;
	
	enum Mode {LOGIN, CREATE_ACCOUNT};
	private Mode currentMode = Mode.LOGIN;
	
	public void startScene(Stage stage) {
		window = stage;
	
		grid = new GridPane();
		GridPane.setHalignment(grid, HPos.CENTER);
		GridPane.setValignment(grid, VPos.CENTER);
		//top, right, bottom, left padding
		grid.setPadding(new Insets(10, 50, 10, 55));
		grid.setVgap(8);
		grid.setHgap(10);
		grid.setGridLinesVisible(false);
		
		grid.getColumnConstraints().add(new ColumnConstraints(100));
		
		titleLabel = new Label();
		titleLabel.setText("qRequest");
		titleLabel.setStyle("-fx-font: 35px \"Copperplate Gothic Bold\";");
		GridPane.setConstraints(titleLabel, 1, 0);
		
		usernameField = new TextField();
		usernameField.setMaxWidth(150);
		usernameField.setPromptText("Username");
		usernameField.setOnKeyPressed(e -> usernameFieldTyping(e));
		GridPane.setConstraints(usernameField, 1, 1);
		GridPane.setHalignment(usernameField, HPos.CENTER);
		
		passwordField = new PasswordField();
		passwordField.setMaxWidth(150);
		passwordField.setPromptText("Password");
		passwordField.setOnAction(this::passwordFieldEnter);
		passwordField.setOnKeyPressed(e -> passwordFieldTyping(e));
		GridPane.setConstraints(passwordField, 1, 2);
		GridPane.setHalignment(passwordField, HPos.CENTER);
		
		loginButton = new Button();
		loginButton.setText("Login");
		loginButton.setOnAction(this::loginButtonPress);
		loginButton.setDisable(true);
		GridPane.setConstraints(loginButton, 1, 3);
		GridPane.setHalignment(loginButton, HPos.CENTER);
		
		newAccountLabel = new Label();
		newAccountLabel.setText("Don't have an account?");
		GridPane.setConstraints(newAccountLabel, 1, 12);
		GridPane.setHalignment(newAccountLabel, HPos.CENTER);
		
		createAccountButton = new Button();
		createAccountButton.setText("Create Account");
		createAccountButton.setOnAction(this::createAccountButtonPress);
		GridPane.setConstraints(createAccountButton, 1, 13);
		GridPane.setHalignment(createAccountButton, HPos.CENTER);
		
		grid.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton, createAccountButton,
								newAccountLabel);
		
		
		//The layout determines how controls are laid out
		/*layout = new VBox(titleLabel, usernameField, passwordField, loginButton, createAccountButton);
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(10);*/
		 // To align horizontally in the cell
		
		
		

		Scene loginScene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
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
	
	void createAccountButtonPress(ActionEvent event) {
		if(currentMode == Mode.LOGIN) {
			currentMode = Mode.CREATE_ACCOUNT;
			newAccountLabel.setText("Already have an account?");
			createAccountButton.setText("Log in instead");
		} else {
			currentMode = Mode.LOGIN;
			startScene(window);
		}
		
		//new CreateAccountControl().processCreateAccount(usernameField.getText(), passwordField.getText());
	}
	
	//Triggered when enter key is pressed in the password field
	void passwordFieldEnter(ActionEvent event) {
		if(!passwordField.isDisabled()) {
			loginButtonPress(event);
		}
	}
	
	//Disables login button if either fields are empty. 
	//Keep in mind that the KeyEvent is triggered before the key is put into the field 
	void usernameFieldTyping(KeyEvent event) {
		if(usernameField.getLength() <= 1 && event.getCode().toString().equals("BACK_SPACE")) {
			loginButton.setDisable(true);
		} else {
			if(usernameField.getText().equals("") && !passwordField.getText().equals("")) {
				loginButton.setDisable(false);
			}
		}
		
			
	}
	
	//Disables login button if either fields are empty. 
	//Keep in mind that the KeyEvent is triggered before the key is put into the field 
	void passwordFieldTyping(KeyEvent event) {
		if(passwordField.getLength() <= 1 && event.getCode().toString().equals("BACK_SPACE")) {
			loginButton.setDisable(true);
		} else {
			if(!usernameField.getText().equals("") && passwordField.getText().equals("")) {
				loginButton.setDisable(false);
			}
		}
	}
}
