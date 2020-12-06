package com.qrequest.objects;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**Repesents a certain point in time.<br>
 * <code>toString()</code> returns relative informal date.<br>(Example: "3 days ago")
 */
@SuppressWarnings("serial")
public class TeiTime extends GregorianCalendar {

	@Override
	public String toString() {
		Calendar currentTime = Calendar.getInstance();
		long minutesPassed = (currentTime.getTimeInMillis() - this.getTimeInMillis()) / 1000 / 60;
		
		// If less than a minute has passed, print seconds
		if(minutesPassed == 0) {
			return "Just now";
		}
		
		// If less than an hour has passed, print minutes
		if(minutesPassed < 60) {
			return minutesPassed + " minute" + ((minutesPassed == 1) ? "" : "s") + " ago";
		}
		
		// If less than an day has passed, print hours
		if(minutesPassed < (60 * 24)) {
			return (minutesPassed / 60) + " hour" + ((minutesPassed / 60 == 1) ? "" : "s") + " ago";
		}
		
		// If less than an week has passed, print days
		if(minutesPassed < (60 * 24 * 7)) {
			return (minutesPassed / 60 / 24) + " day" + ((minutesPassed / 60 / 24 == 1) ? "" : "s") + " ago";
		}
		
		// If less than an month has passed, print weeks
		if(minutesPassed < (60 * 24 * 30)) {
			return (minutesPassed / 60 / 24 / 7) + " week" + ((minutesPassed / 60 / 24 / 7 == 1) ? "" : "s") + " ago";
		}
		
		// If less than an year has passed, print months
		if(minutesPassed < (60 * 24 * 365)) {
			return (minutesPassed / 60 / 24 / 30) + " month" + ((minutesPassed / 60 / 24 / 30 == 1) ? "" : "s") + " ago";
		}
		
		return "A long time ago";
	}
}