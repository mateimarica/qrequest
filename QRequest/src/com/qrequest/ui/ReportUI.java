package com.qrequest.ui;

import com.qrequest.control.LoginControl;
import com.qrequest.control.ReportControl;
import com.qrequest.objects.Post;
import com.qrequest.objects.Report;

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

/**The UI for reporting a post.*/
public class ReportUI {
	
	/**The post being reported.*/
	private Post post;
	
	/**The button that displays the reporting window.*/
	private Button reportButton;
	
	/**Create a ReportUI. Creates a reportButton.
	 * @param post The post being reported.
	 */
	public ReportUI(Post post){
		this.post = post;
		
		reportButton = new Button();
		reportButton.setPrefSize(30, 30);
		reportButton.setId("reportButton");
		reportButton.setTooltip(new Tooltip("Report " + post.getPostType()));
		reportButton.setOnAction(e -> reportButtonPress());
	}
	
	/**Triggered when the report button is pressed.*/
	private void reportButtonPress() {
		Report report = displayReportPostDialog();
		if(report != null) {
			new ReportControl().reportPost(report);
			PopupUI.displayInfoDialog("Report Sent", "Thank you for helping keep our platform safe!");
		}
	}
	
	/**Returns the button that displays the reporting window.
	 * @return The button that displays the reporting window.
	 */
	public Button getReportButton() {
		return reportButton;
	}
	
	/**Displays a pop-up window that gives the user the option to edit their own post <i>description</i>.
	 * @param post The post being edited.
	 * @return <code>true</code> if the <code>Post</code> object's description was edited, <code>false</code> if the user canceled.
	 */
	private Report displayReportPostDialog() {

		Report report = new Report(LoginControl.getUser(), post);
		
		String postType = post.getPostType();
		
		// Create the custom dialog.
		Dialog dialog = new Dialog();
		dialog.setTitle("Report " + postType);

		PopupUI.setupDialogStyling(dialog);
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		// top, right, bottom, left padding
		gridPane.setPadding(new Insets(20, 10, 10, 10));
		
		ComboBox<String> reportTypeBox = new ComboBox<>();	
		reportTypeBox.setPromptText("Reason for report");
		GridPane.setConstraints(reportTypeBox, 0, 0);
		String[] reportTypes = report.getReportTypes();
		for(int i = 0; i < reportTypes.length; i++) {
			reportTypeBox.getItems().add(reportTypes[i]);
		}
		
		TextArea reportField = new TextArea();
		reportField.setPromptText("Detailed report (Optional)");
		reportField.setMaxSize(300, 200);
		reportField.setWrapText(true);
		GridPane.setConstraints(reportField, 0, 1);
		
		gridPane.getChildren().addAll(reportTypeBox, reportField);

		// Set the button types.
		ButtonType confirmBtnType = new ButtonType("Send Report", ButtonData.RIGHT);
		dialog.getDialogPane().getButtonTypes().addAll(confirmBtnType, ButtonType.CANCEL);

		final Button confirmBtn = (Button)dialog.getDialogPane().lookupButton(confirmBtnType);
		confirmBtn.addEventFilter(ActionEvent.ACTION, event -> {

			if (reportField.getText().length() > 1000) {
			   PopupUI.displayWarningDialog("Error Reporting " + postType, "Report is too long.");
			   event.consume(); //make it so the dialog does not close
			   return;
			}
			
			if(reportTypeBox.getSelectionModel().isEmpty()) {
				PopupUI.displayWarningDialog("Error Reporting " + postType, "Must select reason for report.");
				event.consume(); //make it so the dialog does not close
				return;
			}
		});

		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.setContent(gridPane);
		
		if(dialog.showAndWait().get().equals(confirmBtnType)) {
			report.setReportType(reportTypeBox.getValue());
			report.setDesc(reportField.getText());
			return report;
		}
		return null;
	}
}