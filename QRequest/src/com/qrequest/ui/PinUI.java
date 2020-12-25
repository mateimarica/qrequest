package com.qrequest.ui;

import com.qrequest.control.PinControl;
import com.qrequest.helpers.LanguageManager;
import com.qrequest.objects.Question;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;

/**The UI for pinning a question.*/
public class PinUI {
	
	/**The question being pinned.*/
	private Question question;
	
	/**A reference to the forum UI.*/
	private ForumUI forumUI;
	
	/**The button for pinning a question.*/
	private ToggleButton pinButton;

	/**Create a PinUI. Creates a button for pinning.
	 * @param question The question being pinned.
	 * @param forumUI A reference to the forum UI.
	 */
	public PinUI(Question question, ForumUI forumUI) {
		this.question = question;
		this.forumUI = forumUI;
		
		pinButton = new ToggleButton();
		pinButton.setPrefSize(30, 30);
		pinButton.setSelected(question.isPinned());
		pinButton.setId("pinButton");
		pinButton.setTooltip(new Tooltip(LanguageManager.getLangBundle().getString("pinQuestionButton")));
		pinButton.setOnAction(e -> pinButtonPress());
	}
	
	/**Returns the button for pinning a question.
	 * @return The button for pinning a question.
	 */
	public ToggleButton getPinButton() {
		return pinButton;
	}
	
	/**Triggered when the pin button is clicked. Pins the question and refreshes the forum.*/
	private void pinButtonPress() {
		new PinControl().pinQuestion(question);
		forumUI.refresh();
	}
}
