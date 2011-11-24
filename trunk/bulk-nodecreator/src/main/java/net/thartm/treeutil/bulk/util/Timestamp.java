package net.thartm.treeutil.bulk.util;

import java.util.Calendar;
import java.util.Date;

public class Timestamp {

	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;

	public Timestamp(Date dateTime){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTime);
		
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);
		hour = cal.get(Calendar.HOUR_OF_DAY);
		minute = cal.get(Calendar.MINUTE);
		second =  cal.get(Calendar.SECOND);
	}
	
	public String[] getHierarchy(){
		return new String[]{
				String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(hour), String.valueOf(minute)};
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @return the hour
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @return the minute
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * @return the second
	 */
	public int getSecond() {
		return second;
	}
	
}