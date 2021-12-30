package com.qrequest.control;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;

import com.qrequest.control.Connector.Method;
import com.qrequest.ui.MainUI;
import com.qrequest.ui.PopupUI;
import com.qrequest.ui.MainUI.Environment;
import com.qrequest.ui.PopupUI.ProgressDialog;
import com.qrequest.util.OSUtil;
import com.qrequest.util.OSUtil.OS;

import org.eclipse.jetty.client.api.ContentResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Platform;

public class UpdateController {
	private UpdateController() {}

	private static final String RELEASES_ENDPOINT = "https://api.github.com/repos/mateimarica/qrequest/releases?per_page=1",
	                            DOWNLOAD_INFO_ENDPOINT = (MainUI.getEnv() == Environment.PROD ? 
	                                                     "https://qr.mateimarica.dev/api/downloads" :
								                         "https://qr.mateimarica.local:5000/api/downloads");

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
								determineDownload(latestReleaseJson);
							}).start();
						}
					});
				}
			default: // Fail silently
				return;
		}
	}

	private static void determineDownload(JSONObject latestReleaseJson) {
		JSONArray assets = (JSONArray) latestReleaseJson.get("assets");

		ContentResponse res = Connector.send(Method.GET, DOWNLOAD_INFO_ENDPOINT, null, "application/json", null, true);
		JSONObject extsJson = new JSONObject(res.getContentAsString()).getJSONObject("exts");

		String soughtExtension;
		switch (OSUtil.getOS()) {
			case WINDOWS:
				soughtExtension = extsJson.getString("windows");
				break;
			case LINUX:
				soughtExtension = extsJson.getString("linux");
				break;
			case MACOS:
				soughtExtension = extsJson.getString("macos");
				break;
			default:
				soughtExtension = "jar";
		}

		for (int i = 0; i < assets.length(); i++) {
			JSONObject asset = assets.getJSONObject(0);
			if(asset.getString("name").endsWith(soughtExtension)) {
				int downloadSize = asset.getInt("size"); // bytes
				String downloadFilename = asset.getString("name");
				String downloadURL = asset.getString("browser_download_url");
				downloadUpdate(downloadSize, downloadFilename, downloadURL);
				return;
			}
		}

		Platform.runLater(() -> {
			PopupUI.displayErrorDialog(
				"Download failed", 
				"Couldn't find the correct distribution for your operating system."
			);
		});
	}

	private static void downloadUpdate(int downloadSize, String downloadFilename, String downloadURL) {
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