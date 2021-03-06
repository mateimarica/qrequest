package com.qrequest.ui;

import java.util.ResourceBundle;

import com.qrequest.control.LoginControl;
import com.qrequest.helpers.LanguageManager;
import com.qrequest.helpers.ThemeManager;
import com.qrequest.objects.Language;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;

/**The UI for the menu bar (bar at the top with Settings, etc).*/
public class MenuBarUI {
	
	/**The menu bar.*/
	private MenuBar menubar;
	
	/**A reference to the forumUI.*/
	private ForumUI forumUI;
	
	/**Creates a new MenuBarUI.
	 * @param forumUI A reference to the forumUI.
	 */
	public MenuBarUI(ForumUI forumUI) {
		this.forumUI = forumUI;
		
		ResourceBundle langBundle = LanguageManager.getLangBundle();
		
		menubar = new MenuBar();
		
		//ACCOUNT MENU
		Menu accountMenu = new Menu(String.format(langBundle.getString("loggedInAsButton"), LoginControl.getUser().getUsername()));
		MenuItem logoutItem = new MenuItem(langBundle.getString("logoutButton"));
		logoutItem.setOnAction(e -> logoutButtonPress());
		accountMenu.getItems().add(logoutItem);
		
		//OPTIONS MENU
		Menu optionsMenu = new Menu(langBundle.getString("optionsButton"));
		CheckMenuItem darkModeItem = new CheckMenuItem(langBundle.getString("darkModeButton"));
		darkModeItem.setOnAction(e -> themeButtonPress(darkModeItem.isSelected()));
		darkModeItem.setSelected(ThemeManager.isDarkModeEnabled());
		optionsMenu.getItems().addAll(darkModeItem);
		
		//LANGUAGE MENU
		Menu languageMenu = new Menu(langBundle.getString("languageButton"));
		Language currentLang = LanguageManager.getSavedLanguage();
		for(Language lang : Language.values()) { //Automatically lists all Languages.
			CheckMenuItem langItem = new CheckMenuItem(lang.getName());
			langItem.setOnAction(e -> {
				if(langItem.isSelected()) {
					LanguageManager.setLanguage(lang);
					forumUI.restartScene();
				}
			}); 
			
			if(lang == currentLang) {
				langItem.setSelected(true);
			}
			
			languageMenu.getItems().add(langItem);
		}
		
		
		menubar.getMenus().addAll(accountMenu, optionsMenu, languageMenu);
	}
	
	/**Returns the menu bar.
	 * @return The menu bar.
	 */
	public MenuBar getMenuBar() {
		return menubar;
	}
	
	
	/**Called when the <b>Dark mode</b> button is clicked.<br>
	 * Saves the theme and restarts the scene to apply the new theme.
	 * @param darkThemeEnabled The result of <code>isSelected()</code> on the darkModeItem CheckBoxItem.
	 */
	private void themeButtonPress(boolean darkThemeEnabled) {
		ThemeManager.saveTheme(darkThemeEnabled);
		forumUI.restartScene();
	}
	
	/**Triggered when the logout button is clicked. Calls the logout() method in forumUI.*/
	private void logoutButtonPress() {
		forumUI.logout();
	}
	
	
}
