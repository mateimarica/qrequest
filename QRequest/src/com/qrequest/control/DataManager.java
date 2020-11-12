package com.qrequest.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.qrequest.object.User;

public class DataManager {
	Connection connection = null;
	
	public DataManager() {
		 try {
	         Class.forName("com.mysql.jdbc.Driver").newInstance();
	     } catch (Exception e) {
	      System.err.println(e.toString());
	     }
		String url = "***REMOVED***";
		try {
			connection = DriverManager.getConnection(url, "***REMOVED***", "***REMOVED***");
		} catch (SQLException e) {
			System.err.println("Database connection error: " + e.getMessage());
		}
	}
	
	public User getAccount(String username, String password) {
		User user = new User();
		try {
			Statement st = connection.createStatement();
			
			//create query string
			String sqlQuery = "SELECT * FROM Users WHERE "
							+ "Username = '" + username +  "' AND "
							+ "Password = sha1('" + password + "');";
		
			//execute SQL query
			ResultSet rs = st.executeQuery(sqlQuery);

			//build user object
			if(rs.next()) {
				user.username = rs.getString("Username");
				return user;
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error in DataManager.getAccount(). " + e.getMessage());
		}
		
		return null;		
	}
}
