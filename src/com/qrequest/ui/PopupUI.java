package com.qrequest.ui;

import java.io.IOException;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.qrequest.managers.LanguageManager;
import com.qrequest.managers.ThemeManager;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**Class for reducing the amount of boilerplate code when displaying pop-ups.
 * Everything is static because this class has no state. No instance variables. 
 * This is an appropriate use of the static keyword.*/
public class PopupUI {
	
	private static ArrayList<Dialog> openDialogs = new ArrayList<>();

	//Not Javadocing these variables.. Pretty self-explanatory.
	
	static final int MIN_ANSWER_DESC_LENGTH = 1;
	
	static final int MAX_DESC_LENGTH = 10000;
	
	static final int MIN_NAME_LENGTH = 3;
	static final int MAX_NAME_LENGTH = 10;
	
	static final int MIN_PASSWORD_LENGTH = 3;
	static final int MAX_PASSWORD_LENGTH = 40;
	
	static final int MIN_QUESTION_LENGTH = 6;
	static final int MAX_QUESTION_LENGTH = 200;
	
	/**Displays a warning dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window. Should describe the warning.
	 * @return <code>true</code> if the <b>OK</b> button is clicked, <code>false</code> if any other button is clicked.
	 */
	public static boolean displayWarningDialog(String title, String text) {
		return displayDialog(AlertType.WARNING, title, text);
	}
	
	/**Displays a information dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window. Should give the user some information.
	 * @return <code>true</code> if the <b>OK</b> button is clicked, <code>false</code> if any other button is clicked.
	 */
	public static boolean displayInfoDialog(String title, String text) {
		return displayDialog(AlertType.INFORMATION, title, text);
	}

	/**Displays an error dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window. Should describe the error.
	 * @return <code>true</code> if the <b>OK</b> button is clicked, <code>false</code> if any other button is clicked.
	 */
	public static boolean displayErrorDialog(String title, String text) {
		return displayDialog(AlertType.ERROR, title, text);
	}

	/**Displays an confirmation dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window. Should ask something like "Are you sure...".
	 * @return <code>true</code> if the <b>OK</b> button is clicked, <code>false</code> if any other button is clicked.
	 */
	public static boolean displayConfirmationDialog(String title, String text) {
		return displayDialog(AlertType.CONFIRMATION, title, text);
	}
	
	/**General method for displaying a dialog.
	 * @param title The title of the pop-up window.
	 * @param text The text within the window.
	 * @return <code>true</code> if the <b>OK</b> button is clicked, <code>false</code> if any other button is clicked.
	 */
	private static boolean displayDialog(AlertType alert, String title, String text) {
		Alert dialog = new Alert(alert);
		dialog.setHeaderText(null);
		dialog.setTitle(title);
		dialog.setContentText(text);

		//set dark theme for pop-up
		Scene scene = dialog.getDialogPane().getScene();
		scene.getStylesheets().add(ThemeManager.getCurrentThemeURL());			
		
		//Set the icon for the popup
		Stage stage = (Stage) scene.getWindow();
		stage.getIcons().add(
		    new Image(new PopupUI().getClass().getResource(MainUI.ICON_URL).toString()));
		
		try {
			return (dialog.showAndWait().get().equals(ButtonType.OK));
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	/**Shortcut method for displaying the database connection dialog. Used in every Control class.
	 * @return <code>true</code> if the <b>OK</b> button is clicked, <code>false</code> if any other button is clicked.
	 */
	@Deprecated
	public static boolean displayDatabaseConnectionErrorDialog() {
		return displayErrorDialog(LanguageManager.getString("connectionErrorTitle"), LanguageManager.getString("connectionError"));
	}
	
	/**Sets the icon and the theme for the dialog window.
	 * @param dialog The dialog window.
	 */
	static void setupDialogStyling(Dialog dialog) {
		openDialogs.add(dialog);
		//Set the icon for the popup
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(
		    new Image(new PopupUI().getClass().getResource(MainUI.ICON_URL).toString()));
		
		//sets the theme
		dialog.getDialogPane().getStylesheets().add(ThemeManager.getCurrentThemeURL());
	}

	static void closeOpenDialogs() {
		for (Dialog dialog : openDialogs) {
			dialog.close();
		}
		openDialogs.clear();
	}
	
	static void removeOpenDialog(Dialog dialog) {
		openDialogs.remove(dialog);
	}

	public static class ProgressDialog {
		private Dialog dialog;
		private ProgressBar progressBar;
		private Label progressLabel;
		private int target;
		private double targetInMegabytes;
		private Channel channel;
		private boolean wasCanceled = false;
	
		public ProgressDialog(String title, int target) {
			this.target = target;
			this.targetInMegabytes = convertToMB(target);
			Platform.runLater(() -> {
				progressBar = new ProgressBar(0);
				progressBar.setPrefWidth(300);

				progressLabel = new Label("0 / " + targetInMegabytes + " MB");

				VBox vbox = new VBox();
				vbox.setPrefWidth(300);
				vbox.getChildren().addAll(progressBar, progressLabel);
				vbox.setAlignment(Pos.CENTER);

				dialog = new Dialog();
				PopupUI.setupDialogStyling(dialog);

				dialog.setTitle(title);
				dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
				dialog.setOnCloseRequest(e -> {
					wasCanceled = true;
					if (channel != null) {
						try {
							channel.close();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
					PopupUI.removeOpenDialog(dialog);
				});

				DialogPane dialogPane = dialog.getDialogPane();
				dialogPane.setContent(vbox);
			});
		}

		public void close() {
			Platform.runLater(() -> {
				dialog.close();
			});
			PopupUI.removeOpenDialog(dialog);
		}

		public ProgressDialog show() {
			Platform.runLater(() -> {
				dialog.show();
			});
			return this;
		}

		public void updateProgess(int progress) {
			Platform.runLater(() -> {
				progressBar.setProgress((double) progress / target);
				progressLabel.setText(convertToMB(progress) + " / " + targetInMegabytes + " MB");
			});
		}

		public void setChannel(Channel channel) {
			this.channel = channel;
		}

		public boolean wasCanceled() {
			return wasCanceled;
		}

		private static double convertToMB(int bytes) {
			double megabytes = bytes / 1000000.0;
			return (double) Math.round(megabytes * 100) / 100;
		}
	}
}
