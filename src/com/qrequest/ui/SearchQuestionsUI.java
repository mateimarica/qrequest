package com.qrequest.ui;

import com.qrequest.control.QuestionController;
import com.qrequest.managers.LanguageManager;
import com.qrequest.objects.QuestionSearchFilters;
import com.qrequest.objects.Tag;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

/**The UI for searching for questions.*/
public class SearchQuestionsUI {
	
	/**The button that displays the search questions window.*/
	private Button searchQuestionsBtn;
	
	/**A reference to the forum UI.*/
	private ForumUI forumUI;
	
	/**Create a SearchQuestionsUI. This creates a searchQuestionsBtn.
	 * @param forumUI *A reference to the forum UI.
	 */
	public SearchQuestionsUI(ForumUI forumUI) {
		this.forumUI = forumUI;
		
		searchQuestionsBtn = new Button("\uD83D\uDD0D " + LanguageManager.getString("searchQuestionsButton"));
		searchQuestionsBtn.setOnAction(e -> searchQuestionsBtnPress());
	}
	
	/** Returns the button that displays the search questions window.
	 * @return The button that displays the search questions window.
	 */
	public Button getSearchQuestionsButton() {
		return searchQuestionsBtn;
	}
	
	/**Triggered when the search question button is pressed.*/
	private void searchQuestionsBtnPress() {
		QuestionSearchFilters filters = displaySearchQuestionsDialog();
		if(filters != null) {
			forumUI.bottomBarUI.repopulateBottomBar();
			forumUI.createQuestionsList(QuestionController.search(filters));
		}
	}
	
	/**Displays a pop-up that allows the users to search for a specific question.
	 * @return A <code>QuestionSearchFilters</code> containg the specified filters. <code>null</code> if the user exited out.
	 */
	private QuestionSearchFilters displaySearchQuestionsDialog() {
		
		Dialog dialog = new Dialog();
		dialog.setTitle(LanguageManager.getString("searchQuestionsPopupTitle"));

		PopupUI.setupDialogStyling(dialog);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20, 10, 10, 10)); // top, right, bottom, left padding

		TextField titleField = new TextField();
		titleField.setPromptText(LanguageManager.getString("searchQuestionsFieldPrompt"));
		titleField.setPrefWidth(300);
		gridPane.add(titleField, 0, 0);
		
		ComboBox<Tag> tagTypeBox = new ComboBox<>();	
		tagTypeBox.setPromptText(LanguageManager.getString("searchQuestionsTagPrompt"));
		Tag[] tagTypes = Tag.values();
		for(int i = 0; i < tagTypes.length; i++) {
			tagTypeBox.getItems().add(tagTypes[i]);
		}
		gridPane.add(tagTypeBox, 0, 1);
		
		ComboBox<HasAnswerOption> hasBeenAnsweredBox = new ComboBox<>();
		hasBeenAnsweredBox.setPromptText(LanguageManager.getString("searchQuestionsHasAnswerPrompt"));
		hasBeenAnsweredBox.getItems().addAll(HasAnswerOption.values());
		gridPane.add(hasBeenAnsweredBox, 0, 2);

		// Set the button types.
		ButtonType searchBtnType = new ButtonType(LanguageManager.getString("searchQuestionsButton"), ButtonData.RIGHT);
		dialog.getDialogPane().getButtonTypes().addAll(searchBtnType, ButtonType.CANCEL);

		final Button searchBtn = (Button)dialog.getDialogPane().lookupButton(searchBtnType);
		searchBtn.addEventFilter(ActionEvent.ACTION, e -> {
			if(titleField.getText().length() == 0 && tagTypeBox.getSelectionModel().isEmpty() && hasBeenAnsweredBox.getSelectionModel().isEmpty()) {
				PopupUI.displayErrorDialog(
						LanguageManager.getString("searchQuestionErrorTitle"), 
						LanguageManager.getString("searchQuestionUnselectedItemError")
				);
				e.consume(); //make it so the dialog does not close
				return;
		   }
		});

		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContent(gridPane);

		if(dialog.showAndWait().get().equals(searchBtnType)) {
			PopupUI.removeOpenDialog(dialog);
			return new QuestionSearchFilters(titleField.getText(), tagTypeBox.getValue(), hasBeenAnsweredBox.getValue());
		}
		PopupUI.removeOpenDialog(dialog);
		return null;
	}
	
	/**Represents an "Has answer?" option in the Search Questions menu.*/
	public enum HasAnswerOption {
		EITHER("searchQuestionsHasAnswerEither"),
		YES("yes"),
		NO("no");
		
		private String propertyName;
		
		private HasAnswerOption(String propertyName) {
			this.propertyName = propertyName;
		}
		
		public String getPropertyName() {
			return propertyName;
		}
		
		@Override
		public String toString() {
			return LanguageManager.getString(propertyName);
		}
	}
	
}
