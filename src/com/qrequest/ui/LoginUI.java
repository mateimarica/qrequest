package com.qrequest.ui;

import java.io.IOException;

import com.qrequest.control.UserController;
import com.qrequest.managers.LanguageManager;
import com.qrequest.managers.ThemeManager;
import com.qrequest.objects.Credentials;
import com.qrequest.objects.Language;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**The login/account creation user interface.*/
public class LoginUI {
		
	/**The dimensions of the Login screen. Don't change this.*/
	private final int WINDOW_HEIGHT = 300, WINDOW_WIDTH = 500;
	
	/**Enunumator defining which "mode" the login menu is in.*/
	private enum Mode {LOGIN, CREATE_ACCOUNT};

	/**The current mode that the login is in. Login is default, can switch to Create Account.*/
	private Mode currentMode = Mode.LOGIN;
	
	@FXML private Button languageButton;
	
	/**The username field.*/
	@FXML private TextField usernameField;
	
	/**The masked password field.*/
	@FXML private PasswordField passwordField;
	
	/**The unmasked password field.*/
	@FXML private TextField unmaskedPasswordField;
	
	/**The "Login"/"Create Account" button in the center of the screen.*/
	@FXML private Button loginButton;
	
	/**The "Create an account"/"Log in instead" button at bottom of the screen.*/
	@FXML private Button changeModeButton;
	
	/**The small label at the bottom of the screen asking "Don't have an account?"/"Already have an account?".*/
	@FXML private Label newAccountLabel;

	/**The "Show password" checkbox for viewing your password.*/
	@FXML private CheckBox showPasswordCheckBox;
	
	/**If this checkbox is ticked when the Login action is triggered, the user's credentials will be saved.*/
	@FXML private CheckBox saveCredentialsCheckBox;	

	@FXML private Label versionLabel;	
	
	private static Stage stage;
	/**The login menu is created and shown by this method. Called by the MainUI class.
	 * @param stage Where all the controls are created, put into the grid layout pane, put in the scene, then the stage, then shown.
	 */
	public void startScene(Stage stage) {	
		try {
			stage.setResizable(false);
			LoginUI.stage = stage;
			GridPane root = FXMLLoader.load(getClass().getResource("/fxml/LoginUI.fxml"), LanguageManager.getLangBundle());
			Scene loginScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
			
			loginScene.getStylesheets().add(ThemeManager.getCurrentThemeURL());
					
			stage.setScene(loginScene);
			stage.show();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	@FXML
	public void initialize() {
		versionLabel.setText(versionLabel.getText() + " " + getClass().getPackage().getImplementationVersion());
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
	@FXML
	private void processLoginButtonPress() {
		syncPasswordFields();
		
		String username = usernameField.getText();
		String password = passwordField.getText();
		Credentials creds = new Credentials(username, password);
		if (currentMode == Mode.LOGIN) {
			if(!UserController.login(creds))
				return;
		} else {
			if(!areCredentialsValid(creds) || !UserController.register(creds))
				return;
		}

		if(saveCredentialsCheckBox.isSelected()) {
			Credentials.saveCredentials(creds);
		} else {
			Credentials.removeCredentials();
		}
	
		ForumUI forumUI = new ForumUI();
		UserController.saveForumUI(forumUI);
		forumUI.startScene(LoginUI.stage);
	}
	
	/**Checks if the username & password in the fields are valid. 
	 * <dt>Requirements:</dt>
	 * 		<dd><li>Username must be 3 to 10 characters and only alphanumeric.</dd>
	 * 		<dd><li>Password must be 3 to 40 characters.</dd>
	 * @return True/false depending if the credentials are valid.
	 */
	private boolean areCredentialsValid(Credentials creds) {
		String username = creds.getUsername();

		if (username.length() < 3 || username.length() > 10) {
			PopupUI.displayWarningDialog(
					LanguageManager.getString("invalidUsernameTitle"), 
					String.format(
						LanguageManager.getString("usernameBadLength"), PopupUI.MIN_NAME_LENGTH, PopupUI.MAX_NAME_LENGTH
					)
			);
			
			return false;
		}

		if (!username.matches("[A-Za-z0-9]+")) {
			PopupUI.displayWarningDialog(LanguageManager.getString("invalidUsernameTitle"), LanguageManager.getString("usernameNotAlphanumeric"));
			
			return false;
		}
		
		String password = creds.getPassword();
		
		if(password.length() < 3 || password.length() > 40) {

			PopupUI.displayWarningDialog(
					LanguageManager.getString("invalidPasswordTitle"), 
					String.format(
						LanguageManager.getString("passwordBadLength"), PopupUI.MIN_PASSWORD_LENGTH, PopupUI.MAX_PASSWORD_LENGTH
					)
			);
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
	@FXML
	private void changeModeButtonPress() {
		
		if (currentMode == Mode.LOGIN) {
			currentMode = Mode.CREATE_ACCOUNT;
			
			loginButton.setText(LanguageManager.getString("createAccButton"));
			newAccountLabel.setText(LanguageManager.getString("switchToLoginText"));
			changeModeButton.setText(LanguageManager.getString("switchToLoginButton"));
		} else {
			currentMode = Mode.LOGIN;
			loginButton.setText(LanguageManager.getString("loginButton"));
			newAccountLabel.setText(LanguageManager.getString("switchToCreateAccText"));
			changeModeButton.setText(LanguageManager.getString("switchToCreateAccButton"));

		}

		loginButton.setDisable(true);
		unmaskedPasswordField.setVisible(false);
		passwordField.setVisible(true);
		showPasswordCheckBox.setSelected(false);
		usernameField.clear();
		passwordField.clear();
	}
	
	/**Wrapper method for FXML. See {@link #fieldTyping(KeyEvent, TextField, TextField) fieldTyping()}*/
	@FXML
	private void usernameFieldTyping(KeyEvent event) { fieldTyping(event, usernameField, passwordField); }
	
	/**Wrapper method for FXML. See {@link #fieldTyping(KeyEvent, TextField, TextField) fieldTyping()}*/
	@FXML
	private void passwordFieldTyping(KeyEvent event) { fieldTyping(event, passwordField, usernameField); }
	
	/**Wrapper method for FXML. See {@link #fieldTyping(KeyEvent, TextField, TextField) fieldTyping()}*/
	@FXML
	private void unmaskedPasswordFieldTyping(KeyEvent event) { fieldTyping(event, unmaskedPasswordField, usernameField); }
	
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
			
		} else if (thisField.getLength() < 1 && key.equals("BACK_SPACE")
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
	@FXML
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
	
	/**Triggered when the languageButton is clicked. Automatically lists all <code>Language</code>s.*/
	@FXML
	private void languageButtonPress() {
		
		languageButton.setOnMouseClicked(e -> {
			ContextMenu contextMenu = new ContextMenu();
						
			Language savedLang = LanguageManager.getSavedLanguage();
			
			for(Language lang : Language.values()) {
				CheckMenuItem langItem = new CheckMenuItem(lang.getName());
				langItem.setOnAction(l -> {
					if(langItem.isSelected()) {
						LanguageManager.setLanguage(lang);
						startScene(LoginUI.stage); 
					}
				}); 
				
				if(lang == savedLang) {
					langItem.setSelected(true);
				}
				
				contextMenu.getItems().add(langItem);
			}
			
			  
			contextMenu.show(stage, e.getScreenX() + 1, e.getScreenY() + 15);
		});
	}
}
