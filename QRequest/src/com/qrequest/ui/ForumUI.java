package com.qrequest.ui;
import java.util.ArrayList;

import com.qrequest.control.GetQuestionsControl;
import com.qrequest.control.LoginControl;
import com.qrequest.object.Question;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ForumUI {
	private Stage window;
	private MenuBar menuBar;
	private TableView<Question> questionsTable;
	private BorderPane forumLayout;
	
	private final int WINDOW_HEIGHT = 700;
	private final int WINDOW_WIDTH = 552;
	
	@SuppressWarnings("deprecation")
	public void startScene(Stage stage) {
		window = stage;
		forumLayout = new BorderPane();
		menuBar = new MenuBar();
		
		
		MenuItem logoutItem = new MenuItem("Log out");
		logoutItem.setOnAction(e -> logout());
		
		Menu accountMenu = new Menu("Logged in as: " + LoginControl.getUser().getUsername());
		accountMenu.getItems().add(logoutItem);
		menuBar.getMenus().addAll(accountMenu);
		
		
		CheckMenuItem darkModeItem = new CheckMenuItem("Dark mode");
		darkModeItem.setSelected(ThemeHelper.darkModeEnabled);
		darkModeItem.setOnAction(e -> themeChanged(darkModeItem));
		
		Menu optionsMenu = new Menu("Options");
		optionsMenu.getItems().add(darkModeItem);
		menuBar.getMenus().addAll(optionsMenu);
		forumLayout.setTop(menuBar);
		
		//createQuestionsTable();		
		
		
		
		ListView<GridPane> questionView = new ListView<GridPane>();
		questionView.getItems().addAll(buildQuestionPane(null));
		forumLayout.setCenter(questionView);
		
		
		ToolBar bottomBar = new ToolBar();
		forumLayout.setBottom(bottomBar);
		
		Button askQuestionBtn = new Button("\u2795 Ask a question");
		askQuestionBtn.setOnAction(e -> askQuestionBtnPressed());
		bottomBar.getItems().add(askQuestionBtn);
		
		Button refreshBtn = new Button("\uD83D\uDDD8 Refresh");
		refreshBtn.setOnAction(e -> refreshTable());
		bottomBar.getItems().add(refreshBtn);
		
		
		//forumLayout.getChildren().addAll(questionsTable);
		Scene scene = new Scene(forumLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
		if(ThemeHelper.darkModeEnabled)
			scene.getStylesheets().add(ThemeHelper.darkThemeFileURL);
		else
			scene.getStylesheets().add(ThemeHelper.lightThemeFileURL);
			
		
		stage.setScene(scene);
		stage.show();
		
	}
	
	private void logout() {
		LoginControl.resetUser();
		new LoginUI().startScene(window);
	}
	
	private void askQuestionBtnPressed() {
		PopupUI.displayPostQuestionDialog(this);
	}
	
	private void populateTable() {
		ArrayList<Question> questions = GetQuestionsControl.getQuestions();
		for(int i = 0; i < questions.size(); i++) {
			questionsTable.getItems().add(questions.get(i));
		}
	}
	
	void refreshTable() {
		questionsTable.getItems().clear();
		populateTable();
		//questionsTable.refresh();
	}
	
	private void themeChanged(CheckMenuItem darkModeItem) {
		ThemeHelper.saveTheme(darkModeItem.isSelected());
		startScene(window);
	}
	
	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	private void createQuestionsTable() {
		questionsTable = new TableView();
		questionsTable.setRowFactory(tv -> {

		    // Define our new TableRow
		    TableRow<Question> row = new TableRow<>();
		    
		    row.setOnMouseClicked(event -> {
		    	if(event.getClickCount() == 2){
		    		System.out.println(row.getItem().getTitle());
		    	}
		    });
		    return row;
		});
		
		
		
		TableColumn titleCol = new TableColumn("Title");
		titleCol.impl_setReorderable(false);
		titleCol.setResizable(false);
		titleCol.setMinWidth(150);
		titleCol.setCellValueFactory(
                new PropertyValueFactory<>("title"));
		
        TableColumn descCol = new TableColumn("Description");
        descCol.impl_setReorderable(false);
        descCol.setResizable(false);
        descCol.setMinWidth(175);
        descCol.setCellValueFactory(
                new PropertyValueFactory<>("description"));
        
        
        
        
        @SuppressWarnings("rawtypes")
		TableColumn authorCol = new TableColumn("Author");
        authorCol.impl_setReorderable(false);
        authorCol.setResizable(false);
        authorCol.setMinWidth(100);
        authorCol.setCellValueFactory(
                new PropertyValueFactory<>("author"));
        
        TableColumn timePostedCol = new TableColumn("Time Posted");
        timePostedCol.impl_setReorderable(false);
        timePostedCol.setResizable(false);
        timePostedCol.setMinWidth(125);
        timePostedCol.setCellValueFactory(
                new PropertyValueFactory<>("timePosted"));
        
        questionsTable.getColumns().addAll(titleCol, descCol, authorCol, timePostedCol);
		forumLayout.setCenter(questionsTable);
		
		
		populateTable();
	}
	
	private GridPane buildQuestionPane(Question question) {
		GridPane questionPane = new GridPane();
		// top, right, bottom, left padding
		questionPane.setPadding(new Insets(2, 2, 2, 2));
		questionPane.setVgap(8);
		questionPane.setHgap(8);
		//questionPane.setGridLinesVisible(true);
		
		ToggleButton upvoteBtn = new ToggleButton("\u25B2");
		GridPane.setConstraints(upvoteBtn, 0, 0);
		ToggleButton downvoteBtn = new ToggleButton("\u25BC");
		downvoteBtn.setId("downvote-button");
		GridPane.setConstraints(downvoteBtn, 0, 1);
		Label title = new Label("Help! I have a big question! m dolor sit amet, consectetur adipiscing elit. Donec dui ur");
		title.setStyle("-fx-font-size: 15px;\r\n" + 
				"-fx-font-family: Verdana;"
				+ "-fx-font-weight: bold;");
		title.setMaxWidth(WINDOW_WIDTH * 0.7);
		title.setMinWidth(WINDOW_WIDTH * 0.7);
		title.setWrapText(true);
		GridPane.setConstraints(title, 1, 0);
		
		Label author = new Label("Posted by aaaa");
		author.setAlignment(Pos.CENTER_RIGHT);
		author.setMinWidth(WINDOW_WIDTH * 0.18);
		author.setMaxWidth(WINDOW_WIDTH * 0.18);
		author.setWrapText(true);
		GridPane.setConstraints(author, 2, 0);
		
		Label desc = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec dui urna, porta id fermentum vel, dapibus tristique nisl.");
		desc.setMaxWidth(WINDOW_WIDTH * 0.7);
		desc.setMinWidth(WINDOW_WIDTH * 0.7);
		desc.setWrapText(true);
		GridPane.setConstraints(desc, 1, 1);
		questionPane.getChildren().addAll(upvoteBtn, downvoteBtn, title,author, desc);
		
		return questionPane;
	}
}
