package com.qrequest.ui;

import java.util.ResourceBundle;

import com.qrequest.control.AnswerController;
import com.qrequest.control.QuestionController;
import com.qrequest.managers.LanguageManager;
import com.qrequest.objects.Answer;
import com.qrequest.objects.Post;
import com.qrequest.objects.Question;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

/**The UI for deleting a post.*/
public class DeletePostUI {
	
	/**The button for deleting a post.*/
	private Button deleteButton;
	
	/**The post being deleted.*/
	private Post post;
	
	/**A reference to the forum UI.*/
	private ForumUI forumUI;
	
	/**Create a new DeletePostUI.
	 * @param post The post being deleted.
	 * @param forumUI A reference to the forum UI.
	 */
	public DeletePostUI(Post post, ForumUI forumUI) {
		this.post = post;
		this.forumUI = forumUI;
		
		deleteButton = new Button();
		deleteButton.setPrefSize(30, 30);
		deleteButton.setId("deleteButton");
		if(post.isQuestion()) {
			deleteButton.setTooltip(new Tooltip(LanguageManager.getString("deleteQuestionButtonTooltip")));
		} else {
			deleteButton.setTooltip(new Tooltip(LanguageManager.getString("deleteAnswerButtonTooltip")));
		}
		deleteButton.setOnAction(e -> deleteButtonPress());
	}
	
	/**Returns the delete-post button.
	 * @return The delete-post button.
	 */
	public Button getDeleteButton() {
		return deleteButton;
	}
	
	/**Triggered when the delete button is pressed. Displays a pop-up asking the user to confirm the deletion.
	 * The post is only deleted if the user clicks "OK".
	 */
	private void deleteButtonPress() {
		if(PopupUI.displayConfirmationDialog(
			LanguageManager.getString("confirmDeleteTitle"), 
				String.format(
					(post.isQuestion() ? LanguageManager.getString("confirmDeleteQuestion") : LanguageManager.getString("confirmDeleteAnswer")), 
					post.getPostType()
				)
		)) {
			if(post.isQuestion()) {
				QuestionController.delete((Question) post);
				forumUI.backButtonPress();
			} else {
				AnswerController.delete((Answer) post);
				forumUI.refresh();
			}
		}
	}
}
