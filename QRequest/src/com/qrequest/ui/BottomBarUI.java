package com.qrequest.ui;

import com.qrequest.helpers.LanguageManager;
import com.qrequest.objects.Question;
import com.qrequest.ui.ForumUI.Mode;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

public class BottomBarUI {
	
	/**The toolbar are at the bottom of the window containing buttons like "Ask Question" and "Refresh"*/
	private ToolBar bottomBar;
	private ForumUI forumUI;
	
	public BottomBarUI(ForumUI forumUI) {
		this.forumUI = forumUI;
		bottomBar = new ToolBar();
		repopulateBottomBar();
	}
	
	/**Populates the bottom toolBar and places at the bottom of the root BorderPane. */
	void repopulateBottomBar() {
		bottomBar.getItems().clear();
		
		Mode currentMode = forumUI.getMode();
		
		if(currentMode == Mode.FRONT_PAGE) { //If the app is on the front page, display these buttons on the bottomBar:
			Button askQuestionBtn = new AskQuestionUI(forumUI).getAskQuestionButton();
			Button messagingBtn = new MessagingUI().getMessagingButton();
			Button searchUsersBtn = new SearchUsersUI().getSearchUsersButton();
			Button searchQuestionsBtn = new SearchQuestionsUI(forumUI).getSearchQuestionsButton();
			
			bottomBar.getItems().addAll(askQuestionBtn, searchUsersBtn, searchQuestionsBtn, messagingBtn);
			
		} else {//If the app is in the question viewer, display these buttons on the bottomBar:
			Button backBtn = new Button("\u25C0 " + LanguageManager.getString("backButton"));
			backBtn.setOnAction(e -> backButtonPress());
			bottomBar.getItems().add(backBtn);
			
			if(currentMode == Mode.QUESTION_VIEWER) {
				
				Button postAnswerBtn = new PostAnswerUI(forumUI.getCurrentQuestion(), forumUI).getPostAnswerButton();
				
				bottomBar.getItems().addAll(postAnswerBtn);
			}
			
		}
		//Always display this button on the bottomBar:
		Button refreshBtn;
		refreshBtn = new Button("\uD83D\uDDD8 " +  LanguageManager.getString("refreshButton"));
		refreshBtn.setOnAction(e -> refreshButtonPress());
		bottomBar.getItems().add(refreshBtn);
	}
	
	public ToolBar getBottomBar() {
		repopulateBottomBar();
		return bottomBar;
	}
	
	
	
	private void refreshButtonPress() {
		forumUI.refresh();
	}
	
	private void backButtonPress() {
		forumUI.backButtonPress();
		repopulateBottomBar();
	}
	
}
