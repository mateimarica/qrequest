package com.qrequest.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;


public class ResultSetWrapper{
	private ResultSet rs;
	
	public ResultSetWrapper(ResultSet resultSet) {
			this.rs = resultSet;
	}
	
	public String getString(String columnName) {
		
		try {
			return rs.getString(columnName);
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
	
	public String getString(int columnNumber) {
		
		try {
			return rs.getString(columnNumber);
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
	
	public int getInt(String columnName) {
		try {
			
			return rs.getInt(columnName);
		} catch (SQLException e) { e.printStackTrace(); }
		return 0;
	}
	
	public Date getTimestamp(String columnName) {
		try {
			return (Date) rs.getTimestamp(columnName, Calendar.getInstance());
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
	

	public boolean next(){
		try {
			return rs.next();
		} catch (SQLException e) { e.printStackTrace(); }
		return false;
	}
}
