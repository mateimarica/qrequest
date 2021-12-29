package com.qrequest.control;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;

import com.qrequest.control.Connector.Method;
import com.qrequest.ui.PopupUI;
import com.qrequest.ui.PopupUI.ProgressDialog;

import org.eclipse.jetty.client.api.ContentResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Platform;

public class UpdateController {
	private UpdateController() {}

	private static final String RELEASES_ENDPOINT = "https://api.github.com/repos/mateimarica/qrequest/releases?per_page=1";

	public static void checkForUpdates() {
		String currentVersion = UpdateController.class.getPackage().getImplementationVersion();
		if (currentVersion == null) return;

		ContentResponse res = Connector.send(Method.GET, RELEASES_ENDPOINT, null, "application/vnd.github.v3+json", null, false);
		if (res == null) return;

		switch (res.getStatus()) {
			case 200:
				JSONObject latestReleaseJson = (JSONObject) new JSONArray(res.getContentAsString()).get(0);
				String latestVersion = (String) latestReleaseJson.get("tag_name");

				if (!currentVersion.equals(latestVersion)) {
					Platform.runLater(() -> {
						if(PopupUI.displayConfirmationDialog(
							"Update available", 
							"There is a new version of QRequest available: " + latestVersion + "\nWould you like to download it?"
						)) {
							new Thread(() -> {
								downloadUpdate(latestReleaseJson);
							}).start();
						}
					});
				}
			default: // Fail silently
				return;
		}
	}

	private static void downloadUpdate(JSONObject latestReleaseJson) {
		JSONObject assetJson = ((JSONObject) ((JSONArray) latestReleaseJson.get("assets")).get(0)); // Change the 0 if there will be multiple
		
		int downloadSize = assetJson.getInt("size"); // bytes
		String downloadFilename = assetJson.getString("name");
		String downloadURL = assetJson.getString("browser_download_url");

		String currentJarPath = Paths.get(
			new java.io.File(UpdateController.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toString()
		).toString();

		Path destinationPath = Paths.get(new java.io.File(currentJarPath).getParent(), downloadFilename);
		
		ProgressDialog progressDialog = new ProgressDialog().show();
		Consumer<Double> progessCallback = (percent) -> {
			progressDialog.updateProgess(percent);
		};

		try (ProcessInputStream in = new ProcessInputStream(new URL(downloadURL).openStream(), 1024*16, downloadSize, progessCallback)) {
			Files.copy(in, destinationPath, StandardCopyOption.REPLACE_EXISTING);
			progressDialog.close();
			restartClient(new java.io.File(currentJarPath).toString(), destinationPath.toString());
		} catch (IOException e) {
			Platform.runLater(() -> {
				PopupUI.displayErrorDialog(
					"Download failed", 
					"The download could not be completed. Error: " + e.getMessage()
				);
			});
		}
	}

	private static void restartClient(String currentJarPath, String destinationPath) {
		try {
			String cmd = "cmd /c ping localhost -n 2 > nul && del /f \"" + currentJarPath + "\" && start \"\" \"" + destinationPath + "\"";
			Runtime.getRuntime().exec(cmd);
			Platform.exit();
			System.exit(0);
		} catch (IOException e) {
			Platform.runLater(() -> {
				PopupUI.displayErrorDialog(
					"Cleanup failed", 
					"This version of QRequest could not be cleaned up. Error: " + e.getMessage() 
					+ "\nHowever, the new version of QRequest should now exist in the directory from which the executable was started."
				);
			});
		}
	}
}