package com.qrequest.ui;
import java.util.ArrayList;
import java.util.Scanner;

import com.qrequest.control.GetAnswerControl;
import com.qrequest.control.GetQuestionControl;
import com.qrequest.control.LoginControl;
import com.qrequest.control.SearchUsersControl;
import com.qrequest.control.ThemeHelper;
import com.qrequest.object.Answer;
import com.qrequest.object.Credentials;
import com.qrequest.object.Post;
import com.qrequest.object.Question;
import com.qrequest.object.User;

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
import javafx.scene.control.skin.TableHeaderRow;
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
	
	/**Enunumator defining which "mode" the forum is in.
	 */
	private enum Mode {FRONT_PAGE, QUESTION_VIEWER};
	
	/**The current mode that the forum is in. Front page is default.
	 */
	private Mode currentMode = Mode.FRONT_PAGE;
	
	private Question currentQuestion;
	
	private ToolBar bottomBar;
	
	private Button askQuestionBtn;
	
	private Button searchUsersBtn;
	
	private Button refreshBtn;
	
	private ListView<GridPane> questionView;
	
	
	@SuppressWarnings("deprecation")
	public void startScene(Stage stage) {
		
		window = stage;
		forumLayout = new BorderPane();
		menuBar = new MenuBar();
		questionView = new ListView<GridPane>();
		
		
		
		MenuItem logoutItem = new MenuItem("Log out");
		logoutItem.setOnAction(e -> logout());
		
		Menu accountMenu = new Menu("Logged in as: " + LoginControl.getUser().getUsername());
		accountMenu.getItems().add(logoutItem);
		menuBar.getMenus().addAll(accountMenu);
		
		
		CheckMenuItem darkModeItem = new CheckMenuItem("Dark mode");
		darkModeItem.setSelected(ThemeHelper.darkModeEnabled());
		darkModeItem.setOnAction(e -> themeChanged(darkModeItem));
		
		Menu optionsMenu = new Menu("Options");
		optionsMenu.getItems().add(darkModeItem);
		menuBar.getMenus().addAll(optionsMenu);
		forumLayout.setTop(menuBar);
		
		if(currentMode == Mode.FRONT_PAGE) {
			createQuestionsTable();	
		} else {
			blowupQuestion(currentQuestion);
		}
		
		populateToolbar();
		
		
		
		//forumLayout.getChildren().addAll(questionsTable);
		Scene scene = new Scene(forumLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
		if(ThemeHelper.darkModeEnabled())
			scene.getStylesheets().add(ThemeHelper.darkThemeFileURL);
		else
			scene.getStylesheets().add(ThemeHelper.lightThemeFileURL);
			
		
		stage.setScene(scene);
		stage.show();
		
	}
	
	private void searchUsersBtnPress() {
		PopupUI.displaySearchUsersDialog();
	}
	private void populateToolbar() {
		bottomBar = new ToolBar();
		
		
		if(currentMode == Mode.FRONT_PAGE) {
			askQuestionBtn = new Button("\u2795 Ask a question");
			askQuestionBtn.setOnAction(e -> askQuestionBtnPressed());
			searchUsersBtn = new Button("\uD83D\uDC64 Search Users");
			searchUsersBtn.setOnAction(e -> searchUsersBtnPress());
			bottomBar.getItems().addAll(askQuestionBtn, searchUsersBtn);
			
			
		} else {
			Button backBtn = new Button("\u25C0 Go back");
			backBtn.setOnAction(e -> backBtnPress());
			bottomBar.getItems().add(backBtn);
			
			Button postAnswerBtn = new Button("\u2795 Answer question");
			postAnswerBtn.setOnAction(e -> answerQuestionBtnPress());
			
			bottomBar.getItems().addAll(postAnswerBtn);
			
		}
		
		refreshBtn = new Button("\uD83D\uDDD8 Refresh");
		refreshBtn.setOnAction(e -> refresh());
		bottomBar.getItems().add(refreshBtn);
		
		forumLayout.setBottom(bottomBar);
	}
	
	private void logout() {
		Credentials.removeCredentials();
		LoginControl.resetUser();
		new LoginUI().startScene(window);
	}
	
	private void askQuestionBtnPressed() {
		PopupUI.displayPostQuestionDialog(this);
	}
	
	private void populateTable() {
		ArrayList<Question> questions = GetQuestionControl.getAllQuestions();
		for(int i = 0; i < questions.size(); i++) {
			questionsTable.getItems().add(questions.get(i));
		}
		forumLayout.setCenter(questionsTable);
	}
	
	void refresh() {
		if(currentMode == Mode.FRONT_PAGE) {
			questionsTable.getItems().clear();
			populateTable();
		} else {
			blowupQuestion(currentQuestion);
		}
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
		    		currentMode = Mode.QUESTION_VIEWER;
		    		currentQuestion = row.getItem();
		    		blowupQuestion(currentQuestion);
		    	}
		    });
		    return row;
		});
		
		
		TableColumn titleCol = new TableColumn("Title");
		titleCol.setSortable(false);
		titleCol.setResizable(false);
		titleCol.setMinWidth(150);
		titleCol.setReorderable(false);
		titleCol.setCellValueFactory(
                new PropertyValueFactory<>("title"));
		
        TableColumn descCol = new TableColumn("Description");
        descCol.setSortable(false);
        descCol.setResizable(false);
        descCol.setMinWidth(175);
        descCol.setReorderable(false);
        descCol.setCellValueFactory(
                new PropertyValueFactory<>("description"));

		TableColumn authorCol = new TableColumn("Author");
        authorCol.setSortable(false);
        authorCol.setResizable(false);
        authorCol.setMinWidth(100);
        authorCol.setReorderable(false);
        authorCol.setCellValueFactory(
                new PropertyValueFactory<>("author"));
        
        TableColumn timePostedCol = new TableColumn("Time Posted");
        timePostedCol.setSortable(false);
        timePostedCol.setResizable(false);
        timePostedCol.setMinWidth(125);
        timePostedCol.setReorderable(false);
        timePostedCol.setCellValueFactory(
                new PropertyValueFactory<>("timePosted"));
        
        questionsTable.getColumns().addAll(titleCol, descCol, authorCol, timePostedCol);
		
		
		populateTable();
	}
	
	private void blowupQuestion(Question question) {
		
		
		populateToolbar();
		
		questionsTable.setVisible(false);
		questionView.setVisible(true);
		
		GetQuestionControl.refreshQuestion(currentQuestion);
		
		questionView.getItems().clear();
		questionView.getItems().add(buildQuestionPane(question));
		
		ArrayList<Answer> answerList = GetAnswerControl.getAllAnswers(currentQuestion);
		
		for(int i = 0; i < answerList.size(); i++) {
			questionView.getItems().add(buildAnswerPane(answerList.get(i)));
		}
		
		forumLayout.setCenter(questionView);
	}
	
	private void answerQuestionBtnPress() {
		PopupUI.displayPostAnswerDialog(this, currentQuestion);
	}
	
	private void backBtnPress() {
		questionView.getItems().clear();
		questionView.setVisible(false);
		questionsTable.setVisible(true);
		currentMode = Mode.FRONT_PAGE;
		populateToolbar();
		refresh();
	}
	
	private GridPane buildQuestionPane(Question question) {
		GridPane questionPane = new GridPane();
		// top, right, bottom, left padding
		questionPane.setPadding(new Insets(2, 2, 2, 2));
		questionPane.setVgap(8);
		questionPane.setHgap(8);
		//questionPane.setGridLinesVisible(true);
		
		ToggleButton questionUpvoteBtn = new ToggleButton("\u25B2");
		GridPane.setConstraints(questionUpvoteBtn, 0, 0);
		questionUpvoteBtn.setId("upvoteButton");
		ToggleButton questionDownvoteBtn = new ToggleButton("\u25BC");
		GridPane.setConstraints(questionDownvoteBtn, 0, 1);
		questionDownvoteBtn.setId("downvoteButton");
		
		Label qusetionTitle = new Label(question.getTitle());
		qusetionTitle.setStyle("-fx-font-size: 15px;\r\n" + 
				"-fx-font-family: Verdana;"
				+ "-fx-font-weight: bold;");
		qusetionTitle.setMaxWidth(WINDOW_WIDTH * 0.7);
		qusetionTitle.setMinWidth(WINDOW_WIDTH * 0.7);
		qusetionTitle.setWrapText(true);
		GridPane.setConstraints(qusetionTitle, 1, 0);
		
		Label questionAuthor = new Label("Posted by " + question.getAuthor().getUsername());
		questionAuthor.setAlignment(Pos.CENTER_RIGHT);
		questionAuthor.setMinWidth(WINDOW_WIDTH * 0.18);
		questionAuthor.setMaxWidth(WINDOW_WIDTH * 0.18);
		questionAuthor.setWrapText(true);
		GridPane.setConstraints(questionAuthor, 2, 0);
		
		Label questionDesc = new Label(question.getDescription());
		questionDesc.setMaxWidth(WINDOW_WIDTH * 0.7);
		questionDesc.setMinWidth(WINDOW_WIDTH * 0.7);
		questionDesc.setWrapText(true);
		GridPane.setConstraints(questionDesc, 1, 1);
		questionPane.getChildren().addAll(questionUpvoteBtn, questionDownvoteBtn, qusetionTitle, questionAuthor, questionDesc);
		
		return questionPane;
	}
	
	private GridPane buildAnswerPane(Answer answer) {
		GridPane answerPane = new GridPane();
		// top, right, bottom, left padding
		answerPane.setPadding(new Insets(2, 2, 2, 30));
		answerPane.setVgap(8);
		answerPane.setHgap(8);
		//questionPane.setGridLinesVisible(true);
		
		ToggleButton upvoteBtn = new ToggleButton("\u25B2");
		GridPane.setConstraints(upvoteBtn, 0, 0);
		upvoteBtn.setId("upvoteButton");
		
		ToggleButton downvoteBtn = new ToggleButton("\u25BC");
		GridPane.setConstraints(downvoteBtn, 0, 1);
		downvoteBtn.setId("downvoteButton");
		
		upvoteBtn.setOnAction(e -> {
			if(upvoteBtn.isSelected()) {
				if(downvoteBtn.isSelected()) {
					downvoteBtn.setSelected(false);
				}
			}
		});
		
		downvoteBtn.setOnAction(e -> {
			if(downvoteBtn.isSelected()) {
				if(downvoteBtn.isSelected()) {
					upvoteBtn.setSelected(false);
				}
			}
		});
		
		
		Label answerText = new Label(answer.getDescription());
		answerText.setMaxWidth(WINDOW_WIDTH * 0.62);
		answerText.setMinWidth(WINDOW_WIDTH * 0.62);
		answerText.setWrapText(true);
		GridPane.setConstraints(answerText, 1, 0);
		
		Label author = new Label("Posted by " + answer.getAuthor().getUsername());
		author.setAlignment(Pos.CENTER_RIGHT);
		author.setMinWidth(WINDOW_WIDTH * 0.21);
		author.setMaxWidth(WINDOW_WIDTH * 0.21);
		author.setWrapText(true);
		GridPane.setConstraints(author, 2, 0);
		
		answerPane.getChildren().addAll(upvoteBtn, downvoteBtn, answerText, author);
		
		return answerPane;
	}
	
	private GridPane buildPostPane(Post post) {
		GridPane postPane = new GridPane();
		// top, right, bottom, left padding
		postPane.setPadding(new Insets(2, 2, 2, 30));
		postPane.setVgap(8);
		postPane.setHgap(8);
		//questionPane.setGridLinesVisible(true);
		
		ToggleButton upvoteBtn = new ToggleButton("\u25B2");
		GridPane.setConstraints(upvoteBtn, 0, 0);
		upvoteBtn.setId("upvoteButton");
		
		ToggleButton downvoteBtn = new ToggleButton("\u25BC");
		GridPane.setConstraints(downvoteBtn, 0, 1);
		downvoteBtn.setId("downvoteButton");
		
		upvoteBtn.setOnAction(e -> {
			if(upvoteBtn.isSelected()) {
				if(downvoteBtn.isSelected()) {
					downvoteBtn.setSelected(false);
				}
			}
		});
		
		downvoteBtn.setOnAction(e -> {
			if(downvoteBtn.isSelected()) {
				if(downvoteBtn.isSelected()) {
					upvoteBtn.setSelected(false);
				}
			}
		});
		
		
		/*Label answerText = new Label(answer.getDescription());
		answerText.setMaxWidth(WINDOW_WIDTH * 0.62);
		answerText.setMinWidth(WINDOW_WIDTH * 0.62);
		answerText.setWrapText(true);
		GridPane.setConstraints(answerText, 1, 0);
		
		Label author = new Label("Posted by " + answer.getAuthor().getUsername());
		author.setAlignment(Pos.CENTER_RIGHT);
		author.setMinWidth(WINDOW_WIDTH * 0.21);
		author.setMaxWidth(WINDOW_WIDTH * 0.21);
		author.setWrapText(true);
		GridPane.setConstraints(author, 2, 0);*/
		
		//postPane.getChildren().addAll(upvoteBtn, downvoteBtn, answerText, author);
		
		return postPane;
	}
	
}
