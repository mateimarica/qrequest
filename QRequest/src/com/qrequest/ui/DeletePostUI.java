package com.qrequest.ui;

import com.qrequest.control.DeletePostControl;
import com.qrequest.objects.Post;

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
		deleteButton.setTooltip(new Tooltip("Delete " + post.getPostType()));	
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
		if(PopupUI.displayConfirmationDialog("Confirm Deletion", "Are you sure you want to delete this " + post.getPostType() + "? This cannot be undone!")) {
			new DeletePostControl().processDeletePost(post);
			if(post.isQuestion()) {
				forumUI.backButtonPress();
			} else {
				forumUI.refresh();
			}
		}
	}
}
