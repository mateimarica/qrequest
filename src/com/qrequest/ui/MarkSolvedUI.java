package com.qrequest.ui;

import com.qrequest.control.QuestionController;
import com.qrequest.control.UserController;
import com.qrequest.helpers.LanguageManager;
import com.qrequest.objects.Answer;
import com.qrequest.objects.Post;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;

/**The mark-solved UI.*/
public class MarkSolvedUI {
	
	/**The answer that this MarkSolvedUI is assocated with.*/
	private Answer answer;
	
	/**A reference to the forum UI.*/
	private ForumUI forumUI;
	
	/**The button for marking an answer as solved.*/
	private ToggleButton markSolvedButton;
	
	/**The checkmark that appears on the front directly on the question.*/
	private Label markedSolvedOnQuestionLabel;
	
	/**The checkmark that appears on the answer, inside of the question.*/
	private Label markedSolvedOnAnswerLabel;
	
	/**Creates a MarkSolvedUI for use on question panes. Only creates a markedSolvedOnQuestionLabel.*/
	public MarkSolvedUI() {
		markedSolvedOnQuestionLabel = new Label("\u2714");
		markedSolvedOnQuestionLabel.setId("markedSolvedLabel");
		markedSolvedOnQuestionLabel.setTooltip(new Tooltip(LanguageManager.getString("questionPaneMarkedSolvedTooltip")));
		markedSolvedOnQuestionLabel.setPadding(new Insets(0, 0, 0, 10));
		markedSolvedOnQuestionLabel.setMaxHeight(1);
	}
	
	/**Returns the checkmark that appears on the front directly on the question.
	 * @return The checkmark that appears on the front directly on the question.
	 */
	public Label getMarkedSolvedOnQuestionLabel() {
		return markedSolvedOnQuestionLabel;
	}	
	
	/**Creates a MarkSolvedUI for use only inside of blown-up question, on an answer.
	 * Creates a markedSolvedOnAnswerLabel and a markSolvedButton;
	 * @param post The answer that this UI is associated with. 
	 * If the <code>Post</code> is actually a Question, all characteristics will be <code>null</code>.
	 * @param forumUI A reference to the forum UI.
	 */
	public MarkSolvedUI(Post post, ForumUI forumUI) {
		
		if(post.isAnswer()) { //If the post is an answer
			
			this.answer = (Answer)post;
			this.forumUI = forumUI;
			
			Answer solvedAnswer = (answer).getQuestion().getSolvedAnswer();		
			if(solvedAnswer != null && solvedAnswer.getID().equals(answer.getID())) {
				markedSolvedOnAnswerLabel = new Label();
				markedSolvedOnAnswerLabel.setText("\u2714");
				markedSolvedOnAnswerLabel.setId("markedSolvedLabel");
				markedSolvedOnAnswerLabel.setTooltip(new Tooltip(LanguageManager.getString("answerPaneMarkedSolvedTooltip")));
				markedSolvedOnAnswerLabel.setWrapText(true);
			}
		
			if ((answer.getQuestion().getAuthor().getUsername().equals(UserController.getUser().getUsername()) //AND the answer's question is yours
				&& !answer.getAuthor().getUsername().equals(UserController.getUser().getUsername()))) { //and the answer isn't yours
							
				markSolvedButton = new ToggleButton();
				solvedAnswer = answer.getQuestion().getSolvedAnswer();
				if(solvedAnswer != null && solvedAnswer.getID().equals(answer.getID())) {
					markSolvedButton.setSelected(true);
				}
				markSolvedButton.setPrefSize(30, 30);
				markSolvedButton.setId("markSolvedButton");
				markSolvedButton.setTooltip(new Tooltip(LanguageManager.getString("markAsSolvedTooltip")));
				markSolvedButton.setOnAction(e -> markSolvedButtonPress());
			}	
		}
	}
		
	/**Triggered when the mark-solved button is clicked.*/
	private void markSolvedButtonPress() {
		if(markSolvedButton.isSelected()) {
			if (QuestionController.markSolved(answer.getQuestion(), answer.getID()))
				forumUI.refresh();
		} else {
			if (QuestionController.markSolved(answer.getQuestion(), null))
				markedSolvedOnAnswerLabel.setVisible(false);
		}
	}
	
	/**Returns the button for marking the answer as solved.
	 * @return The button for marking the answer as solved.
	 */
	public ToggleButton getMarkSolvedButton() {
		return markSolvedButton;
	}
	
	/**Returns The checkmark that appears on the answer, inside of the question. Will be invisible if the answer isn't mark as the solver.
	 * @return The checkmark that appears on the answer, inside of the question.
	 */
	public Label getMarkedSolvedOnAnswerLabel() {
		return markedSolvedOnAnswerLabel;
	}
}
