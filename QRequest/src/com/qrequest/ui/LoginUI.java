package com.qrequest.ui;

import com.qrequest.control.CreateAccountControl;
import com.qrequest.control.LoginControl;
import com.qrequest.exceptions.DatabaseConnectionException;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**The login/account creation user interface.
 */
public class LoginUI {
	
	/**The "Login"/"Create Account" button in the center of the screen.
	 */
	private Button loginButton;
	
	/**The "Create an account"/"Log in instead" button at bottom of the screen.
	 */
	private Button changeModeButton;
	
	/**The big "QREQUEST" title at the top of the screen.
	 */
	private Label titleLabel;
	
	/**The small label at the bottom of the screen asking "Don't have an account?"/"Already have an account?".
	 */
	private Label newAccountLabel;

	/**The "Show password" checkbox for viewing your password.
	 */
	private CheckBox showPasswordCheckBox;

	/**The stage upon which every control is placed.
	 */
	private Stage window;
	
	/**The layout of the Login menu. All controls are put into an index denoted by (x, y) coords, where (0, 0) is the top left corner.<br>
	 * Use <code>loginGridLayout.setGridLinesVisible(true);</code> to view how the controls are laid out. 
	 */
	private GridPane loginGridLayout;
	
	/**The username field.
	 */
	private TextField usernameField;
	
	/**The masked password field.
	 */
	private PasswordField passwordField;
	
	/**The unmasked password field.
	 */
	private TextField unmaskedPasswordField;
	
	/**The dimensions of the Login screen. Don't change this.
	 */
	private final int WINDOW_HEIGHT = 300, WINDOW_WIDTH = 500;
	
	

	/**Enunumator defining which "mode" the login menu is in.
	 */
	private enum Mode {LOGIN, CREATE_ACCOUNT};

	/**The current mode that the login is in. Login is default, can switch to Create Account.
	 */
	private Mode currentMode = Mode.LOGIN;

	/**The login menu is created and shown by this method. Called by the MainUI class.
	 * @param stage Where all the controls are created, put into the grid layout pane, put in the scene, then the stage, then shown.
	 */
	public void startScene(Stage stage) {
		window = stage; // Saves pointer to stage for reference in other methods.

		loginGridLayout = new GridPane();
		GridPane.setHalignment(loginGridLayout, HPos.CENTER);
		GridPane.setValignment(loginGridLayout, VPos.CENTER);
		// top, right, bottom, left padding
		loginGridLayout.setPadding(new Insets(10, 10, 10, 55));
		loginGridLayout.setVgap(8);
		loginGridLayout.setHgap(10);
		loginGridLayout.setGridLinesVisible(false); // Set this to true if you want to see how the login menu is laid out.

		loginGridLayout.getColumnConstraints().add(new ColumnConstraints(100));

		titleLabel = new Label();
		titleLabel.setText("qRequest");
		titleLabel.getStylesheets().add("/com/qrequest/ui/resources/title-styling.css");
		GridPane.setConstraints(titleLabel, 1, 0); 

		usernameField = new TextField();
		usernameField.setMaxWidth(150);
		usernameField.setPromptText("Username");
		usernameField.setOnAction(e -> loginButtonPress());
		usernameField.setOnKeyPressed(e -> fieldTyping(e, usernameField, passwordField));
		GridPane.setConstraints(usernameField, 1, 1);
		GridPane.setHalignment(usernameField, HPos.CENTER);

		passwordField = new PasswordField();
		passwordField.setMaxWidth(150);
		passwordField.setPromptText("Password");
		passwordField.setOnAction(e -> loginButtonPress());
		passwordField.setOnKeyPressed(e -> fieldTyping(e, passwordField, usernameField));
		GridPane.setConstraints(passwordField, 1, 2);
		GridPane.setHalignment(passwordField, HPos.CENTER);

		unmaskedPasswordField = new TextField();
		unmaskedPasswordField.setMaxWidth(150);
		unmaskedPasswordField.setPromptText("Password");
		unmaskedPasswordField.setOnAction(e -> loginButtonPress());
		unmaskedPasswordField.setOnKeyPressed(e -> fieldTyping(e, unmaskedPasswordField, usernameField));
		unmaskedPasswordField.setVisible(false);
		GridPane.setConstraints(unmaskedPasswordField, 1, 2);
		GridPane.setHalignment(unmaskedPasswordField, HPos.CENTER);

		showPasswordCheckBox = new CheckBox("Show password");
		showPasswordCheckBox.setOnAction(e -> showPasswordCheckBoxTicked());
		GridPane.setConstraints(showPasswordCheckBox, 2, 2);

		loginButton = new Button();
		loginButton.setText("Login");
		loginButton.setOnAction(e -> loginButtonPress());
		loginButton.setDisable(true);
		GridPane.setConstraints(loginButton, 1, 3);
		GridPane.setHalignment(loginButton, HPos.CENTER);

		newAccountLabel = new Label();
		newAccountLabel.setText("Don't have an account?");
		GridPane.setConstraints(newAccountLabel, 1, 11);
		GridPane.setHalignment(newAccountLabel, HPos.CENTER);

		changeModeButton = new Button();
		changeModeButton.setText("Create an account");
		changeModeButton.setOnAction(e -> changeModeButtonPress());
		GridPane.setConstraints(changeModeButton, 1, 12);
		GridPane.setHalignment(changeModeButton, HPos.CENTER);
		
		//Adds all the controls to the grid layout
		loginGridLayout.getChildren().addAll(titleLabel, usernameField, passwordField, unmaskedPasswordField, showPasswordCheckBox,
				loginButton, changeModeButton, newAccountLabel);

		
		Scene loginScene = new Scene(loginGridLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
		if(ThemeHelper.darkModeEnabled)
			loginScene.getStylesheets().add(ThemeHelper.darkThemeFileURL);
		
		
		stage.setScene(loginScene);
		stage.show();
	}

	
	/**Runs whens the "Login"/"Create Button" button is clicked. (Note: not the button at the button)
	 * <dt>Login Case:</dt>
	 * 		<dd><li>Attempts to login with username & password.
	 * 		<br><li>If successful, starts ForumUI.
	 * 		<br><li>If it fails, displays error pop-up.</dd>
	 * <dt>Create Account Case:</dt>
	 * 		<dd><li>First checks if the username & password have the right requirements. If not, displays warning pop-up.
	 * 		<br><li>Then checks if the username is already in use. If it is, displays error pop-up.
	 * 		<br><li>If no more errors, creates account and automatically jumps to the Login Case with the username & password.</dd>
	 */
	private void loginButtonPress() {
		syncPasswordFields();
		
		String username = usernameField.getText();
		String password = passwordField.getText();
		
		if (currentMode == Mode.LOGIN) {
			boolean loginSuccessful = false; 
			try {
				loginSuccessful = LoginControl.processLogin(username, password);
				
				if (loginSuccessful) {
					new ForumUI().startScene(window);
				} else {
					PopupUI.displayErrorDialog("Login Failed", "Username or password is incorrect.");
				}
			} catch (DatabaseConnectionException e) {
				PopupUI.displayErrorDialog("Connection Error", "Couldn't connect to the database. "
						+ "Make sure you're connected to the UNB VPN.");
			}

			
		} else {
			if (!areCredentialsValid()) {
				return;
			}
			
			try {
				if(CreateAccountControl.processDoesAccountExist(username)) {
					PopupUI.displayErrorDialog("Error Creating Account", "An account with that username already exists.");
					return;
				}
			} catch (DatabaseConnectionException e) {
				PopupUI.displayErrorDialog("Connection Error", "Couldn't connect to the database. "
						+ "Make sure you're connected to the UNB VPN.");
				return;
			}
			
			
			CreateAccountControl.processCreateAccount(username, password);
			currentMode = Mode.LOGIN;
			loginButtonPress();
		}
	}
	
	/**Checks if the username & password in the fields are valid. 
	 * <dt>Requirements:</dt>
	 * 		<dd><li>Username must be 3 to 10 characters and only alphanumeric.</dd>
	 * 		<dd><li>Password must be 3 to 40 characters.</dd>
	 * @return True/false depending if the credentials are valid.
	 */
	private boolean areCredentialsValid() {
		String username = usernameField.getText();
		
		if (username.length() < 3 || username.length() > 10) {
			PopupUI.displayWarningDialog("Invalid Username", "Username length must be between 3 and 10.");
			return false;
		}

		if (!username.matches("[A-Za-z0-9]+")) {
			PopupUI.displayWarningDialog("Invalid Username", "Username must be alphanumeric.");
			return false;
		}
		
		String password = passwordField.getText();
		
		if(password.length() < 3 || password.length() > 40) {
			PopupUI.displayWarningDialog("Invalid Password", "Passwords must be between 3 and 40 characters");
			return false;
		}

		return true;
	}

	/**Syncs regular passwordField to the unmaskedPasswordField's text. This would be called if you uncheck "Show password".
	 */
	private void syncPasswordFields() {
		if (showPasswordCheckBox.isSelected()) {
			passwordField.setText(unmaskedPasswordField.getText());
		}
	}
	
	/**Run every time the "Create an account"/"Login in instead" button is pressed.<br>
	 * Does all the switching, clears the fields, and changes the currentMode.
	 */
	private void changeModeButtonPress() {
		if (currentMode == Mode.LOGIN) {
			currentMode = Mode.CREATE_ACCOUNT;
			loginButton.setText("Create Account");
			newAccountLabel.setText("Already have an account?");
			changeModeButton.setText("Log in instead");
		} else {
			currentMode = Mode.LOGIN;
			loginButton.setText("Login");
			newAccountLabel.setText("Don't have an account?");
			changeModeButton.setText("Create an account");

		}

		loginButton.setDisable(true);
		unmaskedPasswordField.setVisible(false);
		passwordField.setVisible(true);
		showPasswordCheckBox.setSelected(false);
		usernameField.clear();
		passwordField.clear();
	}

	

	
	/**Runs every time a character is typed or deleted in the usernameField or passwordField.<br>
	 * Disables "Login" button if there's no text in either field.<br>
	 * Note: This method is called <i>before</i> the key described in the KeyEvent is put into the field. 
	 * @param event The KeyEvent that describes the key that was just pressed.
	 * @param thisField The current field that is being typed in.
	 * @param otherField The field that is not being typed in.
	 */
	private void fieldTyping(KeyEvent event, TextField thisField, TextField otherField) {
		String key = event.getCode().toString();
		
		
		if (thisField.getSelectedText().length() == thisField.getLength()
				&& (key.equals("BACK_SPACE") || key.equals("DELETE"))) {
			loginButton.setDisable(true);

		} else if (thisField.getLength() <= 1 && key.equals("BACK_SPACE")
				&& (thisField.getCaretPosition() != 0) 
				|| (thisField.getLength() == 0 && thisField.getCaretPosition() == 0 && key.equals("DELETE"))) {
			loginButton.setDisable(true);

		} else if (thisField.getText().isEmpty() && !otherField.getText().isEmpty()) {
			loginButton.setDisable(false);

		}
	}
	
	/**Runs when the "Show password" checkbox is ticked/unticked.<br>
	 * When ticked, overlays a regular textfield over the passwordField to act as the unmasked passwordField.<br>
	 * When unticked, hides the unmasked "passwordField".<br>
	 * Transfers the password between the passwordField and the unmasked "passwordField".
	 */
	private void showPasswordCheckBoxTicked() {
		boolean bothFieldsEmpty = usernameField.getText().isEmpty() && passwordField.getText().isEmpty();
		
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
