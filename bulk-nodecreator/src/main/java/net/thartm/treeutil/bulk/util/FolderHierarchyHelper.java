package net.thartm.treeutil.bulk.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class FolderHierarchyHelper {

	public FolderHierarchyHelper(){
	}
	
	/**
	 * Creates a random {@link Date} between the baseTime and now.
	 * 
	 * @param baseTime Is a simple {@link Date}
	 * @return
	 */
	public Date getRandomDateTime(Date baseTime) {
		Calendar cal = Calendar.getInstance();
		long val1 = cal.getTimeInMillis();	
		long val2 = baseTime.getTime();

		Random r = new Random();
		long randomTS = (long) (r.nextDouble() * (val2 - val1)) + val1;
		
		return new Date(randomTS);
	}

	/**
	 * Create a random {@link Date} based hierarchy represented as a String[].
	 * The concept is borrowed from Alfresco's content store concepts to 
	 * equally distribute the amount of content within the repo tree.
	 * 
	 * @param baseTime
	 * @return A String[] that represents a random {@link Date}
	 */
	public String[] getRandomDateHierarchy(Date baseTime) {
		Date randomDateTime = getRandomDateTime(baseTime);
		return this.convertToHierarchy(randomDateTime);
	}
	
	/**
	 * Converts a date to a String[] hierarchy like as follows:
	 * The timestamp 12.11.2011 22:33:55 is converted to [2011,11,12,22,33,55]
	 *  
	 * @param dateTime
	 * @return
	 */
	public String[] convertToHierarchy(Date dateTime){
		Timestamp ts = new Timestamp(dateTime);
		return ts.getHierarchy();
	}

}