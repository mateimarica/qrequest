package com.qrequest.ui;

import com.qrequest.control.CreateAnswerControl;
import com.qrequest.control.LoginControl;
import com.qrequest.objects.Answer;
import com.qrequest.objects.Question;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

/**The UI for posting a answer to a question.*/
public class PostAnswerUI {
	
	/**The question that is being answered.*/
	private Question question;
	
	/**A reference to the forum UI.*/
	private ForumUI forumUI;
	
	/**The button that displays the pop-up for answering a question.*/
	private Button postAnswerBtn;
	
	/**Create a PostAnswerUI. Also creates a Post Answer button.
	 * @param question The question that is being answered.
	 * @param forumUI A reference to the forum UI.
	 */
	public PostAnswerUI(Question question, ForumUI forumUI) {
		this.question = question;
		this.forumUI = forumUI;
		
		postAnswerBtn = new Button("\u2795 Answer question");
		postAnswerBtn.setOnAction(e -> processPostAnswerButtonPress());
	}
	
	/**Returns the button that displays the pop-up for answering a question.
	 * @return The button that displays the pop-up for answering a question.
	 */
	public Button getPostAnswerButton() {
		return postAnswerBtn;
	}
	
	/**Called when the Ask Answer button is clicked.<br>
	 * Triggers a pop-up window where the user can create a answer.
	 * If the user doesn't cancel, the answer is created in the database and displayed on the UI.
	 */
	private void processPostAnswerButtonPress() {
		Answer newAnswer = displayPostAnswerDialog();
		if(newAnswer != null) {
			new CreateAnswerControl().processPostAnswer(newAnswer);
			forumUI.refresh();
		}
	}
	
	/**Displays a pop-up window that gives the option for the user to post an answer to a question.
	 * @param question The question being answered.
	 * @return The created <code>Answer</code> object, or <code>null</code> if the user canceled.
	 */
	private Answer displayPostAnswerDialog() {
			Dialog dialog = new Dialog();
			dialog.setTitle("Post an Answer");
			
			PopupUI.setupDialogStyling(dialog);

			GridPane gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			// top, right, bottom, left padding
			gridPane.setPadding(new Insets(20, 10, 10, 10));
			
			TextArea answerField = new TextArea();
			answerField.setPromptText("Your answer");
			answerField.setMaxSize(400, 400);
			answerField.setWrapText(true);
			gridPane.add(answerField, 0, 0);
			
			// Set the button types.
			ButtonType postAnswerBtnType = new ButtonType("Post Answer", ButtonData.RIGHT);
			dialog.getDialogPane().getButtonTypes().addAll(postAnswerBtnType, ButtonType.CANCEL);
			
			final Button postAnswerBtn = (Button)dialog.getDialogPane().lookupButton(postAnswerBtnType);
			postAnswerBtn.addEventFilter(ActionEvent.ACTION, event -> {
				int answerFieldLength = answerField.getText().length();
				
				if (answerFieldLength < 1 || answerFieldLength > PopupUI.MAX_DESC_LENGTH) {
				   PopupUI.displayWarningDialog("Did not post answer", "Answer must be 1 to " + PopupUI.MAX_DESC_LENGTH +  " characters in length.");
				   event.consume(); //make it so the dialog does not close
				   return;
				}
			});
			
			DialogPane dialogPane = dialog.getDialogPane();
			dialogPane.setContent(gridPane);
	
			if(dialog.showAndWait().get().equals(postAnswerBtnType)) {
				return new Answer(answerField.getText(), LoginControl.getUser(), question);
			}
			return null;
	}
}
