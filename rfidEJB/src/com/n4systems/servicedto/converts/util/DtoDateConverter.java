package com.n4systems.servicedto.converts.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class DtoDateConverter {
	private static Logger logger = Logger.getLogger(DtoDateConverter.class);  
	
	public static Date convertStringToDate(String stringDate) {
		if (stringDate == null || stringDate.length() == 0)
			return null;

		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
		Date dateConvert = null;
		try {
			dateConvert = df.parse(stringDate);
		} catch (ParseException e) {

			// try another way
			try {
				df = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
				dateConvert = df.parse(stringDate);
			} catch (ParseException ee) {
				// do nothing, return null
				logger.warn("failed to parse string date "
						+ stringDate.toString(), ee);
			}
		}

		return dateConvert;
	}

}
