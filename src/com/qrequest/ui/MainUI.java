package com.qrequest.ui;

import com.qrequest.control.UpdateController;
import com.qrequest.control.UserController;
import com.qrequest.managers.LanguageManager;
import com.qrequest.objects.Credentials;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**Main UI, starts the LoginUI automatically.
 */
public class MainUI extends Application {
	
	/**The URL of the application's icon. Used in every popup window.*/
	final static String ICON_URL = "/images/icon.png";
	
	private static Environment env = Environment.PROD;

	public enum Environment { PROD, DEV }

	public static Environment getEnv() {
		return env;
	}

	/**Starts the JavaFX instance.*/
	void begin(String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase(Environment.DEV.name())) {
			env = Environment.DEV;
		}
		launch(args);
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
		//Sets icon, application title, etc
		setUpStage(stage);

		new Thread(() -> {
			UpdateController.checkForUpdates();
		}).start();

		LanguageManager.loadSavedLanguage();
		
		//Checks the saved credentials (if there are any)
		Credentials creds = Credentials.getSavedCredentials();

		//if the credentials DO exist
		if(creds != null) {
			if(UserController.login(creds)) {
				new ForumUI().startScene(stage);
			} else {
				Credentials.removeCredentials();
				
				new LoginUI().startScene(stage);
				
				PopupUI.displayErrorDialog(LanguageManager.getString("loginFailedTitle"), 
											LanguageManager.getString("autoLoginFailed"));
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
	
	/**Called when the stage is closed.*/
	private static void closeProgram(Stage stage) {
		//This method is called when the user tries to close the program.
		//This could have some kind of saving functionality if needed.
		stage.close();
		Platform.exit();
		System.exit(0);
	}	
}