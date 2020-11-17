package com.qrequest.ui;

import java.beans.EventHandler;

import org.omg.CORBA.FREE_MEM;

import com.qrequest.control.CreateAccountControl;
import com.qrequest.control.LoginControl;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
	private Button changeModeButton;

	private Label titleLabel;
	private Label newAccountLabel;


	private CheckBox showPasswordCheckBox;

	private Stage window;
	private GridPane grid;
	private TextField usernameField;
	private PasswordField passwordField;
	private TextField unmaskedPasswordField;

	private final int WINDOW_HEIGHT = 300;
	private final int WINDOW_WIDTH = 500;

	private enum Mode {LOGIN, CREATE_ACCOUNT};

	private Mode currentMode = Mode.LOGIN;

	public void startScene(Stage stage) {
		window = stage;

		grid = new GridPane();
		GridPane.setHalignment(grid, HPos.CENTER);
		GridPane.setValignment(grid, VPos.CENTER);
		// top, right, bottom, left padding
		grid.setPadding(new Insets(10, 10, 10, 55));
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
		usernameField.setOnKeyPressed(e -> fieldTyping(e, usernameField, passwordField));
		GridPane.setConstraints(usernameField, 1, 1);
		GridPane.setHalignment(usernameField, HPos.CENTER);

		passwordField = new PasswordField();
		passwordField.setMaxWidth(150);
		passwordField.setPromptText("Password");
		passwordField.setOnAction(this::passwordFieldEnter);
		passwordField.setOnKeyPressed(e -> fieldTyping(e, passwordField, usernameField));
		GridPane.setConstraints(passwordField, 1, 2);
		GridPane.setHalignment(passwordField, HPos.CENTER);

		unmaskedPasswordField = new TextField();
		unmaskedPasswordField.setMaxWidth(150);
		unmaskedPasswordField.setPromptText("Password");
		unmaskedPasswordField.setOnAction(this::passwordFieldEnter);
		unmaskedPasswordField.setOnKeyPressed(e -> fieldTyping(e, unmaskedPasswordField, usernameField));
		unmaskedPasswordField.setVisible(false);
		GridPane.setConstraints(unmaskedPasswordField, 1, 2);
		GridPane.setHalignment(unmaskedPasswordField, HPos.CENTER);

		showPasswordCheckBox = new CheckBox("Show password");
		showPasswordCheckBox.setOnAction(this::showPasswordCheckBoxTicked);
		GridPane.setConstraints(showPasswordCheckBox, 2, 2);

		loginButton = new Button();
		loginButton.setText("Login");
		loginButton.setOnAction(this::loginButtonPress);
		loginButton.setDisable(true);
		GridPane.setConstraints(loginButton, 1, 3);
		GridPane.setHalignment(loginButton, HPos.CENTER);

		newAccountLabel = new Label();
		newAccountLabel.setText("Don't have an account?");
		GridPane.setConstraints(newAccountLabel, 1, 11);
		GridPane.setHalignment(newAccountLabel, HPos.CENTER);

		changeModeButton = new Button();
		changeModeButton.setText("Create Account");
		changeModeButton.setOnAction(this::changeModeButtonPress);
		GridPane.setConstraints(changeModeButton, 1, 12);
		GridPane.setHalignment(changeModeButton, HPos.CENTER);

		grid.getChildren().addAll(titleLabel, usernameField, passwordField, unmaskedPasswordField, showPasswordCheckBox,
				loginButton, changeModeButton, newAccountLabel);

		// The layout determines how controls are laid out
		/*
		 * layout = new VBox(titleLabel, usernameField, passwordField, loginButton,
		 * createAccountButton); layout.setAlignment(Pos.CENTER); layout.setSpacing(10);
		 */
		// To align horizontally in the cell

		Scene loginScene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
		stage.setScene(loginScene);
		stage.show();
	}

	// The "Login" button and the "Create Account" button (not the one at the
	// bottom) are the same buttons
	private void loginButtonPress(ActionEvent event) {
		syncPasswordFields();
		
		if (currentMode == Mode.LOGIN) {
			boolean loginSuccessful = new LoginControl().processLogin(usernameField.getText(), passwordField.getText());

			if (loginSuccessful) {
				new ForumUI().startScene(window);
			} else {
				PopupUI.displayErrorDialog("Login Failed", "Username or password is incorrect.");
			}
		} else {
			if (!areCredentialsValid())
				return;
			
			new CreateAccountControl().processCreateAccount(usernameField.getText(), passwordField.getText());
			currentMode = Mode.LOGIN;
			loginButtonPress(event);
		}
	}

	private boolean areCredentialsValid() {
		String username = usernameField.getText();
		
		if (username.length() < 3 || username.length() > 8) {
			PopupUI.displayWarningDialog("Invalid Username", "Username length must be between 3 and 8.");
			return false;
		}

		if (!username.matches("[A-Za-z0-9]+")) {
			PopupUI.displayWarningDialog("Invalid Username", "Username must be alphanumeric.");
			return false;
		}
		
		String password = passwordField.getText();
		
		if(password.length() < 3) {
			PopupUI.displayWarningDialog("Invalid Password", "Passwords must be at least 3 characters.");
			return false;
		}

		return true;
	}

	// Sets the passwordField's text to the unmaskedPasswordField's text (if it's
	// newer)
	private void syncPasswordFields() {
		if (showPasswordCheckBox.isSelected()) {
			passwordField.setText(unmaskedPasswordField.getText());
		}
	}

	private void changeModeButtonPress(ActionEvent event) {
		if (currentMode == Mode.LOGIN) {
			currentMode = Mode.CREATE_ACCOUNT;
			loginButton.setText("Create Account");
			newAccountLabel.setText("Already have an account?");
			changeModeButton.setText("Log in instead");
		} else {
			currentMode = Mode.LOGIN;
			loginButton.setText("Login");
			newAccountLabel.setText("Don't have an account?");
			changeModeButton.setText("Create Account");

		}

		loginButton.setDisable(true);
		unmaskedPasswordField.setVisible(false);
		passwordField.setVisible(true);
		showPasswordCheckBox.setSelected(false);
		usernameField.clear();
		passwordField.clear();
	}

	// Triggered when enter key is pressed in the password field
	private void passwordFieldEnter(ActionEvent event) {
		loginButtonPress(event);
	}

	// Disables login button if either fields are empty.
	// Keep in mind that the KeyEvent is triggered before the key is put into the
	// thisField = current field being typed in, otherField = field not being typed
	// in
	private void fieldTyping(KeyEvent event, TextField thisField, TextField otherField) {

		if (thisField.getSelectedText().length() == thisField.getText().length()
				&& (event.getCode().toString().equals("BACK_SPACE") || event.getCode().toString().equals("DELETE"))) {
			loginButton.setDisable(true);

		} else if (thisField.getLength() <= 1 && event.getCode().toString().equals("BACK_SPACE")) {
			loginButton.setDisable(true);

		} else if (thisField.getText().equals("") && !otherField.getText().equals("")) {
			loginButton.setDisable(false);

		}
	}

	void showPasswordCheckBoxTicked(ActionEvent event) {
		boolean bothFieldsEmpty = usernameField.getText().equals("") && passwordField.getText().equals("");

		if (showPasswordCheckBox.isSelected()) {
			unmaskedPasswordField.setVisible(true);
			passwordField.setVisible(false);
			unmaskedPasswordField.setText(passwordField.getText());

			if (!bothFieldsEmpty) {
				unmaskedPasswordField.requestFocus();
				unmaskedPasswordField.selectEnd();
			}

		} else {
			unmaskedPasswordField.setVisible(false);
			passwordField.setVisible(true);
			passwordField.setText(unmaskedPasswordField.getText());

			if (!bothFieldsEmpty) {
				passwordField.requestFocus();
				passwordField.selectEnd();
			}
		}
	}
}
