package com.qrequest.ui;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainUI extends Application {
	
	private Stage window;
	static Scene loginScene, forumScene;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		window = stage;
		stage.setTitle("QRequest");
		stage.setResizable(false);
		
		new LoginUI().startScene(stage);
	}
}