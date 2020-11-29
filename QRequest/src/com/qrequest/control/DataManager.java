package com.qrequest.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import com.qrequest.exceptions.DatabaseConnectionException;
import com.qrequest.objects.Answer;
import com.qrequest.objects.Credentials;
import com.qrequest.objects.Post;
import com.qrequest.objects.Question;
import com.qrequest.objects.ResultSetWrapper;
import com.qrequest.objects.User;
import com.qrequest.objects.Vote;

//class is abstract so no instances can be made - all references are to be STATIC
//default access modifiers so ONLY control classes can access the DataManager
/**Database manager. Only this class should connect to and access the database.
 * All access modifiers are default or private so that <b>ONLY</b> control classes can access the DataManager.
 */
abstract class DataManager {
	
	/**The connection to the database.
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
	static User getAccount(Credentials creds) throws DatabaseConnectionException {
		checkConnection();
		
		//create query string
		String sqlQuery = "SELECT * FROM Users WHERE "
				+ "Username = '" + creds.getUsername() +  "' AND Password = ";
		
		if(creds.isPasswordHashed()) {
			sqlQuery += "'" + creds.getPassword() +"';";
		} else {
			sqlQuery += "SHA1('" + creds.getPassword() +"');";
		}
		
		//execute SQL query
		ResultSetWrapper rs = executeRetrieveQuery(sqlQuery);

		//build user object
		User user;
		if(rs.next()) {
			user = new User(rs.getString("Username"));
			return user;
		}

		return null;
	}
	
	/**Creates an account with a username & password.
	 * @param username
	 * @param password
	 * @return <code>true</code> if account is created, <code>false</code> if an account with that username already exists.
	 * @throws DatabaseConnectionException If there is no connection to the database.
	 */
	static boolean createAccount(Credentials creds) throws DatabaseConnectionException {
		
		if(accountExists(creds.getUsername())) {
			return false;
		}
		
		executeUpdateQuery("INSERT INTO Users VALUES('" + creds.getUsername() 
							+ "', SHA1('" + creds.getPassword() + "'));");
		return true;
	}
	
	/**Saves a question into the database from a question object.
	 * @param question The question being saved.
	 */
	static void createQuestion(Question question) {
		
		//Must escape all apostrophes with another apostrophe so MySQL recognizes that they aren't quotes
		String title = question.getTitle().replaceAll("'", "''");
		String desc = question.getDescription().replaceAll("'", "''");
		
		executeUpdateQuery("INSERT INTO Questions VALUES("
				+ "'" + title + "', "
				+ "'" + desc + "', "
				+ "'" + question.getAuthor().getUsername() + "', "
				+ "'" + question.getID() + "', "
				+ "CURRENT_TIMESTAMP, "
				+ "NULL);"); //this NULL is where the Tag argument will go
	}
	
	
	/**Syncs a question object with its instance in the database.
	 * @param question The question being updated/refresh
	 */
	static void refreshQuestion(Question question) {
		ResultSetWrapper rs = executeRetrieveQuery("SELECT * FROM Questions WHERE Id = '" + question.getID() + "';");
		
		//updates Question object
		while(rs.next()) {
			question.setDescription(rs.getString("Description"));
		}		
	}
	
	/**Checks if a given username exists as a User in the database.
	 * @param username The username.
	 * @return <code>True</code> if the account exists, <code>false</code> if not.
	 * @throws DatabaseConnectionException If there is no connection to the database.
	 */
	static boolean accountExists(String username) throws DatabaseConnectionException {
		checkConnection();
		
		ResultSetWrapper rs = executeRetrieveQuery("SELECT COUNT(1) AS UserExists FROM Users WHERE UPPER(Username) = UPPER('" + username + "');");
			
		while(rs.next()) {
			if(rs.getInt("UserExists") == 1) {
				return true;
			} 
		}	
		return false;
	}
	
	/**Post an answer.
	 * @param answer The <code>Answer</code> object.
	 */
	static void postAnswer(Answer answer) {
			
		//Must escape all apostrophes with another apostrophe so MySQL recognizes that they aren't quotes
		String answerText = answer.getDescription().replaceAll("'", "''");
		
		executeUpdateQuery("INSERT INTO Answers VALUES("
				+ "'" + answerText + "', "
				+ "'" + answer.getAuthor().getUsername() + "', "
				+ "'" + answer.getID() + "', "
				+ "'" + answer.getQuestion().getID() + "', "
				+ "CURRENT_TIMESTAMP);");
	}	
	
	/**Wrapper method to retrieve all the questions from the database.
	 * @return <code>ArrayList&ltQuestion&gt</code>
	 */
	static ArrayList<Question> getAllQuestions() {
		ArrayList<Post> postList = getAllPosts(null);
		ArrayList<Question> questionList = new ArrayList<>();
		for(int i = 0; i < postList.size(); i++) {
			questionList.add((Question)postList.get(i));
		}
		
		return questionList;
	}
	
	/**Wrapper method to retrieve all the answers to a question.
	 * @param question The <code>Question</code> object.
	 * @return<code>ArrayList&ltAnswer&gt</code>
	 */
	static ArrayList<Answer> getAllAnswers(Question question) {
		ArrayList<Post> postList = getAllPosts(question);
		ArrayList<Answer> answerList = new ArrayList<>();
		for(int i = 0; i < postList.size(); i++) {
			answerList.add((Answer)postList.get(i));
		}
		
		return answerList;
	}
	
	/**Method that does one of two things:<p>
	 * <b>Case 1:</b> <code>Question</code> argument is <code>null</code><br>
	 * - Retrieves all the questions in the database.
	 * <br><br>
	 * <b>Case 2:</b> <code>Question</code> argument is <bold>not</bold> <code>null</code><br>
	 * - Retrieves all the answers to the question.
	 * @param question The <code>Question</code> argument.
	 * @return <code>ArrayList&ltPost&gt</code>
	 */
	private static ArrayList<Post> getAllPosts(Question question) {
		boolean isQuestionMode = question == null;
		
		ArrayList<Post> answerList = new ArrayList<>();
		
		String query;
		
		if(isQuestionMode) {
			query = "SELECT * FROM Questions ORDER BY TimePosted DESC;";
		} else {
			query = "SELECT * FROM Answers "
					+ "WHERE QuestionId = '" + question.getID() + "'"
					+ " ORDER BY TimePosted DESC;";
		}
		
		// Order by TimePosted DESCENDING so that newer answers are at the top
		ResultSetWrapper rs = executeRetrieveQuery(query);
			
		//build Question object and put into ArrayList
		while(rs.next()) {
			String title = null, description;
			User author = null, answerer = null;
			UUID ID;
			Date date;
			if(isQuestionMode) {
				title = rs.getString("Title");
				description = rs.getString("Description");
				author = new User(rs.getString("Author"));
				ID = UUID.fromString(rs.getString("Id"));
				date = rs.getTimestamp("TimePosted");
			} else {
				description = rs.getString("Answer");
				answerer = new User(rs.getString("Answerer"));
				ID = UUID.fromString(rs.getString("Id"));
				date = (Date) rs.getTimestamp("TimePosted");
			}
			
			ResultSetWrapper rs2 = executeRetrieveQuery("SELECT SUM(Vote) AS Votes FROM Votes WHERE PostId = '" + ID + "';");
			
			int votes = 0;
			while(rs2.next()) {
				votes = rs2.getInt("Votes");
			}
			
			ResultSetWrapper rs3 = executeRetrieveQuery("SELECT Vote FROM Votes "
					+ "WHERE PostId = '" + ID + "' AND Voter = '" + LoginControl.getUser() + "';");
			
			int currentUserVote = 0;
			while(rs3.next()) {
				currentUserVote = rs3.getInt("Vote");
			}

			Post post = null;
			if(isQuestionMode) {
				ResultSetWrapper rs4 = executeRetrieveQuery("SELECT COUNT(QuestionId) AS AnswerCount "
						+ "FROM Answers WHERE QuestionId = '" + ID + "';");
				
				int answerCount = 0;
				while(rs4.next()) {
					answerCount = rs4.getInt("AnswerCount");
				}
				
				post = new Question(title, description, author, ID, date, votes, currentUserVote, answerCount);
			} else {
				post = new Answer(description, answerer, question, ID, date, votes, currentUserVote);			
			}
			
			answerList.add(post);	
		}
			
		return answerList;
	}
	
	/**Gets all the Users whose usernames contain the given string.
	 * @param username A string that will be matched to usernames in the database.
	 * @return <code>ArrayList&ltUser&gt</code>
	 */
	static ArrayList<User> getUsers(String username) {
		ArrayList<User> userList = new ArrayList<>();
		
		// Order by TimePosted DESCENDING so that newer posts are at the top
		ResultSetWrapper rs = executeRetrieveQuery("SELECT * FROM Users WHERE Username LIKE '%" + username +"%';");
		
		while(rs.next()) {			
			User user = new User(rs.getString("Username"));					
			userList.add(user);	
		}
		
		return userList;
	}
	
	/**Hashes a password with SHA-1 for local storage of saved credentials.
	 * @param password The password to be hashed.
	 * @return The hashed password.
	 */
	static String hashPassword(String password) {
			ResultSetWrapper rs =  executeRetrieveQuery("SELECT SHA1('" + password +"');");
			rs.next();
			return rs.getString(1);	
	}
	
	/**Add a vote the database. Note: a <code>Vote</code> object contains the <code>Post</code> that it was voted on.
	 * @param vote The vote to be added.
	 */
	static void addVote(Vote vote) {
			if(vote.getVote().value() == 0) {
				executeUpdateQuery("DELETE FROM Votes "
								 + "WHERE PostId = '" + vote.getPost().getID() + "' "
								 + "AND Voter = '" + vote.getVoter() + "';");
			} else {
				executeUpdateQuery("INSERT INTO Votes "
								 + "VALUES(" + vote.getVote().value() 
									+ ", '" + vote.getPost().getID() 
									+ "', '" + vote.getVoter() + "') "
								 + "ON DUPLICATE KEY UPDATE Vote = " + vote.getVote().value() + ";");
			}
	}
	
	/**Updated a post's description in the database.
	 * @param post The <code>Post</code> object with the updated description.
	 */
	static void editPost(Post post) {
		String desc = post.getDescription().replaceAll("'", "''");

		if(post.isQuestion()) {
			executeUpdateQuery("UPDATE Questions SET Description = "
					+ "'" + desc + "' WHERE Id = '" + post.getID() + "';");
		} else {
			executeUpdateQuery("UPDATE Answers SET Answer = "
					+ "'" + desc + "' WHERE Id = '" + post.getID() + "';");
		}
		
	}
	
	/**Deletes a question from the database.
	 * @param question The question being deleted.
	 */
	static void deletePost(Post post) {
			if(post.isQuestion()) { //If the post if a Question:
				executeUpdateQuery("DELETE FROM Questions WHERE Id = '" + post.getID() + "';");
				executeUpdateQuery("DELETE FROM Answers WHERE QuestionId = '" + post.getID() + "';");
			} else { //If the post if an Answer:
				executeUpdateQuery("DELETE FROM Answers WHERE Id = '" + post.getID() + "';");
			}
	}
	
	/**Execute an update-query with no return value.<br>
	 * Example queries: <code> INSERT INTO, UPDATE, DELETE FROM, ...</code>
	 * @param query The SQL query to be executed.
	 */
	private static void executeUpdateQuery(String query) {
		System.out.println(query);
		Statement st;		
		try {
			st = connection.createStatement();
			st.executeUpdate(query);
			st.close();
		} catch (SQLException e) {
			System.err.println("SQL error in DataManager: "
					+ "\n\t=======STACK TRACE=========\\n" + e.getStackTrace()
					+ "\n\t=========MESSAGE===========\\n" + e.getMessage());
		}
	}
	
	/**Execute a retrieve-query with return value(s).<br>
	 * Example: <code>SELECT ...</code>
	 * @param query The SQL query to be executed.
	 * @return The <code>ResultSetWrapper</code> that contains the query's results. Functions the same as a <code>ResultSet</code>.
	 */
	private static ResultSetWrapper executeRetrieveQuery(String query) {
		System.out.println(query);
		Statement st;
		try {
			st = connection.createStatement();
			return new ResultSetWrapper(st.executeQuery(query));
		} catch (SQLException e) {
			System.err.println("SQL error in DataManager: "
					+ "\n\t=======STACK TRACE=========\\n" + e.getStackTrace()
					+ "\n\t=========MESSAGE===========\\n" + e.getMessage());
		}
		return null;
	}

	/**Checks the connection to the database. If connection is not <code>null</code>, no further action is taken.
	 * @throws DatabaseConnectionException If there is no connection to the database.
	 */
	private static void checkConnection() throws DatabaseConnectionException {
		if(connection == null) {
			connection = initializeConnection();
			if(connection == null) {
				throw new DatabaseConnectionException("Could not connect to database.");
			}
		}
	}


}