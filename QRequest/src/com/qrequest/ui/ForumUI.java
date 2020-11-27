package com.qrequest.ui;
import java.io.IOException;
import java.util.ArrayList;

import com.qrequest.control.DeletePostControl;
import com.qrequest.control.EditQuestionControl;
import com.qrequest.control.GetAnswerControl;
import com.qrequest.control.GetQuestionControl;
import com.qrequest.control.LoginControl;
import com.qrequest.control.PostAnswerControl;
import com.qrequest.control.PostQuestionControl;
import com.qrequest.control.ThemeHelper;
import com.qrequest.control.VoteControl;
import com.qrequest.objects.Answer;
import com.qrequest.objects.Credentials;
import com.qrequest.objects.Post;
import com.qrequest.objects.Question;
import com.qrequest.objects.Vote;
import com.qrequest.objects.Vote.VoteType;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ForumUI {
	
	/**The ListView that listed the front page questions or the individual question + answers.*/
	private ListView<GridPane> postList;
	
	/**The dimensions of the window. Don't change this*/
	private final int WINDOW_HEIGHT = 700, WINDOW_WIDTH = 552;
	
	/**Enunumator defining which "mode" the forum is in.*/
	private enum Mode {FRONT_PAGE, QUESTION_VIEWER};
	
	/**The current mode that the forum is in. Front page is default.*/
	private Mode currentMode = Mode.FRONT_PAGE;
	
	/**Reference to the current question that is being interacted with*/
	private Question currentQuestion;
	
	/**The toolbar are at the bottom of the window containing buttons like "Ask Question" and "Refresh"*/
	private ToolBar bottomBar;
	
	/**Button to ask a question*/
	private Button askQuestionBtn;
	
	/**Button to search for users*/
	private Button searchUsersBtn;
	
	/**Button to refresh the current post list*/
	private Button refreshBtn;
	
	@FXML private MenuBar menuBar;
	
	@FXML private Menu accountMenu;
	@FXML private MenuItem logoutItem;
	
	@FXML private Menu optionsMenu;
	@FXML private CheckMenuItem darkModeItem;
	
	private BorderPane root;
	
	@SuppressWarnings("deprecation")
	public void startScene(Stage stage) {
		
		try {
			root = FXMLLoader.load(getClass().getResource("/com/qrequest/resources/fxml/ForumUI.fxml"));
		} catch (IOException e) { e.printStackTrace(); }
		
		postList = new ListView<GridPane>();
		postList.setFocusTraversable(false);
		
		root.setCenter(postList);
		
		if(currentMode == Mode.FRONT_PAGE) {
			createQuestionsList();	
		} else {
			blowupQuestion(currentQuestion);
		}
		
		bottomBar = new ToolBar();
		populateToolbar();		
		
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		scene.getStylesheets().add(ThemeHelper.getCurrentThemeURL());
		
		
		stage.setScene(scene);
		stage.show();		
	}
	
	private Object loadFXML(String URL) {
		try {
			return FXMLLoader.load(getClass().getResource(URL));
		} catch (IOException e) { e.printStackTrace(); }
		return null;
	}
	
	@FXML
	public void initialize(){
		darkModeItem.setSelected(ThemeHelper.isDarkModeEnabled());
		accountMenu.setText("Logged in as: " + LoginControl.getUser().getUsername());
	}
	
	private void searchUsersBtnPress() {
		PopupUI.displaySearchUsersDialog();
	}
	
	void processQuestionDeleted() {
		currentQuestion = null;
		backBtnPress();
	}
	
	private void populateToolbar() {
		bottomBar.getItems().clear();
		
		
		if(currentMode == Mode.FRONT_PAGE) { //If the app is on the front page, display these buttons on the bottomBar:
			askQuestionBtn = new Button("\u2795 Ask a question");
			askQuestionBtn.setOnAction(e -> processAskQuestionButtonPress());
			
			searchUsersBtn = new Button("\uD83D\uDC64 Search Users");
			searchUsersBtn.setOnAction(e -> searchUsersBtnPress());
			bottomBar.getItems().addAll(askQuestionBtn, searchUsersBtn);
			
			
		} else {//If the app is in the question viewer, display these buttons on the bottomBar:
			Button backBtn = new Button("\u25C0 Go back");
			backBtn.setOnAction(e -> processBackButtonPress());
			bottomBar.getItems().add(backBtn);
			
			Button postAnswerBtn = new Button("\u2795 Answer question");
			postAnswerBtn.setOnAction(e -> processPostAnswerButtonPress());
			
			if(currentQuestion.getAuthor().getUsername().equals(LoginControl.getUser().getUsername())) {
				Button deleteBtn = new Button("\uD83D\uDDD1 Delete question");
				deleteBtn.setOnAction(e -> processDeleteQuestionButtonPress());
					
				Button editBtn = new Button("\uD83D\uDD89 Edit Question");
				editBtn.setOnAction(e -> processEditQuestionButtonPress());
				bottomBar.getItems().addAll(deleteBtn, editBtn);
			}
			
			
			bottomBar.getItems().addAll(postAnswerBtn);
			
		}
		//Always display this button on the bottomBar:
		refreshBtn = new Button("\uD83D\uDDD8 Refresh");
		refreshBtn.setOnAction(e -> refresh());
		bottomBar.getItems().add(refreshBtn);
		
		root.setBottom(bottomBar); //Put the bottomBar on the bottom of the root pane
	}
	
	
	
	private void processBackButtonPress() {
		currentQuestion = null;
		backBtnPress();
	}
	
	private void processAskQuestionButtonPress() {
		Question newQuestion = PopupUI.displayAskQuestionDialog();
		if(newQuestion != null) {
			PostQuestionControl.processPostQuestion(newQuestion);
			refresh();
		}
	}
	
	private void processPostAnswerButtonPress() {
		Answer newAnswer = PopupUI.displayPostAnswerDialog(currentQuestion);
		if(newAnswer != null) {
			PostAnswerControl.processPostAnswer(newAnswer);
			refresh();
		}
	}
	
	private void processEditQuestionButtonPress() {
		if(PopupUI.displayEditQuestionDialog(currentQuestion)) {
			EditQuestionControl.processEditQuestion(currentQuestion);
			refresh();
		}
	}
	
	private void processDeleteQuestionButtonPress() {
		if(PopupUI.displayConfirmationDialog("Confirm Deletion", "Are you sure you want to delete this question? This cannot be undone!")) {
			DeletePostControl.processDeletePost(currentQuestion);
			processQuestionDeleted();
		}
	}
	
	
	
	@FXML
	private void logout() {
		Credentials.removeCredentials();
		LoginControl.resetUser();
		new LoginUI().startScene(MainUI.stage);
	}
	

	
	void refresh() {
		if(currentMode == Mode.FRONT_PAGE) {
			createQuestionsList();
		} else {
			blowupQuestion(currentQuestion);
		}
	}
	
	@FXML
	private void themeChanged() {
		ThemeHelper.saveTheme(darkModeItem.isSelected());
		startScene(MainUI.stage);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createQuestionsList() {
        ArrayList<Question> questionList = GetQuestionControl.getAllQuestions();
        
        for(int i = 0; i < questionList.size(); i++) {
        	postList.getItems().add(buildQuestionPane(questionList.get(i)));
        	
        }
        
	}
	
	private void blowupQuestion(Question question) {
		currentMode = Mode.QUESTION_VIEWER;
		populateToolbar();
		
		GetQuestionControl.refreshQuestion(question);		
		
		populateQuestion(question);
	}
	
	private void populateQuestion(Question question) {
		postList.getItems().clear();
		postList.getItems().add(buildPostPane(currentQuestion));
		
		ArrayList<Answer> answerList = GetAnswerControl.getAllAnswers(currentQuestion);
		
		for(int i = 0; i < answerList.size(); i++) {
			postList.getItems().add(buildPostPane(answerList.get(i)));
		}
	}
	

	private void backBtnPress() {
		postList.getItems().clear();
		currentMode = Mode.FRONT_PAGE;
		populateToolbar();
		refresh();
	}
	
	private GridPane buildQuestionPane(Question question) {
		
		
		GridPane postPane = new GridPane();
		
		postPane.setPadding(new Insets(2, 2, 2, 2));
		
		
		postPane.setVgap(8);
		postPane.setHgap(8);
		
		ToggleButton upvoteBtn = new ToggleButton("\u25B2");
		GridPane.setConstraints(upvoteBtn, 0, 0);
		upvoteBtn.setId("upvoteButton");
		
		ToggleButton downvoteBtn = new ToggleButton("\u25BC");
		GridPane.setConstraints(downvoteBtn, 0, 1);
		downvoteBtn.setId("downvoteButton");
	
		if(question.getCurrentUserVote() == 1) {
			upvoteBtn.setSelected(true);
		} else if (question.getCurrentUserVote() == -1) {
			downvoteBtn.setSelected(true);
		}
		
		upvoteBtn.setOnAction(e -> {
			if(upvoteBtn.isSelected()) {
				if(downvoteBtn.isSelected()) {
					downvoteBtn.setSelected(false);
				}
			
				VoteControl.addVote(new Vote(question, LoginControl.getUser(), VoteType.UPVOTE));
				question.setCurrentUserVote(VoteType.UPVOTE);
			} else {
				VoteControl.addVote(new Vote(question, LoginControl.getUser(), VoteType.RESET_VOTE));
				question.setCurrentUserVote(VoteType.RESET_VOTE);
			}
		});
		
		downvoteBtn.setOnAction(e -> {
			if(downvoteBtn.isSelected()) {
				if(downvoteBtn.isSelected()) {
					upvoteBtn.setSelected(false);
				}
				
				VoteControl.addVote(new Vote(question, LoginControl.getUser(), VoteType.DOWNVOTE));
				question.setCurrentUserVote(VoteType.DOWNVOTE);
			} else {
				VoteControl.addVote(new Vote(question, LoginControl.getUser(), VoteType.RESET_VOTE));
				question.setCurrentUserVote(VoteType.RESET_VOTE);
			}
		});
		
		Label questionTitleLabel = new Label(question.getTitle());
		questionTitleLabel.setId("questionTitleLabel");
		
		questionTitleLabel.setMaxWidth(WINDOW_WIDTH * 0.65);
		questionTitleLabel.setMinWidth(WINDOW_WIDTH * 0.65);
		questionTitleLabel.setWrapText(true);
		GridPane.setConstraints(questionTitleLabel, 1, 0);
		
		questionTitleLabel.setOnMouseClicked(e -> {
			currentQuestion = question;
			blowupQuestion(question);
		});
	
		
		Label authorLabel = new Label("Posted by " + question.getAuthor().getUsername());
		authorLabel.setAlignment(Pos.CENTER_RIGHT);
		authorLabel.setMinWidth(WINDOW_WIDTH * 0.18);
		authorLabel.setMaxWidth(WINDOW_WIDTH * 0.18);
		authorLabel.setWrapText(true);
		GridPane.setConstraints(authorLabel, 2, 0);
		
		Label questionDesc = new Label(question.getDescription());
		questionDesc.setMaxWidth(WINDOW_WIDTH * 0.3);
		questionDesc.setMinWidth(WINDOW_WIDTH * 0.3);
		questionDesc.setWrapText(true);
		GridPane.setConstraints(questionDesc, 1, 1);
		
		postPane.getChildren().addAll(upvoteBtn, downvoteBtn, questionTitleLabel, authorLabel, questionDesc);
		

		return postPane;
	}
	
	
	private GridPane buildPostPane(Post post) {
		boolean isQuestion = post.getClass().equals(Question.class);
		
		GridPane postPane = new GridPane();
			
		// top, right, bottom, left padding
		if(isQuestion) {
			postPane.setPadding(new Insets(2, 2, 2, 2));
		} else {
			postPane.setPadding(new Insets(2, 2, 2, 30));
		}
		
		postPane.setVgap(8);
		postPane.setHgap(8);
		
		ToggleButton upvoteBtn = new ToggleButton("\u25B2");
		GridPane.setConstraints(upvoteBtn, 0, 0);
		upvoteBtn.setId("upvoteButton");
		
		ToggleButton downvoteBtn = new ToggleButton("\u25BC");
		GridPane.setConstraints(downvoteBtn, 0, 1);
		downvoteBtn.setId("downvoteButton");
	
		if(post.getCurrentUserVote() == 1) {
			upvoteBtn.setSelected(true);
		} else if (post.getCurrentUserVote() == -1) {
			downvoteBtn.setSelected(true);
		}
		
		upvoteBtn.setOnAction(e -> {
			if(upvoteBtn.isSelected()) {
				if(downvoteBtn.isSelected()) {
					downvoteBtn.setSelected(false);
				}
			
				VoteControl.addVote(new Vote(post, LoginControl.getUser(), VoteType.UPVOTE));
				post.setCurrentUserVote(VoteType.UPVOTE);
			} else {
				VoteControl.addVote(new Vote(post, LoginControl.getUser(), VoteType.RESET_VOTE));
				post.setCurrentUserVote(VoteType.RESET_VOTE);
			}
		});
		
		downvoteBtn.setOnAction(e -> {
			if(downvoteBtn.isSelected()) {
				if(downvoteBtn.isSelected()) {
					upvoteBtn.setSelected(false);
				}
				
				VoteControl.addVote(new Vote(post, LoginControl.getUser(), VoteType.DOWNVOTE));
				post.setCurrentUserVote(VoteType.DOWNVOTE);
			} else {
				VoteControl.addVote(new Vote(post, LoginControl.getUser(), VoteType.RESET_VOTE));
				post.setCurrentUserVote(VoteType.RESET_VOTE);
			}
		});
		
		Label questionTitleLabel = null;
		if(isQuestion) {
			questionTitleLabel = new Label(((Question) post).getTitle());
			questionTitleLabel.setStyle("-fx-font-size: 15px;\r\n" + 
					"-fx-font-family: Verdana;"
					+ "-fx-font-weight: bold;");
			questionTitleLabel.setMaxWidth(WINDOW_WIDTH * 0.7);
			questionTitleLabel.setMinWidth(WINDOW_WIDTH * 0.7);
			questionTitleLabel.setWrapText(true);
			GridPane.setConstraints(questionTitleLabel, 1, 0);
		}
		
		Label authorLabel = new Label("Posted by " + post.getAuthor().getUsername());
		authorLabel.setAlignment(Pos.CENTER_RIGHT);
		authorLabel.setMinWidth(WINDOW_WIDTH * 0.18);
		authorLabel.setMaxWidth(WINDOW_WIDTH * 0.18);
		authorLabel.setWrapText(true);
		GridPane.setConstraints(authorLabel, 2, 0);
		
		Label questionDesc = new Label(post.getDescription());
		questionDesc.setMaxWidth(WINDOW_WIDTH * 0.7);
		questionDesc.setMinWidth(WINDOW_WIDTH * 0.7);
		questionDesc.setWrapText(true);
		GridPane.setConstraints(questionDesc, 1, 1);
		
		postPane.getChildren().addAll(upvoteBtn, downvoteBtn, authorLabel, questionDesc);
		
		if(isQuestion) {
			postPane.getChildren().add(questionTitleLabel);
		}
		
		return postPane;
	}
	
}