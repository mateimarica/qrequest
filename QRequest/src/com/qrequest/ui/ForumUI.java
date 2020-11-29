package com.qrequest.ui;

import java.util.ArrayList;

import com.qrequest.control.DeletePostControl;
import com.qrequest.control.EditPostControl;
import com.qrequest.control.GetAnswerControl;
import com.qrequest.control.GetQuestionControl;
import com.qrequest.control.LoginControl;
import com.qrequest.control.CreateAnswerControl;
import com.qrequest.control.CreateQuestionControl;
import com.qrequest.control.ThemeHelper;
import com.qrequest.control.VoteControl;
import com.qrequest.objects.Answer;
import com.qrequest.objects.Credentials;
import com.qrequest.objects.Post;
import com.qrequest.objects.Question;
import com.qrequest.objects.Vote;
import com.qrequest.objects.Vote.VoteType;

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

public class ForumUI {
	
	/**The ListView that listed the front page questions or the individual question + answers.*/
	private ListView<BorderPane> postList;
	
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
	
	/**The root pane, i.e. what every JavaFX control is placed on*/
	private BorderPane root;
	
	/**The stage*/
	private Stage stage;
	
	/**Called by LoginUI to start the scene*/
	public void startScene(Stage stage) {
		startScene(stage, null);
	}
	
	/**Starts the scene.
	 * @param stage The primary stage.
	 * @param currentQuestion If this is not <code>null</code>, blows up this question immediately.
	 */
	public void startScene(Stage stage, Question currentQuestion) {
		this.stage = stage;
		
		if(currentQuestion != null) {
			this.currentQuestion = currentQuestion;
		}	
		
		root = new BorderPane();
		
		postList = new ListView<>();
		postList.setFocusTraversable(false);
		
		populateMenuBar();
		
		root.setCenter(postList);

		refresh();
		
		bottomBar = new ToolBar();
		populateToolbar();		
		
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		scene.getStylesheets().add(ThemeHelper.getCurrentThemeURL());
		
		stage.setScene(scene);
		stage.show();		
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
		optionsMenu.getItems().add(darkModeItem);
		
		menuBar.getMenus().addAll(accountMenu, optionsMenu);
		root.setTop(menuBar);
	}

	/**Called when the Search Users button is pressed. Triggers a pop-up window*/
	private void searchUsersBtnPress() {
		PopupUI.displaySearchUsersDialog();
	}
	
	/**Populates the bottom toolBar and places at the bottom of the root BorderPane. */
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
			
			bottomBar.getItems().addAll(postAnswerBtn);
			
		}
		//Always display this button on the bottomBar:
		refreshBtn = new Button("\uD83D\uDDD8 Refresh");
		refreshBtn.setOnAction(e -> refresh());
		bottomBar.getItems().add(refreshBtn);
		
		root.setBottom(bottomBar); //Put the bottomBar on the bottom of the root pane
	}
	
	/**Called when the back button is pressed. Sets current question to null.*/
	private void processBackButtonPress() {
		currentQuestion = null;
		backBtnPress();
	}
	
	/**Called when the Ask Question button is clicked.<br>
	 * Triggers a pop-up window where the user can create a question.
	 * If the user doesn't cancel, the question is created in the database and displayed on the UI.
	 */
	private void processAskQuestionButtonPress() {
		Question newQuestion = PopupUI.displayAskQuestionDialog();
		if(newQuestion != null) {
			CreateQuestionControl.processPostQuestion(newQuestion);
			refresh();
		}
	}
	
	/**Called when the Ask Answer button is clicked.<br>
	 * Triggers a pop-up window where the user can create a answer.
	 * If the user doesn't cancel, the answer is created in the database and displayed on the UI.
	 */
	private void processPostAnswerButtonPress() {
		Answer newAnswer = PopupUI.displayPostAnswerDialog(currentQuestion);
		if(newAnswer != null) {
			CreateAnswerControl.processPostAnswer(newAnswer);
			refresh();
		}
	}
	
	/**Called when the user clicks "Log out" in the menu bar.<br>
	 * Clears the saved credentials and user object, and starts the LoginUI scene.
	 */
	private void logout() {
		Credentials.removeCredentials();
		LoginControl.resetUser();
		new LoginUI().startScene(stage);
	}
	
	/**Refreshes the current window, usually to display updated objects.
	 * If a question was in view when this method was called, the question will be reopened.
	 */
	private void refresh() {
		if(currentMode == Mode.FRONT_PAGE) {
			createQuestionsList();
		} else {
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
	private void createQuestionsList() {
		postList.getItems().clear();
        ArrayList<Question> questionList = GetQuestionControl.getAllQuestions();
        
        for(int i = 0; i < questionList.size(); i++) {
        	postList.getItems().add(buildQuestionPane(questionList.get(i)));
        	
        }
        
	}
	
	/**Displays a single question and all of its answers in the postList.
	 * @param question The question that is to be displayed.
	*/
	private void blowupQuestion(Question question) {
		currentMode = Mode.QUESTION_VIEWER;
		populateToolbar();
		
		//Refreshes the question in case something changed between the question retrieval and this method call.
		GetQuestionControl.refreshQuestion(question); 
		
		populateQuestion(question);
	}
	
	/**Populates the listView with the question and all of its answers.
	 * @param question The question that is to be displayed.
	 */
	private void populateQuestion(Question question) {
		postList.getItems().clear();
		postList.getItems().add(buildPostPane(question));
		
		ArrayList<Answer> answerList = GetAnswerControl.getAllAnswers(question);
		
		for(int i = 0; i < answerList.size(); i++) {
			postList.getItems().add(buildPostPane(answerList.get(i)));
		}
	}
	
	/**Called when the back button is pressed*/
	private void backBtnPress() {
		
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
	
		
		Insets insets = new Insets(30);
		
		Label votesLabel = new Label(question.getVotes() + "");
		formatVotesLabel(votesLabel, 0);
		
		if(question.getVotes() == 0) {
			votesLabel.setId("votesTickerZero");
		} else if (question.getVotes() > 0) {
			votesLabel.setId("votesTickerPositive");
		} else {
			votesLabel.setId("votesTickerNegative");
		}
		
		VBox votesPane = new VBox(votesLabel);
		votesPane.setAlignment(Pos.CENTER);
		//top, right, bottom, left
		votesPane.setPadding(new Insets(25, 10, 25, 10));
		questionPane.setLeft(votesPane);

		
		ToggleButton upvoteBtn = new ToggleButton("\u25B2");
		upvoteBtn.setId("upvoteButton");
		
		ToggleButton downvoteBtn = new ToggleButton("\u25BC");
		downvoteBtn.setId("downvoteButton");
	
		VBox voteButtonsPane = new VBox(upvoteBtn, downvoteBtn);
		voteButtonsPane.setSpacing(5);
		voteButtonsPane.setAlignment(Pos.CENTER);
		
		BorderPane innerPostPane = new BorderPane();
		innerPostPane.setLeft(voteButtonsPane);
		questionPane.setCenter(innerPostPane);
		
		
		if(question.getCurrentUserVote() == 1) {
			upvoteBtn.setSelected(true);
		} else if (question.getCurrentUserVote() == -1) {
			downvoteBtn.setSelected(true);
		}
		
		upvoteBtn.setOnAction(e -> {
			if(upvoteBtn.isSelected()) {
				if(downvoteBtn.isSelected()) {
					downvoteBtn.setSelected(false);
					formatVotesLabel(votesLabel, +2);
				} else {
					formatVotesLabel(votesLabel, +1);
				}
				
				
				VoteControl.addVote(new Vote(question, LoginControl.getUser(), VoteType.UPVOTE));
				question.setCurrentUserVote(VoteType.UPVOTE);
			} else {
				
				formatVotesLabel(votesLabel, -1);
				VoteControl.addVote(new Vote(question, LoginControl.getUser(), VoteType.RESET_VOTE));
				question.setCurrentUserVote(VoteType.RESET_VOTE);
			}
			
			int votes = getVoteCount(votesLabel);
			if(votes == 0) {
				votesLabel.setId("votesTickerZero");
			} else if (votes > 0) {
				votesLabel.setId("votesTickerPositive");
			} else {
				votesLabel.setId("votesTickerNegative");
			}
		});
		
		downvoteBtn.setOnAction(e -> {
			if(downvoteBtn.isSelected()) {
				if(upvoteBtn.isSelected()) {
					upvoteBtn.setSelected(false);
					formatVotesLabel(votesLabel, -2);
				} else {
					formatVotesLabel(votesLabel, -1);
				}
				
				
				VoteControl.addVote(new Vote(question, LoginControl.getUser(), VoteType.DOWNVOTE));
				question.setCurrentUserVote(VoteType.DOWNVOTE);
			} else {
				
				formatVotesLabel(votesLabel, +1);
				VoteControl.addVote(new Vote(question, LoginControl.getUser(), VoteType.RESET_VOTE));
				question.setCurrentUserVote(VoteType.RESET_VOTE);
			}
			
			int votes = getVoteCount(votesLabel);
			if(votes == 0) {
				votesLabel.setId("votesTickerZero");
			} else if (votes > 0) {
				votesLabel.setId("votesTickerPositive");
			} else {
				votesLabel.setId("votesTickerNegative");
			}
		});
		
		Label questionTitleLabel = new Label(question.getTitle());
		questionTitleLabel.setId("questionTitleLabel");
		questionTitleLabel.setPadding(new Insets(0, 0, 0, 10));
		questionTitleLabel.setMaxWidth(WINDOW_WIDTH * 0.65);
		questionTitleLabel.setMinWidth(WINDOW_WIDTH * 0.65);
		questionTitleLabel.setWrapText(true);
		
		
		questionTitleLabel.setOnMouseClicked(e -> {
			currentQuestion = question;
			blowupQuestion(question);
		});
		
		GridPane innerInnerPostPane = new GridPane();
		innerInnerPostPane.setPadding(new Insets(5, 10, 10, 10)); //top, right, bottom, left
		GridPane.setConstraints(questionTitleLabel, 0, 0);
		innerPostPane.setCenter(innerInnerPostPane);
		
		Label authorLabel = new Label("Posted " + question.getTimePosted() + " by " + question.getAuthor().getUsername());
		authorLabel.setPadding(new Insets(10, 0, 10, 20));
		authorLabel.setMinWidth(WINDOW_WIDTH * 0.45);
		authorLabel.setMaxWidth(WINDOW_WIDTH * 0.45);
		authorLabel.setWrapText(true);
		GridPane.setConstraints(authorLabel, 0, 1);
		
		Label answersCountLabel = new Label(question.getAnswerCount() + " responses");
		answersCountLabel.setPadding(new Insets(0, 0, 0, 20));
		answersCountLabel.setMinWidth(WINDOW_WIDTH * 0.18);
		answersCountLabel.setMaxWidth(WINDOW_WIDTH * 0.18);
		answersCountLabel.setWrapText(true);
		GridPane.setConstraints(answersCountLabel, 0, 2);
		
		innerInnerPostPane.getChildren().addAll(questionTitleLabel, authorLabel, answersCountLabel);
		
		
		
		return questionPane;
	}
	
	/**Edits the vote count label for post to display a user's vote immediately.
	 * @param label The vote count Label object.
	 * @param offset How much the vote count changed by.<br>(Example: neutral -> positive = +1)
	 */
	private void formatVotesLabel(Label label, int offset) {
		int labelAsInt = getVoteCount(label);
		label.setText(labelAsInt + offset + ((labelAsInt + offset) < 0 ? "" : " "));
	}
	
	/**Gets the votes down of a vote Label.
	 * 
	 * @param label The label whose vote count is being gotten.
	 * @return The votes count.
	 */
	private int getVoteCount(Label label) {
		return Integer.parseInt(label.getText().trim());
	}
	
	/**Builds a post for a Question <i>or</i> Answer, for use in a blown-up Question.
	 * @param post The 
	 * @return
	 */
	/**Builds a pane for a Question <i>or</i> Answer, for use in a blown-up Question. To be put on the postList.
	 * @param question The <code>Post</code> that is to be made into a BorderPane.
	 * @return The constructed postPane.
	 */
	private BorderPane buildPostPane(Post post) {
		
		boolean isQuestion = post.isQuestion();
		
		BorderPane postPane = new BorderPane();
		
		postPane.setPadding(new Insets(2, 2, 2, 2));
	
		
		Label votesLabel = new Label(post.getVotes() + "");
		formatVotesLabel(votesLabel, 0);
		
		if(post.getVotes() == 0) {
			votesLabel.setId("votesTickerZero");
		} else if (post.getVotes() > 0) {
			votesLabel.setId("votesTickerPositive");
		} else {
			votesLabel.setId("votesTickerNegative");
		}
		
		VBox votesPane = new VBox(votesLabel);
		votesPane.setAlignment(Pos.TOP_CENTER);
		//top, right, bottom, left
		
		votesPane.setPadding(new Insets(30, 10, 25, isQuestion ? 10 : 40));
		postPane.setLeft(votesPane);

		
		ToggleButton upvoteBtn = new ToggleButton("\u25B2");
		upvoteBtn.setId("upvoteButton");
		
		ToggleButton downvoteBtn = new ToggleButton("\u25BC");
		downvoteBtn.setId("downvoteButton");
	
		VBox voteButtonsPane = new VBox(upvoteBtn, downvoteBtn);
		voteButtonsPane.setSpacing(5);
		voteButtonsPane.setPadding(new Insets(10, 0, 0, 0));
		voteButtonsPane.setAlignment(Pos.TOP_CENTER);
		
		BorderPane innerPostPane = new BorderPane();
		innerPostPane.setLeft(voteButtonsPane);
		postPane.setCenter(innerPostPane);
		
		
		if(post.getCurrentUserVote() == 1) {
			upvoteBtn.setSelected(true);
		} else if (post.getCurrentUserVote() == -1) {
			downvoteBtn.setSelected(true);
		}
		
		upvoteBtn.setOnAction(e -> {
			if(upvoteBtn.isSelected()) {
				if(downvoteBtn.isSelected()) {
					downvoteBtn.setSelected(false);
					formatVotesLabel(votesLabel, +2);
				} else {
					formatVotesLabel(votesLabel, +1);
				}
				
				
				VoteControl.addVote(new Vote(post, LoginControl.getUser(), VoteType.UPVOTE));
				post.setCurrentUserVote(VoteType.UPVOTE);
			} else {
				
				formatVotesLabel(votesLabel, -1);
				VoteControl.addVote(new Vote(post, LoginControl.getUser(), VoteType.RESET_VOTE));
				post.setCurrentUserVote(VoteType.RESET_VOTE);
			}
			
			int votes = getVoteCount(votesLabel);
			if(votes == 0) {
				votesLabel.setId("votesTickerZero");
			} else if (votes > 0) {
				votesLabel.setId("votesTickerPositive");
			} else {
				votesLabel.setId("votesTickerNegative");
			}
		});
		
		downvoteBtn.setOnAction(e -> {
			if(downvoteBtn.isSelected()) {
				if(upvoteBtn.isSelected()) {
					upvoteBtn.setSelected(false);
					formatVotesLabel(votesLabel, -2);
				} else {
					formatVotesLabel(votesLabel, -1);
				}
				
				
				VoteControl.addVote(new Vote(post, LoginControl.getUser(), VoteType.DOWNVOTE));
				post.setCurrentUserVote(VoteType.DOWNVOTE);
			} else {
				
				formatVotesLabel(votesLabel, +1);
				VoteControl.addVote(new Vote(post, LoginControl.getUser(), VoteType.RESET_VOTE));
				post.setCurrentUserVote(VoteType.RESET_VOTE);
			}
			
			int votes = getVoteCount(votesLabel);
			if(votes == 0) {
				votesLabel.setId("votesTickerZero");
			} else if (votes > 0) {
				votesLabel.setId("votesTickerPositive");
			} else {
				votesLabel.setId("votesTickerNegative");
			}
		});
		
		
		GridPane innerInnerPostPane = new GridPane();
		innerInnerPostPane.setVgap(5);
		innerInnerPostPane.setPadding(new Insets(5, 10, 10, 10)); //top, right, bottom, left
		innerPostPane.setCenter(innerInnerPostPane);
		
		
		if(isQuestion) {
			Label questionTitleLabel = new Label(((Question)post).getTitle());
			questionTitleLabel.setId("questionTitleLabel");
			questionTitleLabel.setPadding(new Insets(0, 0, 0, 10));
			questionTitleLabel.setMaxWidth(WINDOW_WIDTH * 0.65);
			questionTitleLabel.setMinWidth(WINDOW_WIDTH * 0.65);
			questionTitleLabel.setWrapText(true);
			GridPane.setConstraints(questionTitleLabel, 0, 0);
			innerInnerPostPane.getChildren().add(questionTitleLabel);
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
		
		VBox buttonPane = new VBox(10);
		buttonPane.setPadding(new Insets(5, 5, 5, 5));
		if(LoginControl.getUser().getUsername().equals(post.getAuthor().getUsername())) {
			Button editButton = new Button();
			editButton.setPrefSize(30, 30);
			editButton.setId("editButton");
			editButton.setTooltip(new Tooltip("Edit post"));
			editButton.setOnAction(e -> {
				if(PopupUI.displayEditQuestionDialog(post)) {
					EditPostControl.processEditPost(post);
					refresh();
				}
			});
			
			Button deleteButton = new Button();
			deleteButton.setPrefSize(30, 30);
			deleteButton.setId("deleteButton");
			deleteButton.setTooltip(new Tooltip("Delete post"));	
			deleteButton.setOnAction(e -> {
				if(PopupUI.displayConfirmationDialog("Confirm Deletion", "Are you sure you want to delete this post? This cannot be undone!")) {
					DeletePostControl.processDeletePost(post);
					if(isQuestion) {
						currentQuestion = null;
						backBtnPress();
					} else {
						refresh();
					}
				}
			});
			
			buttonPane.getChildren().addAll(editButton, deleteButton);
			
			
		} else {
			Button reportButton = new Button("\uD83D\uDDE3");
			reportButton.setPrefSize(30, 30);
			reportButton.setId("reportButton");
			reportButton.setTooltip(new Tooltip("Report post"));
			reportButton.setOnAction(e -> {
				//report button function goes here
			});
			
			buttonPane.getChildren().addAll(reportButton);
		}
		postPane.setRight(buttonPane);
		
		return postPane;
	}
	
}