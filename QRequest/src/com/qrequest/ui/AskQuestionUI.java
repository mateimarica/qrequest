package com.qrequest.ui;

import java.util.ResourceBundle;

import com.qrequest.control.AskQuestionControl;
import com.qrequest.control.LoginControl;
import com.qrequest.helpers.LanguageManager;
import com.qrequest.objects.Question;
import com.qrequest.objects.Tag;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

/**The UI for asking a question.*/
public class AskQuestionUI {
	
	/**A reference to the main forum UI.*/
	private ForumUI forumUI; 
	
	/**Button to ask a question*/
	private Button askQuestionBtn;
	
	/**Create a new QuestionUI.
	 * @param forumUI A reference to the forum UI.
	 */
	public AskQuestionUI(ForumUI forumUI) {
		this.forumUI = forumUI;
		
		askQuestionBtn = new Button("\u2795 " + LanguageManager.getString("askQuestionButton"));
		askQuestionBtn.setOnAction(e -> askQuestionButtonPress());
	}
	
	/**Returns the ask-question button.
	 * @return The ask-question button.
	 */
	public Button getAskQuestionButton() {
		return askQuestionBtn;
	}
	
	/**Called when the Ask Question button is clicked.<br>
	 * Triggers a pop-up window where the user can create a question.
	 * If the user doesn't cancel, the question is created in the database and displayed on the UI.
	 */
	private void askQuestionButtonPress() {
		Question newQuestion = displayAskQuestionDialog();
		if(newQuestion != null) {
			new AskQuestionControl().processPostQuestion(newQuestion);
			forumUI.refresh();
		}
	}
	
	/**The dialog for asking a question. Allows user to enter the title and an optional description.<br>
	 * Posts questions upon pressing "OK" and refreshes the question table.
	 * @return The created <code>Question</code> object, or <code>null</code> if the user canceled.
	 */
	public Question displayAskQuestionDialog() {
		
		ResourceBundle langBundle = LanguageManager.getLangBundle();
		
		// Create the custom dialog.
		Dialog dialog = new Dialog();
		dialog.setTitle(langBundle.getString("askQuestionPopupTitle"));
		
		PopupUI.setupDialogStyling(dialog);		

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		// top, right, bottom, left padding
		gridPane.setPadding(new Insets(20, 10, 10, 10));
		
		TextField titleField = new TextField();
		titleField.setPromptText(langBundle.getString("questionFieldPrompt"));
		titleField.setMinWidth(300);
		titleField.setMaxWidth(300);
		gridPane.add(titleField, 0, 0);

		TextArea descField = new TextArea();
		descField.setPromptText(langBundle.getString("descFieldPrompt"));
		descField.setMaxSize(300, 200);
		descField.setWrapText(true);
		gridPane.add(descField, 0, 1);
		
		ComboBox<Tag> tagTypeBox = new ComboBox<>();	
		tagTypeBox.setPromptText(langBundle.getString("tagMenuPrompt"));
		Tag[] tagTypes = Tag.values();
		for(int i = 0; i < tagTypes.length; i++) {
			tagTypeBox.getItems().add(tagTypes[i]);
		}
		gridPane.add(tagTypeBox, 0, 2);
		
		// Set the button types.
		ButtonType postQuestionBtnType = new ButtonType(langBundle.getString("confirmQuestionPostButton"), ButtonData.RIGHT);
		dialog.getDialogPane().getButtonTypes().addAll(postQuestionBtnType, ButtonType.CANCEL);
		
		final Button postQuestionBtn = (Button)dialog.getDialogPane().lookupButton(postQuestionBtnType);
		postQuestionBtn.addEventFilter(ActionEvent.ACTION, event -> {
			
			int titleFieldLength = titleField.getText().length();
			
			if (titleFieldLength < 8 || titleFieldLength > 250) {
					
				PopupUI.displayWarningDialog(
						langBundle.getString("errorPostingQuestionTitle"), 
						String.format(
							langBundle.getString("badTitleLength"), PopupUI.MIN_QUESTION_LENGTH, PopupUI.MAX_QUESTION_LENGTH
						)
				);
				
				event.consume(); //make it so the dialog does not close
				return;
			}
			
			if (descField.getText().length() > PopupUI.MAX_DESC_LENGTH) {

				PopupUI.displayWarningDialog(
						langBundle.getString("errorPostingQuestionTitle"), 
						String.format(
							langBundle.getString("badDescLength"), PopupUI.MAX_DESC_LENGTH
						)
				);
				
				event.consume(); //make it so the dialog does not close
				return;
		   }
			
			if(tagTypeBox.getSelectionModel().isEmpty()) {
				
				PopupUI.displayWarningDialog(
						langBundle.getString("errorPostingQuestionTitle"), 
						langBundle.getString("mustSelectTag")
				);
				
				event.consume(); //make it so the dialog does not close
				return;
			}
		});
		
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContent(gridPane);
		
		if(dialog.showAndWait().get().equals(postQuestionBtnType)) {
			return new Question(titleField.getText(), descField.getText(), LoginControl.getUser(), tagTypeBox.getValue());
		}
		return null;
	}
}
