package com.qrequest.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**Object for wrapping a <code>ResultSet</code> for easy access to its methods. Functions the same as a <code>ResultSet</code>.*/
public class ResultSetWrapper{
	
	/**The wrapped <code>ResultSet</code> object.*/
	private ResultSet rs;
	
	/**Creates a <code>ResultSetWrapper</code> with a <code>ResultSet</code>.*/
	public ResultSetWrapper(ResultSet resultSet) {
			this.rs = resultSet;
	}
	
	/**Retrieves the value of the designated column in the current row
     * of this <code>ResultSetWrapper</code> object as
     * a <code>String</code> in Java.
     * @param columnLabel The name for the column specified with the SQL AS clause.  
     * If the SQL AS clause was not specified, then the label is the name of the column.
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     */
	public String getString(String columnName) {
		
		try {
			return rs.getString(columnName);
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
	
	/**Retrieves the value of the designated column in the current row
     * of this <code>ResultSetWrapper</code> object as
     * a <code>String</code> in Java.
     *
     * @param columnNumber The first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>null</code>
     */
	public String getString(int columnNumber) {
		
		try {
			return rs.getString(columnNumber);
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
	
	 /**Retrieves the value of the designated column in the current row
     * of this <code>ResultSetWrapper</code> object as
     * an <code>int</code> in Java.
     * @param columnName The label for the column specified with the SQL AS clause.  
     * If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL <code>NULL</code>, the
     * value returned is <code>0</code>
     */
	public int getInt(String columnName) {
		try {
			
			return rs.getInt(columnName);
		} catch (SQLException e) { e.printStackTrace(); }
		return 0;
	}
	
	/**
     * Retrieves the value of the designated column in the current row
     * of this <code>ResultSetWrapper</code> object as a <code>java.sql.Timestamp</code> object in Java.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the timestamp if the underlying database does not store
     * timezone information.
     * @param columnName The label for the column specified with the SQL AS clause.  
     * If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value as a <code>java.util.Date</code> object;
     * if the value is SQL <code>NULL</code>,
     * the value returned is <code>null</code> in Java.
     */
	public Date getTimestamp(String columnName) {
		try {
			return (Date) rs.getTimestamp(columnName, Calendar.getInstance());
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
	
	/**
     * Moves the cursor forward one row from its current position.
     * A <code>ResultSet</code> cursor is initially positioned
     * before the first row; the first call to the method
     * <code>next</code> makes the first row the current row; the
     * second call makes the second row the current row, and so on.
     * <p>
     * When a call to the <code>next</code> method returns <code>false</code>,
     * the cursor is positioned after the last row. Any
     * invocation of a <code>ResultSet</code> method which requires a
     * current row will result in a <code>SQLException</code> being thrown.
     *  If the result set type is <code>TYPE_FORWARD_ONLY</code>, it is vendor specified
     * whether their JDBC driver implementation will return <code>false</code>.
     *
     * <P>If an input stream is open for the current row, a call
     * to the method <code>next</code> will
     * implicitly close it. A <code>ResultSet</code> object's
     * warning chain is cleared when a new row is read.
     *
     * @return <code>true</code> if the new current row is valid;
     * <code>false</code> if there are no more rows.
     */
	public boolean next(){
		try {
			return rs.next();
		} catch (SQLException e) { e.printStackTrace(); }
		return false;
	}
}
