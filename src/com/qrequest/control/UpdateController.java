package com.qrequest.control;

import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;

import com.qrequest.control.Connector.Method;
import com.qrequest.objects.ProgressInputStream;
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

				if (!currentVersion.equals(latestVersion)) {
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
		Path currentJarPath = Paths.get(
			new java.io.File(UpdateController.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toString()
		);

		Path destinationPath = Paths.get(new java.io.File(currentJarPath.toString()).getParent(), downloadFilename);
		
		ProgressDialog progressDialog = new ProgressDialog("Downloading...", downloadSize).show();
		Consumer<Integer> progessCallback = (progress) -> {
			progressDialog.updateProgess(progress);
		};

		try (ProgressInputStream in = new ProgressInputStream(new URL(downloadURL).openStream(), 1024*16, progessCallback)) {
			FileChannel channel = FileChannel.open(destinationPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
			progressDialog.setChannel(channel);
			channel.transferFrom(Channels.newChannel(in), 0, downloadSize);
			if(progressDialog.wasCanceled()) {
				boolean deleteSuccessful = destinationPath.toFile().delete();
				Platform.runLater(() -> {
					String deleteMessage = "";
					if (!deleteSuccessful) 
						deleteMessage = "\nHowever, the unfinished download file couldn't be deleted:\n" + destinationPath.toAbsolutePath();

					PopupUI.displayWarningDialog(
						"Download canceled", 
						"The download was canceled." + deleteMessage
					);
				});
				return;
			}
			progressDialog.close();
			restartClient(currentJarPath.toString(), destinationPath.toString());
		} catch (IOException e) {
			progressDialog.close();
			Platform.runLater(() -> {
				PopupUI.displayErrorDialog(
					"Download failed", 
					"The download could not be completed. Error: " + e +"\nIs QRequest in a restricted directory?"
				);
			});
			e.printStackTrace();
		}
	}

	private static void restartClient(String currentJarPath, String destinationPath) {
		try {
			if (OSUtil.getOS() == OS.WINDOWS) {
				Runtime.getRuntime().exec(
					"cmd /c ping localhost -n 3 > nul && del /f \"" + currentJarPath + "\" && start \"\" \"" + destinationPath + "\""
				);
			} else {
				Runtime.getRuntime().exec(
					new String[] {"bash", "-c", "sleep 3; rm -rf \"" + currentJarPath +"\"; java -jar \"" + destinationPath + "\""}
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