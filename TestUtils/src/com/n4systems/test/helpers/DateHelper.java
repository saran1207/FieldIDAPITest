package com.n4systems.test.helpers;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {

	public static Date oneYearFromToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		return calendar.getTime();
	}
	
	public static Date createDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		
		return calendar.getTime();
	}
	
}
