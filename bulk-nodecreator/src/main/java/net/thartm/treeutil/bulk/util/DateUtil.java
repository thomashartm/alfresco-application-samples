package net.thartm.treeutil.bulk.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DateUtil {

	public Date getRandomDateTime(Date baseTime) {
		Calendar cal = Calendar.getInstance();
		long val1 = cal.getTimeInMillis();	
		long val2 = baseTime.getTime();

		Random r = new Random();
		long randomTS = (long) (r.nextDouble() * (val2 - val1)) + val1;
		
		return new Date(randomTS);
	}

}
