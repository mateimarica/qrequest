package com.qrequest.ui;
import java.util.ResourceBundle;

import com.qrequest.control.LoginControl;
import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.helpers.LanguageManager;
import com.qrequest.objects.Credentials;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**Main UI, starts the LoginUI automatically.
 */
public class MainUI extends Application {
	
	/**The URL of the application's icon. Used in every popup window.*/
	final static String ICON_URL = "/com/qrequest/resources/images/icon.png";
	
	
	
	/**Starts the JavaFX instance.*/
	void begin(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
		setUpStage(stage);
		
		LanguageManager.loadSavedLanguage();
		
		//Checks the saved credentials (if there are any)
		Credentials creds = Credentials.getSavedCredentials();

		//if the credentials DO exist
		if(creds != null) {
			
			try {
				if(new LoginControl().processLogin(creds)) {
					new ForumUI().startScene(stage);
				} else {
					Credentials.removeCredentials();
					System.out.println("Cleared");
					new LoginUI().startScene(stage);
					PopupUI.displayErrorDialog(LanguageManager.getLangBundle().getString("loginFailedTitle"), 
											   LanguageManager.getLangBundle().getString("autoLoginFailed"));
				}
				
			} catch (DatabaseConnectionException e) {
				
				new LoginUI().startScene(stage);
				PopupUI.displayDatabaseConnectionErrorDialog();
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
	
	/**Called when the close is closed.*/
	private static void closeProgram(Stage stage) {
		//This method is called when the user tries to close the program.
		//This could have some kind of saving functionality if needed.
		stage.close();
	}	
}