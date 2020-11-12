package com.qrequest.ui;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForumUI {
	Stage window;
	
	public void startScene(Stage stage) {
		window = stage;
		VBox forumLayout = new VBox();
		MainUI.forumScene = new Scene(forumLayout, 1000, 700);
		stage.setScene(MainUI.forumScene);
		//window.show();
	}
}
