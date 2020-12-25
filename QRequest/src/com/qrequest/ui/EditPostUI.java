package com.qrequest.ui;

import java.util.ResourceBundle;

import com.qrequest.control.EditPostControl;
import com.qrequest.helpers.LanguageManager;
import com.qrequest.objects.Post;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

/**The edit post UI.*/
public class EditPostUI {
	
	/**The button for editing a post.*/
	private Button editButton;
	
	/**The post being edited.*/
	private Post post;
	
	/**A reference to the forum UI.*/
	private ForumUI forumUI;
	
	/**Creates a new EditPostUI.
	 * @param post The post being edited.
	 * @param forumUI A reference to the forum UI.
	 */
	public EditPostUI(Post post, ForumUI forumUI) {
		this.post = post;
		this.forumUI = forumUI;
		
		editButton = new Button();
		editButton.setPrefSize(30, 30);
		editButton.setId("editButton");
		if(post.isQuestion()) {
			editButton.setTooltip(new Tooltip(LanguageManager.getString("editQuestionButton")));
		} else {
			editButton.setTooltip(new Tooltip(LanguageManager.getString("editAnswerButton")));

		}
		
		editButton.setOnAction(e -> editButtonPress());
	}
	
	/**Triggered when the edit button is pressed.*/
	private void editButtonPress() {
		if(displayEditPostDialog()) {
			new EditPostControl().processEditPost(post);
			forumUI.refresh();
		}
	}
	
	/**Returns the edit button.*/
	public Button getEditButton() {
		return editButton;
	}
	
	/**Displays a pop-up window that gives the user the option to edit their own post <i>description</i>.
	 * @return <code>true</code> if the <code>Post</code> object's description was edited, <code>false</code> if the user canceled.
	 */
	private boolean displayEditPostDialog() {

		boolean isQuestion = post.isQuestion();
		ResourceBundle langBundle = LanguageManager.getLangBundle();
		
		
		// Create the custom dialog.
		Dialog dialog = new Dialog();
		
		if(isQuestion) {
			dialog.setTitle(langBundle.getString("editQuestionButton"));
		} else {
			dialog.setTitle(langBundle.getString("editAnswerButton"));
		}
		
		PopupUI.setupDialogStyling(dialog);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		// top, right, bottom, left padding
		gridPane.setPadding(new Insets(20, 10, 10, 10));

		TextArea descField = new TextArea();
		descField.setText(post.getDescription());
		descField.setMaxSize(400, 400);
		descField.setWrapText(true);
		gridPane.add(descField, 0, 0);
		
		ComboBox<Tag> tagTypeBox = null;
		if(isQuestion) {
			tagTypeBox = new ComboBox<>();	
			tagTypeBox.getSelectionModel().select(((Question)post).getTag());
			Tag[] tagTypes = Tag.values();
			for(int i = 0; i < tagTypes.length; i++) {
				tagTypeBox.getItems().add(tagTypes[i]);
			}
			gridPane.add(tagTypeBox, 0, 1);
		}


		// Set the button types.
		ButtonType confirmBtnType = new ButtonType(LanguageManager.getString("confirm"), ButtonData.RIGHT);
		dialog.getDialogPane().getButtonTypes().addAll(confirmBtnType, ButtonType.CANCEL);

		final Button confirmBtn = (Button)dialog.getDialogPane().lookupButton(confirmBtnType);
		confirmBtn.addEventFilter(ActionEvent.ACTION, event -> {

			if (descField.getText().length() > PopupUI.MAX_DESC_LENGTH) {
			   
				PopupUI.displayWarningDialog(langBundle.getString("editQuestionErrorTitle"), langBundle.getString("descTooLongError"));
					
				event.consume(); //make it so the dialog does not close
				return;
		   }
		});

		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContent(gridPane);

		if(dialog.showAndWait().get().equals(confirmBtnType)) {
			post.setDescription(descField.getText());
			
			if(isQuestion) {
				((Question)post).setTag(tagTypeBox.getValue());
			}
			
			return true;
		}
		return false;
	}
	
}
