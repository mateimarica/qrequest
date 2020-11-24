package com.qrequest.exceptions;

/**Thrown when the application cannot connection to the database.<br>
 * Likely caused by not being connected to the UNB VPN.
 */
@SuppressWarnings("serial")
public class DatabaseConnectionException extends Exception {
	
	/**Creates a new DatabaseConnectionException.
	 * @param reason The reason why the exception was thrown.
	 */
	public DatabaseConnectionException(String reason) {
		super(reason);
	}
}