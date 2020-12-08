package com.qrequest.ui;
import com.qrequest.control.LoginControl;
import com.qrequest.control.PreferenceManager;
import com.qrequest.control.ThemeHelper;
import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Credentials;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**Main UI, starts the LoginUI automatically.
 */
public class MainUI extends Application {
	
	
	final static String ICON_URL = "/com/qrequest/resources/images/icon.png";
	
	/**Starts the JavaFX instance.
	  */
	void begin(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
		setUpStage(stage);
		
		//Checks the saved credentials (if there are any)
		Credentials creds = Credentials.getSavedCredentials();
		
		//if the credentials DO exist
		if(creds != null) {
			
			try {
				if(LoginControl.processLogin(creds)) {
					new ForumUI().startScene(stage);
				} else {
					Credentials.removeCredentials();
					new LoginUI().startScene(stage);
					PopupUI.displayErrorDialog("Login Error", 
							"Couldn't automatically log in with your saved credentials. Perhaps the database was reset?");
				}
				
			} catch (DatabaseConnectionException e) {
				
				new LoginUI().startScene(stage);
				PopupUI.displayErrorDialog("Connection Error", "Couldn't connect to the database. "
						+ "Make sure you're connected to the UNB VPN.");
			}			
			
		} else {
			//Starts the Login menu if no saved credentials
			new LoginUI().startScene(stage);
		}
		
	}
	
	/**Sets up the stage's icon, setOnCloseRequest() function, and the title.
	 * @param stage The stage.
	 * @return The set up stage.
	 */
	static Stage setUpStage(Stage stage) {
		stage.getIcons().add(new Image(ICON_URL));
		
		//Overriding the program's close request function.
		stage.setOnCloseRequest(e -> {
			e.consume(); //lets Java know that we are handing the user's close request
			closeProgram(stage);			
		});
		
		stage.setTitle("QRequest");
		return stage;
	}
	
	/**Called when the close is closed.
	 */
	private static void closeProgram(Stage stage) {
		//This method is called when the user tries to close the program.
		//This could have some kind of saving functionality if needed.
		stage.close();
	}	
}