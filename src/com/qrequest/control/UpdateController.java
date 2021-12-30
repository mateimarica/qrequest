package com.qrequest.control;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;

import com.qrequest.control.Connector.Method;
import com.qrequest.ui.PopupUI;
import com.qrequest.ui.PopupUI.ProgressDialog;
import com.qrequest.util.OSUtil;
import com.qrequest.util.OSUtil.OS;

import org.eclipse.jetty.client.api.ContentResponse;
import org.json.JSONObject;

import javafx.application.Platform;

public class UpdateController {
	private UpdateController() {}

	public static void checkForUpdates() {
		String currentVersion = UpdateController.class.getPackage().getImplementationVersion();
		if (currentVersion == null) return;

		ContentResponse res = Connector.sendQRequest(Method.GET, "/downloads");
		if (res == null) return;

		switch (res.getStatus()) {
			case 200:
				JSONObject downloadInfoJson = new JSONObject(res.getContentAsString());
				String latestVersion = downloadInfoJson.getString("version");

				if (currentVersion.equals(latestVersion)) {
					Platform.runLater(() -> {
						if(PopupUI.displayConfirmationDialog(
							"Update available", 
							"There is a new version of QRequest available: " + latestVersion + "\nWould you like to download it?"
						)) {
							new Thread(() -> {
								determineDownload(downloadInfoJson);
							}).start();
						}
					});
				}
			default: // Fail silently
				return;
		}
	}

	private static void determineDownload(JSONObject downloadInfoJson) {
		JSONObject osDownloadJson = downloadInfoJson.getJSONObject("downloads").getJSONObject(OSUtil.getOS().toString());
		String downloadFilename = osDownloadJson.optString("name", null);
		int downloadSize = osDownloadJson.getInt("size");
		String downloadURL = osDownloadJson.optString("browser_download_url", null);

		if (downloadFilename == null || downloadSize == 0 || downloadURL == null) {
			Platform.runLater(() -> {
				PopupUI.displayErrorDialog(
					"Download failed", 
					"Couldn't find the correct distribution for your operating system."
				);
			});
		} else {
			downloadUpdate(downloadFilename, downloadSize, downloadURL);
		}
	}

	private static void downloadUpdate(String downloadFilename, int downloadSize, String downloadURL) {
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
			if (OSUtil.getOS() == OS.WINDOWS) {
				Runtime.getRuntime().exec(
					"cmd /c ping localhost -n 2 > nul && del /f \"" + currentJarPath + "\" && start \"\" \"" + destinationPath + "\""
				);
			} else {
				Runtime.getRuntime().exec(
					new String[] {"bash", "-c", "sleep 2; rm -rf \"" + currentJarPath +"\"; java -jar \"" + destinationPath + "\""}
				);
			}
			Platform.exit();
			System.exit(0);
		} catch (IOException e) {
			Platform.runLater(() -> {
				PopupUI.displayErrorDialog(
					"Cleanup failed", 
					"This old version of QRequest could not be cleaned up. However, the new version should now exist the same directory." +
					"\nError: " + e.getMessage()
				);
			});
		}
	}
}