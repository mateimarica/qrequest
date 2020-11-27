package com.qrequest.ui;
import java.io.IOException;
import java.util.ArrayList;

import com.qrequest.control.GetAnswerControl;
import com.qrequest.control.GetQuestionControl;
import com.qrequest.control.LoginControl;
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
	
	private ListView<GridPane> questionsTable;
	
	
	
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
		} catch (IOException e1) { e1.printStackTrace(); }
		
		
		questionView = new ListView<GridPane>();
		
		if(currentMode == Mode.FRONT_PAGE) {
			createQuestionsTable();	
		} else {
			blowupQuestion(currentQuestion);
		}

		populateToolbar();
		
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		if(ThemeHelper.darkModeEnabled())
			scene.getStylesheets().add(ThemeHelper.darkThemeFileURL);
		else
			scene.getStylesheets().add(ThemeHelper.lightThemeFileURL);
			
		
		stage.setScene(scene);
		stage.show();
		System.out.println(menuBar);
		
	}
	
	@FXML
	public void initialize(){
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
		bottomBar = new ToolBar();
		
		
		if(currentMode == Mode.FRONT_PAGE) {
			askQuestionBtn = new Button("\u2795 Ask a question");
			askQuestionBtn.setOnAction(e -> PopupUI.displayPostQuestionDialog(this));
			searchUsersBtn = new Button("\uD83D\uDC64 Search Users");
			searchUsersBtn.setOnAction(e -> searchUsersBtnPress());
			bottomBar.getItems().addAll(askQuestionBtn, searchUsersBtn);
			
			
		} else {
			Button backBtn = new Button("\u25C0 Go back");
			backBtn.setOnAction(e -> {
				currentQuestion = null;
				backBtnPress();
			});
			bottomBar.getItems().add(backBtn);
			
			Button postAnswerBtn = new Button("\u2795 Answer question");
			postAnswerBtn.setOnAction(e -> PopupUI.displayPostAnswerDialog(this, currentQuestion));
			
			if(currentQuestion.getAuthor().getUsername().equals(LoginControl.getUser().getUsername())) {
				Button deleteBtn = new Button("\uD83D\uDDD1 Delete question");
				deleteBtn.setOnAction(e -> {
					PopupUI.displayConfirmDeletionDialog(this, currentQuestion);
				});

				Button editBtn = new Button("\uD83D\uDD89 Edit Question");
				editBtn.setOnAction(e -> {
					PopupUI.displayEditQuestionDialog(this, currentQuestion);
					refresh();
				});
				bottomBar.getItems().addAll(deleteBtn, editBtn);
			}
			
			
			bottomBar.getItems().addAll(postAnswerBtn);
			
		}
		
		refreshBtn = new Button("\uD83D\uDDD8 Refresh");
		refreshBtn.setOnAction(e -> refresh());
		bottomBar.getItems().add(refreshBtn);
		
		root.setBottom(bottomBar);
	}
	
	private void logout() {
		Credentials.removeCredentials();
		LoginControl.resetUser();
		new LoginUI().startScene(MainUI.stage);
	}
	

	
	void refresh() {
		if(currentMode == Mode.FRONT_PAGE) {
			root.getChildren().remove(questionsTable);
			createQuestionsTable();
		} else {
			blowupQuestion(currentQuestion);
		}
	}
	
	private void themeChanged(CheckMenuItem darkModeItem) {
		ThemeHelper.saveTheme(darkModeItem.isSelected());
		startScene(MainUI.stage);
	}
	
	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	private void createQuestionsTable() {
		questionsTable = new ListView();
		
		
	
		questionsTable.setFocusTraversable(false);
		
        ArrayList<Question> questionList = GetQuestionControl.getAllQuestions();
        
        for(int i = 0; i < questionList.size(); i++) {
        	questionsTable.getItems().add(buildQuestionPane(questionList.get(i)));
        	
        }
   
       
        root.setCenter(questionsTable);

	}
	
	private void blowupQuestion(Question question) {
		 
		
		currentMode = Mode.QUESTION_VIEWER;
		populateToolbar();
		
		
		questionsTable.setVisible(false);
		questionView.setVisible(true);
		
		GetQuestionControl.refreshQuestion(question);
		
		
		populateQuestion(question);
	}
	
	private void populateQuestion(Question question) {
		questionView.getItems().clear();
		questionView.getItems().add(buildPostPane(currentQuestion));
		
		ArrayList<Answer> answerList = GetAnswerControl.getAllAnswers(currentQuestion);
		
		for(int i = 0; i < answerList.size(); i++) {
			questionView.getItems().add(buildPostPane(answerList.get(i)));
		}
		
		
		root.setCenter(questionView);
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