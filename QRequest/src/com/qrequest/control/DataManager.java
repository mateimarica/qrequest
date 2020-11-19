package com.qrequest.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.object.Answer;
import com.qrequest.object.Question;
import com.qrequest.object.User;

//class is abstract so no instances can be made - all references are to be STATIC
//default access modifiers so ONLY control classes can access the DataManager
/**Database manager. Only this class should connect to and access the database.
 * All access modifiers are default or private so that <b>ONLY</b> control classes can access the DataManager.
 */
abstract class DataManager {
	
	/**The connection to the databae.
	 */
	private static Connection connection = initializeConnection();
	
	/**For the initial attempt to create a connection to the database.<br>
	 * @return The connection object. Will be <code>null</code> if connection failed.
	 */
	private static Connection initializeConnection() {
		
		 try {
	         Class.forName("com.mysql.jdbc.Driver").newInstance();
	     } catch (Exception e) {
	      System.err.println(e.toString());
	     }
		
		String url = "***REMOVED***";
	
		try {
			Properties properties = new Properties();
			properties.setProperty("user", "***REMOVED***");
			properties.setProperty("password", "***REMOVED***");
			return DriverManager.getConnection(url, properties);
		} catch (SQLException e) {
			return null;
		} 
	}	
	
	/**Returns a user's account if there is an account associated with the given username & password.
	 * @param username
	 * @param password
	 * @return The associated user. <code>null</code> if no user found.
	 * @throws DatabaseConnectionException If there is no connection to the database.
	 */
	static User getAccount(String username, String password) throws DatabaseConnectionException {
		checkConnection();
		
		User user;
		try {
			Statement st = connection.createStatement();
			
			//create query string
			String sqlQuery = "SELECT * FROM Users WHERE "
							+ "Username = '" + username +  "' AND "
							+ "Password = SHA1('" + password + "');";
		
			System.out.println(sqlQuery);
			
			//execute SQL query
			ResultSet rs = st.executeQuery(sqlQuery);

			//build user object
			if(rs.next()) {
				user = new User(rs.getString("Username"));
				return user;
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error in DataManager.getAccount(). " + e.getMessage());
		}
		
		return null;
	}
	
	/**Creates an account with a username & password.
	 * @param username
	 * @param password
	 */
	static void createAccount(String username, String password) {
		try {
			Statement st = connection.createStatement();
			
			String sqlQuery = "INSERT INTO Users VALUES('" + username 
								+ "', SHA1('" + password + "'));";
			
			System.out.println(sqlQuery);
			
			st.executeUpdate(sqlQuery);
			
		} catch (SQLException e) {
			System.err.println("SQL error in DataManager.createAccount(). " + e.getMessage());
		}
	}
	
	/**Saves a question into the database from a question object.
	 * @param question The question being saved.
	 */
	static void createQuestion(Question question) {
		try {
			Statement st = connection.createStatement();
			
			//Must escape all apostrophes with another apostrophe so MySQL recognizes that they aren't quotes
			String title = question.getTitle().replaceAll("'", "''");
			String desc = question.getDescription().replaceAll("'", "''");
			
			String sqlQuery = "INSERT INTO Questions VALUES("
					+ "'" + title + "', "
					+ "'" + desc + "', "
					+ "'" + question.getAuthor().getUsername() + "', "
					+ "'" + question.getID() + "', "
					+ "CURRENT_TIMESTAMP);";
			
			System.out.println(sqlQuery);
									
							
			
			st.executeUpdate(sqlQuery);
			
		} catch (SQLException e) {
			System.err.println("SQL error in DataManager.createQuestion(). " + e.getMessage());
		}
	}
	
	/**Returns all the questions in the database.
	 * @return All the questions in the database as an {@code ArrayList<Question>}
	 */
	static ArrayList<Question> getAllQuestions() {
		
		ArrayList<Question> questionList = new ArrayList<>();
		try {
			Statement st = connection.createStatement();
			
			// Order by TimePosted DESCENDING so that newer posts are at the top
			String sqlQuery = "SELECT * FROM Questions ORDER BY TimePosted DESC;";
			
			System.out.println(sqlQuery);
			
			ResultSet rs = st.executeQuery(sqlQuery);
			
			
			//build Question object and put into ArrayList
			while(rs.next()) {
				String title = rs.getString("Title");
				String desc = rs.getString("Description");
				User author = new User(rs.getString("Author"));
				String ID = rs.getString("Id");
				Date date = (Date) rs.getTimestamp("TimePosted", Calendar.getInstance());
				
				Question question = new Question(title, desc, author, ID, date);
				
				questionList.add(question);	
			}
			
			return questionList;
		} catch (SQLException e) {
			System.err.println("SQL error in DataManager.getAllQuestions(). " + e.getMessage());
		}
		
		return null;
	}
	
	/**Syncs a question object with its instance in the database.
	 * @param question The question being updated/refresh
	 */
	static void refreshQuestion(Question question) {
		
		try {
			Statement st = connection.createStatement();
			
			// Order by TimePosted DESCENDING so that newer posts are at the top
			String sqlQuery = "SELECT * FROM Questions WHERE Id = '" + question.getID() + "';";
			
			System.out.println(sqlQuery);
			
			ResultSet rs = st.executeQuery(sqlQuery);
			
			//updates Question object
			while(rs.next()) {
				question.updateDescription(rs.getString("Description"));
			}
		} catch (SQLException e) {
			System.err.println("SQL error in DataManager.refreshQuestion(). " + e.getMessage());
		}
	}
	
	/**Checks if a given username exists as a User in the database.
	 * @param username The username.
	 * @return <code>True</code> if the account exists, <code>false</code> if not.
	 * @throws DatabaseConnectionException If there is no connection to the database.
	 */
	static boolean checkIfAccountExists(String username) throws DatabaseConnectionException {
		checkConnection();
		
		try {
			Statement st = connection.createStatement();
			
			// Order by TimePosted DESCENDING so that newer posts are at the top
			String sqlQuery = "SELECT COUNT(1) AS UserExists FROM Users WHERE Username = '" + username + "';";
			
			System.out.println(sqlQuery);
			
			ResultSet rs = st.executeQuery(sqlQuery);
			
			
			//build Question object and put into ArrayList
			while(rs.next()) {
				int title = rs.getInt("UserExists");
				if(title == 0) {
					return false;
				} else {
					return true;
				}
			}
			
		} catch (SQLException e) {
			System.err.println("SQL error in DataManager.checkIfAccountExists(). " + e.getMessage());
		}
		
		return true;
	}
	
	//Post an answer
	static void postAnswer(Answer answer) {
		
		try {
			Statement st = connection.createStatement();
			
			//Must escape all apostrophes with another apostrophe so MySQL recognizes that they aren't quotes
			String answerText = answer.getAnswer().replaceAll("'", "''");
			
			String sqlQuery = "INSERT INTO Answers VALUES("
					+ "'" + answerText + "', "
					+ "'" + answer.getAnswerer().getUsername() + "', "
					+ "'" + answer.getID() + "', "
					+ "'" + answer.getQuestion().getID() + "', "
					+ "CURRENT_TIMESTAMP);";
			
			System.out.println(sqlQuery);
							
			st.executeUpdate(sqlQuery);
			
		} catch (SQLException e) {
			System.err.println("SQL error in DataManager.postAnswer(). " + e.getMessage());
		}
	}
	
	static ArrayList<Answer> getAllAnswers(Question question) {
		
		ArrayList<Answer> answerList = new ArrayList<>();
		
		try {
			Statement st = connection.createStatement();
			
			// Order by TimePosted DESCENDING so that newer answers are at the top
			String sqlQuery = "SELECT * FROM Answers "
					+ "WHERE QuestionId = '" + question.getID() + "'"
					+ " ORDER BY TimePosted DESC;";
			
			System.out.println(sqlQuery);
			
			ResultSet rs = st.executeQuery(sqlQuery);
			
			
			//build Question object and put into ArrayList
			while(rs.next()) {
				String answerText = rs.getString("Answer");
				User answerer = new User(rs.getString("Answerer"));
				UUID answerID = UUID.fromString(rs.getString("Id"));

				Date date = (Date) rs.getTimestamp("TimePosted", Calendar.getInstance());
				
				Answer answer = new Answer(answerText, answerer, question, answerID);
				
				answerList.add(answer);	
			}
			
			return answerList;
		} catch (SQLException e) {
			System.err.println("SQL error in DataManager.getAllAnswers(). " + e.getMessage());
		}
		
		return null;
	}
	
	/**Checks the connection to the database. If connection is not <code>null</code>, no further action is taken.
	 * @throws DatabaseConnectionException If there is no connection to the database.
	 */
	private static void checkConnection() throws DatabaseConnectionException {
		if(connection == null) {
			throw new DatabaseConnectionException("Could not connect to database.");
		}
	}


}