package com.qrequest.ui;

import java.util.ArrayList;


import com.qrequest.control.GetAnswerControl;
import com.qrequest.control.GetQuestionControl;
import com.qrequest.control.LoginControl;
import com.qrequest.helpers.ThemeHelper;
import com.qrequest.objects.Answer;
import com.qrequest.objects.Credentials;
import com.qrequest.objects.Post;
import com.qrequest.objects.Question;

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
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**The forum UI. This class handles the displaying of questions and answers.*/
public class ForumUI {
	
	/**The ListView that listed the front page questions or the individual question + answers.*/
	private ListView<BorderPane> postList;
	
	/**The dimensions of the window. Don't change this*/
	private final int WINDOW_HEIGHT = 700, WINDOW_WIDTH = 800; 
	
	/**Enumerator defining which "mode" the forum is in.*/
	private enum Mode {
		/**Represents the state where the the program is on the front page.*/
		FRONT_PAGE, 
		
		/**Represents the state where the program is viewing an individual question & all of its answers.*/
		QUESTION_VIEWER
	};
	
	/**The current mode that the forum is in. Front page is default.*/
	private Mode currentMode = Mode.FRONT_PAGE;
	
	/**Reference to the current question that is being interacted with*/
	private Question currentQuestion;
	
	/**The toolbar are at the bottom of the window containing buttons like "Ask Question" and "Refresh"*/
	private ToolBar bottomBar;
	
	/**Button to refresh the current post list*/
	private Button refreshBtn;
	
	/**The root pane, i.e. what every JavaFX control is placed on*/
	private BorderPane root;
		
	/**The stage*/
	private Stage stage;
	
	/**Called by LoginUI to start the scene*/
	void startScene(Stage stage) {
		startScene(stage, null);
	}
	
	/**Starts the scene.
	 * @param stage The primary stage.
	 * @param currentQuestion If this is not <code>null</code>, blows up this question immediately.
	 */
	void startScene(Stage stage, Question currentQuestion) {
		this.stage = stage;
		
		if(currentQuestion != null) {
			this.currentQuestion = currentQuestion;
		}	
		
		
		root = new BorderPane();
		
		postList = new ListView<>();
		postList.setFocusTraversable(false);
		root.setCenter(postList);
		
		populateMenuBar();
		
		refresh();
		
		bottomBar = new ToolBar();
		populateToolbar();		
		
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		scene.getStylesheets().add(ThemeHelper.getCurrentThemeURL());
		
		stage.setScene(scene);
		stage.show();	
		stage.setResizable(true);
		stage.setMinHeight(300);
		stage.setMinWidth(800);
	}

	/**Populates the menuBar and places it at the top of the root BorderPane.*/
	private void populateMenuBar() {
		MenuBar menuBar = new MenuBar();
		
		Menu accountMenu = new Menu("Logged in as: " + LoginControl.getUser().getUsername());
		MenuItem logoutItem = new MenuItem("Log out");
		logoutItem.setOnAction(e -> logout());
		accountMenu.getItems().add(logoutItem);
		
		Menu optionsMenu = new Menu("Options");
		CheckMenuItem darkModeItem = new CheckMenuItem("Dark mode");
		darkModeItem.setOnAction(e -> themeChanged(darkModeItem.isSelected()));
		darkModeItem.setSelected(ThemeHelper.isDarkModeEnabled());
		optionsMenu.getItems().addAll(darkModeItem);
		
		menuBar.getMenus().addAll(accountMenu, optionsMenu);
		root.setTop(menuBar);
	}

	/**Populates the bottom toolBar and places at the bottom of the root BorderPane. */
	void populateToolbar() {
		bottomBar.getItems().clear();
		
		if(currentMode == Mode.FRONT_PAGE) { //If the app is on the front page, display these buttons on the bottomBar:
			Button askQuestionBtn = new AskQuestionUI(this).getAskQuestionButton();
			Button messagingBtn = new MessagingUI().getMessagingButton();
			Button searchUsersBtn = new SearchUsersUI().getSearchUsersButton();
			Button searchQuestionsBtn = new SearchQuestionsUI(this).getSearchQuestionsButton();
			
			bottomBar.getItems().addAll(askQuestionBtn, searchUsersBtn, searchQuestionsBtn, messagingBtn);
			
		} else {//If the app is in the question viewer, display these buttons on the bottomBar:
			Button backBtn = new Button("\u25C0 Go back");
			backBtn.setOnAction(e -> backButtonPress());
			bottomBar.getItems().add(backBtn);
			
			if(currentMode == Mode.QUESTION_VIEWER) {
				Button postAnswerBtn = new PostAnswerUI(currentQuestion, this).getPostAnswerButton();
				
				bottomBar.getItems().addAll(postAnswerBtn);
			}
			
		}
		//Always display this button on the bottomBar:
		refreshBtn = new Button("\uD83D\uDDD8 Refresh");
		refreshBtn.setOnAction(e -> refresh());
		bottomBar.getItems().add(refreshBtn);
		
		root.setBottom(bottomBar); //Put the bottomBar on the bottom of the root pane
	}
	
	
	/**Called when the user clicks "Log out" in the menu bar.<br>
	 * Clears the saved credentials and user object, and starts the LoginUI scene.
	 */
	private void logout() {
		stage.close();
		Credentials.removeCredentials();
		LoginControl.resetUser();
		new LoginUI().startScene(MainUI.setUpStage(new Stage()));
	}
	
	/**Refreshes the current window, usually to display updated objects.
	 * If a question was in view when this method was called, the question will be reopened.
	 */
	void refresh() {
		if(currentMode == Mode.FRONT_PAGE) {
			createQuestionsList(new GetQuestionControl().getAllQuestions());
		} else if(currentMode == Mode.QUESTION_VIEWER) {
			blowupQuestion(currentQuestion);
		}
	}
	
	/**Called when the <b>Dark mode</b> button is clicked.<br>
	 * Saves the theme and restarts the scene to apply the new theme.
	 * @param darkThemeEnabled The result of <code>isSelected()</code> on the darkModeItem CheckBoxItem.
	 */
	private void themeChanged(boolean darkThemeEnabled) {
		ThemeHelper.saveTheme(darkThemeEnabled);
		startScene(stage, currentQuestion);
	}
	
	/**Displays a list of questions in the postList.*/
	void createQuestionsList(ArrayList<Question> questionList) {
		postList.getItems().clear();
		
        if(questionList.size() == 0) {
        	postList.getItems().add(buildEmptyPane());
        } else {
        	for(int i = 0; i < questionList.size(); i++) {
    			postList.getItems().add(buildQuestionPane(questionList.get(i)));
    		}
        }
	}
	
	/**Displays a single question and all of its answers in the postList.
	 * @param question The question that is to be displayed.
	*/
	private void blowupQuestion(Question question) {
		currentMode = Mode.QUESTION_VIEWER;
		populateToolbar();
		
		//Refreshes the question in case something changed between the question retrieval and this method call.
		new GetQuestionControl().refreshQuestion(question); 
		
		populateQuestion(question);
	}
	
	/**Populates the listView with the question and all of its answers.
	 * @param question The question that is to be displayed.
	 */
	private void populateQuestion(Question question) {
		postList.getItems().clear();
		postList.getItems().add(buildPostPane(question));
		
		ArrayList<Answer> answerList = new GetAnswerControl().getAllAnswers(question);
		
		for(int i = 0; i < answerList.size(); i++) {
			postList.getItems().add(buildPostPane(answerList.get(i)));
		}
	}
	
	/**Called when the back button is pressed*/
	void backButtonPress() {
		currentQuestion = null;
		postList.getItems().clear();
		currentMode = Mode.FRONT_PAGE;
		populateToolbar();
		refresh();
	}
	
	/**Builds an individual question pane for the front page for placing on the postList
	 * @param question The question that is to be made into a BorderPane.
	 * @return The constructed postPane.
	 */
	private BorderPane buildQuestionPane(Question question) {
		
		BorderPane questionPane = new BorderPane();
		questionPane.setPadding(new Insets(2, 2, 2, 2));
		
		if(LoginControl.getUser().isAdmin()) {
			ToggleButton pinButton = new PinUI(question, this).getPinButton();
			
			VBox pinButtonsPane = new VBox(pinButton);
			pinButtonsPane.setSpacing(5);
			pinButtonsPane.setPadding(new Insets(0, 0, 0, 0));
			pinButtonsPane.setAlignment(Pos.CENTER);
			
			questionPane.setRight(pinButtonsPane);
		}
		
				
		VoteUI voteUI = new VoteUI(question); 
		
		Label votesLabel = voteUI.getVotesLabel();
		ToggleButton upvoteBtn = voteUI.getUpvoteButton();
		ToggleButton downvoteBtn = voteUI.getDownvoteButton();
				
		VBox votesPane = new VBox(votesLabel);
		votesPane.setAlignment(Pos.CENTER);
		votesPane.setPadding(new Insets(25, 10, 25, 10)); //top, right, bottom, left
		questionPane.setLeft(votesPane);		
		
		VBox voteButtonsPane = new VBox(upvoteBtn, downvoteBtn);
		voteButtonsPane.setSpacing(5);
		voteButtonsPane.setAlignment(Pos.CENTER);
		
		BorderPane innerPostPane = new BorderPane();
		innerPostPane.setLeft(voteButtonsPane);
		questionPane.setCenter(innerPostPane);
				
		GridPane innerInnerPostPane = new GridPane();
		innerInnerPostPane.setPadding(new Insets(5, 10, 10, 10)); //top, right, bottom, left
		innerPostPane.setCenter(innerInnerPostPane);
		
		Label tagLabel = new Label(question.getTag().getSymbol());
		tagLabel.setTooltip(new Tooltip(question.getTag().getDescription()));
		tagLabel.setId("tagLabel");
		tagLabel.setMinWidth(WINDOW_WIDTH * 0.03);
		tagLabel.setPadding(new Insets(0, 0, 0, 10));
		GridPane.setConstraints(tagLabel, 0, 0);
		
		Label questionTitleLabel = new Label(question.getTitle());
		questionTitleLabel.setId(question.isPinned() ? "pinnedQuestionTitleLabel" : "questionTitleLabel");
		questionTitleLabel.setPadding(new Insets(0, 0, 0, 10));
		questionTitleLabel.setMaxWidth(WINDOW_WIDTH * 0.65);
		questionTitleLabel.setMinWidth(WINDOW_WIDTH * 0.65);
		questionTitleLabel.setWrapText(true);
		GridPane.setConstraints(questionTitleLabel, 1, 0);
		questionTitleLabel.setOnMouseClicked(e -> {
			currentQuestion = question;
			blowupQuestion(question);
		});
		
		Label authorLabel = new Label("Posted " + question.getTimePosted() + " by " + question.getAuthor().getUsername());
		authorLabel.setPadding(new Insets(10, 0, 10, 20));
		authorLabel.setMaxWidth(WINDOW_WIDTH * 0.45);
		authorLabel.setWrapText(true);
		GridPane.setConstraints(authorLabel, 1, 1);
		
		Label answersCountLabel = new Label(question.getAnswerCount() + " responses");
		answersCountLabel.setPadding(new Insets(0, 0, 0, 20));
		answersCountLabel.setMinWidth(WINDOW_WIDTH * 0.18);
		answersCountLabel.setMaxWidth(WINDOW_WIDTH * 0.18);
		answersCountLabel.setWrapText(true);
		GridPane.setConstraints(answersCountLabel, 1, 2);
		
		if(question.hasSolvedAnswer()) {
			Label markedSolvedLabel = new MarkSolvedUI().getMarkedSolvedOnQuestionLabel();			
			innerInnerPostPane.add(markedSolvedLabel, 0, 1);
		}
		
		innerInnerPostPane.getChildren().addAll(tagLabel, questionTitleLabel, authorLabel, answersCountLabel);
		
		return questionPane;
	}
	
	
	/**Builds a pane for a Question <i>or</i> Answer, for use in a blown-up Question. To be put on the postList.
	 * @param question The <code>Post</code> that is to be made into a BorderPane.
	 * @return The constructed postPane.
	 */
	private BorderPane buildPostPane(Post post) {
				
		boolean isQuestion = post.isQuestion();
		
		BorderPane postPane = new BorderPane();
		postPane.setPadding(new Insets(2, 2, 2, 2));
		
		VoteUI voteUI = new VoteUI(post); 
	
		Label votesLabel = voteUI.getVotesLabel();
		
		VBox votesPane = new VBox(votesLabel);
		votesPane.setAlignment(Pos.TOP_CENTER);
		votesPane.setPadding(new Insets(30, 10, 25, isQuestion ? 10 : 40)); //top, right, bottom, left
		
		postPane.setLeft(votesPane);

		ToggleButton upvoteBtn = voteUI.getUpvoteButton();
		ToggleButton downvoteBtn = voteUI.getDownvoteButton();
	
		VBox voteButtonsPane = new VBox(upvoteBtn, downvoteBtn);
		voteButtonsPane.setSpacing(5);
		voteButtonsPane.setPadding(new Insets(10, 0, 0, 0));
		voteButtonsPane.setAlignment(Pos.TOP_CENTER);
		
		BorderPane innerPostPane = new BorderPane();
		innerPostPane.setLeft(voteButtonsPane);
		postPane.setCenter(innerPostPane);
		
		GridPane innerInnerPostPane = new GridPane();
		innerInnerPostPane.setVgap(5);
		innerInnerPostPane.setPadding(new Insets(5, 10, 10, 10)); //top, right, bottom, left
		innerPostPane.setCenter(innerInnerPostPane);
		
		if(isQuestion) {
			Label tagLabel = new Label(((Question)post).getTag().getSymbol());
			tagLabel.setTooltip(new Tooltip(((Question)post).getTag().getDescription()));
			tagLabel.setId("tagLabel");
			tagLabel.setMinWidth(WINDOW_WIDTH * 0.03);
			tagLabel.setPadding(new Insets(0, 0, 0, 10));
			GridPane.setConstraints(tagLabel, 0, 0);
			
			Label questionTitleLabel = new Label(((Question)post).getTitle());
			if(((Question)post).isPinned()) {
				questionTitleLabel.setId("pinnedQuestionTitleLabel");
			} else {
				questionTitleLabel.setId("questionTitleLabel");
			}
			questionTitleLabel.setMouseTransparent(true);
			questionTitleLabel.setPadding(new Insets(0, 0, 0, 40));
			questionTitleLabel.setMaxWidth(WINDOW_WIDTH * 0.65);
			questionTitleLabel.setMinWidth(WINDOW_WIDTH * 0.65);
			questionTitleLabel.setWrapText(true);
			GridPane.setConstraints(questionTitleLabel, 0, 0);
			innerInnerPostPane.getChildren().addAll(tagLabel, questionTitleLabel);
		}
	
		Label authorLabel = new Label("Posted " + post.getTimePosted() + " by " + post.getAuthor().getUsername());
		authorLabel.setPadding(new Insets(0, 0, 0, 20));
		authorLabel.setMinWidth(WINDOW_WIDTH * 0.45);
		authorLabel.setMaxWidth(WINDOW_WIDTH * 0.45);
		authorLabel.setWrapText(true);
		GridPane.setConstraints(authorLabel, 0, 1);

		Label descriptionLabel = new Label(post.getDescription());
		descriptionLabel.setPadding(new Insets(0, 0, 0, 20));
		descriptionLabel.setId("descriptionLabel");
		descriptionLabel.setMaxWidth(WINDOW_WIDTH * 0.65);
		descriptionLabel.setMinWidth(WINDOW_WIDTH * 0.65);
		descriptionLabel.setWrapText(true);
		GridPane.setConstraints(descriptionLabel, 0, 4);
		
		innerInnerPostPane.getChildren().addAll(authorLabel, descriptionLabel);
		
		MarkSolvedUI markSolvedUI = new MarkSolvedUI(post, this);
		
		Label markedSolvedLabel = markSolvedUI.getMarkedSolvedOnAnswerLabel();
		if(markedSolvedLabel != null) {
			VBox symbolsBox = new VBox(markedSolvedLabel);
			symbolsBox.setPadding(new Insets(0, 20, 0, 0));
			symbolsBox.setAlignment(Pos.CENTER);
			innerPostPane.setRight(symbolsBox);
		}
		
		VBox buttonPane = new VBox(10);
		buttonPane.setPadding(new Insets(5, 5, 5, 5));
		
		if(LoginControl.getUser().getUsername().equals(post.getAuthor().getUsername())) {
			Button editButton = new EditPostUI(post, this).getEditButton();
			Button deleteButton = new DeletePostUI(post, this).getDeleteButton();
			buttonPane.getChildren().addAll(editButton, deleteButton);
		} else {
			ToggleButton markSolvedButton = markSolvedUI.getMarkSolvedButton();
			if(markSolvedButton != null) {
				buttonPane.getChildren().add(markSolvedButton);
			}
								
			Button reportButton = new ReportUI(post).getReportButton();
			buttonPane.getChildren().addAll(reportButton);
		}
		
		postPane.setRight(buttonPane);
		return postPane;
	}
	
	/**Creates an empty pane for when a user's search yields no results.
	 * @return An empty post pane containing a funky message.
	 */
	private BorderPane buildEmptyPane() {
		
		BorderPane emptyPane = new BorderPane();
		
		String[] noResultsMsg = {"Hmm... There's nothing here.", 
								"No results.", 
								"You've stumbled upon a dead end."};

		Label label = new Label(noResultsMsg[(int)(Math.random() * noResultsMsg.length)]);
		label.setId("noResultsLabel");
		label.setMouseTransparent(true);
		label.setPadding(new Insets(30, 0, 30, 30));
		label.setMaxWidth(WINDOW_WIDTH * 0.65);
		label.setMinWidth(WINDOW_WIDTH * 0.65);
		emptyPane.setLeft(label);
		
		return emptyPane;
	}
	
}