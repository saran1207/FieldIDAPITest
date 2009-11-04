package com.n4systems.test.helpers;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {

	public static Date oneYearFromToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		return calendar.getTime();
	}
	
	
	
}
