package net.thartm.treeutil.bulk.util;

import java.util.Calendar;
import java.util.Date;

class Timestamp {

	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;

	Timestamp(int year, int month, int day, int hour, int minute,
			int second) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	Timestamp(Date dateTime){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTime);
		parseCalendar(cal);
	}
	
	Timestamp(Calendar cal){
		parseCalendar(cal);		
	}
	
	private void parseCalendar(Calendar cal){
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);
		hour = cal.get(Calendar.HOUR_OF_DAY);
		minute = cal.get(Calendar.MINUTE);
		second =  cal.get(Calendar.SECOND);
	}
	
	String[] getHierarchy(){
		return new String[]{
				//month+1 ... increment month by one as January is usually translated to 0 .. 
				String.valueOf(year), String.valueOf(month+1), String.valueOf(day), String.valueOf(hour), String.valueOf(minute)};
	}

	/**
	 * @return the year
	 */
	int getYear() {
		return year;
	}

	/**
	 * @return the month
	 */
	int getMonth() {
		return month;
	}

	/**
	 * @return the day
	 */
	int getDay() {
		return day;
	}

	/**
	 * @return the hour
	 */
	int getHour() {
		return hour;
	}

	/**
	 * @return the minute
	 */
	int getMinute() {
		return minute;
	}

	/**
	 * @return the second
	 */
	int getSecond() {
		return second;
	}
	
}