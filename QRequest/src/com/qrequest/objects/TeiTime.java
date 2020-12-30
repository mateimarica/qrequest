package com.qrequest.objects;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.qrequest.helpers.LanguageManager;

/**Repesents a certain point in time.<br>
 * <code>toString()</code> returns relative informal date.<br>(Example: "3 days ago")
 */
@SuppressWarnings("serial")
public class TeiTime extends GregorianCalendar {
	
	private static final int MILLI_PER_MIN = 60000;
	
	private static final int MINS_PER_HOUR = 60;
	private static final int MINS_PER_DAY = 1440;
	private static final int MINS_PER_WEEK = 10080; 
	private static final int MINS_PER_MONTH = 43200;
	private static final int MINS_PER_YEAR = 525600;
	
	
	@Override
	public String toString() {
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		Calendar currentTime = Calendar.getInstance();

		System.out.println();
		long minutesPassed = (currentTime.getTimeInMillis() + Math.abs(currentTime.getTimeZone().getRawOffset()) - this.getTimeInMillis()) / MILLI_PER_MIN;

		switch(LanguageManager.getSavedLanguage()) {
			case ROMANIAN:
				// If less than a minute has passed, print seconds
				if(minutesPassed == 0) {
					return "chiar acum";
				}
				
				// If less than an hour has passed, print minutes
				if(minutesPassed < MINS_PER_HOUR) {
					return "acum " + ((minutesPassed == 1) ? "un minut" : minutesPassed + ((minutesPassed >= 20) ? " de" : "") + " minute");
				}
				
				
				
				// If less than an day has passed, print hours
				if(minutesPassed < MINS_PER_DAY) {
					long hoursPassed = minutesPassed / MINS_PER_HOUR;
					return "acum " + (hoursPassed == 1 ? "o or\u0103" : hoursPassed + ((hoursPassed >= 20) ? " de" : "") + " ore");
				}
				
				// If less than an week has passed, print days
				if(minutesPassed < MINS_PER_WEEK) {
					long daysPassed = minutesPassed / MINS_PER_DAY;
					return "acum " + (daysPassed == 1 ? "o zi" : daysPassed + " zile");
				}
				
				// If less than an month has passed, print weeks
				if(minutesPassed < MINS_PER_MONTH) {
					long weeksPassed = minutesPassed / MINS_PER_WEEK;
					return "acum " + (weeksPassed == 1 ? "o s\u0103pt\u0103mân\u0103" : weeksPassed + " s\u0103pt\u0103mâni");
				}
				
				// If less than an year has passed, print months
				if(minutesPassed < MINS_PER_YEAR) {
					long monthsPassed = minutesPassed / MINS_PER_MONTH;
					return "acum " + (monthsPassed == 1 ? "o lun\u0103" : monthsPassed + " luni");
				} 
				
				return "acum mult timp";
		
			default: 
				// If less than a minute has passed, print seconds
				if(minutesPassed == 0) {
					return "just now";
				}
				
				// If less than an hour has passed, print minutes
				if(minutesPassed < MINS_PER_HOUR) {
					return minutesPassed + " minute" + ((minutesPassed == 1) ? "" : "s") + " ago";
				}
				
				// If less than an day has passed, print hours
				if(minutesPassed < MINS_PER_DAY) {
					long hoursPassed = minutesPassed / MINS_PER_HOUR;
					return hoursPassed + ((hoursPassed == 1) ? " hour ago" : " hours ago");
				}
				
				// If less than an week has passed, print days
				if(minutesPassed < MINS_PER_WEEK) {
					long daysPassed = minutesPassed / MINS_PER_DAY;
					return daysPassed + (daysPassed == 1 ? " day ago" : " days ago");
				}
				
				// If less than an month has passed, print weeks
				if(minutesPassed < MINS_PER_MONTH) {
					long weeksPassed = minutesPassed / MINS_PER_WEEK;
					return weeksPassed + (weeksPassed == 1 ? " week ago" : " weeks ago");
				}
				
				// If less than an year has passed, print months
				if(minutesPassed < MINS_PER_YEAR) {
					long monthsPassed = minutesPassed / MINS_PER_MONTH;
					return monthsPassed + (monthsPassed == 1 ? " month ago" : " months ago");
				}
				
				return "a long time ago";
			
		}
	}
}