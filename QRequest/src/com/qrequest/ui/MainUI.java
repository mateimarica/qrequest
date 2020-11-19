package com.qrequest.ui;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**Main UI, starts the LoginUI automatically.
 */
public class MainUI extends Application {
	
	/**Instance of the window.
	 */
	private Stage window;
	
	final static String ICON_URL = "/com/qrequest/ui/resources/icon.png";
	
	/**Starts the JavaFX instance.
	 */
	void begin(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		window = stage;
		
		stage.getIcons().add(new Image(ICON_URL));
		
		ThemeHelper.retrieveTheme();
		
		//Overriding the program's close request function.
		stage.setOnCloseRequest(e -> {
			e.consume(); //lets Java know that we are handing the user's close request
			closeProgram();			
		});
		
		stage.setTitle("QRequest");
		stage.setResizable(false);
		
		//Starts the Login menu.
		new LoginUI().startScene(stage);
	}
	
	/**Called when the close is closed.
	 */
	private void closeProgram() {
		//This method is called when the user tries to close the program.
		//This could have some kind of saving functionality if needed.
		window.close();
	}	
}