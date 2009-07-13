package com.n4systems.util;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.n4systems.model.utils.PlainDate;



public class FieldidDateFormatter {

	private Date date;
	private boolean convertTimeZone = true;
	private boolean showTime = true;
	private DateTimeDefinition definition;
	
	public FieldidDateFormatter(Date date, DateTimeDefinition definition, boolean convertTimeZone, boolean showTime) {
		super();
		this.date = date;
		this.convertTimeZone = convertTimeZone;
		this.showTime = showTime;
		this.definition = definition;
	}
	
	
	public String format() {
		if (date == null) {
            return "";
		}
		if (date instanceof PlainDate)
        
        if (definition == null) {
        	return "";
        }
        
        Date userTime = convertDate();
        return formatTheDate(userTime);
	}


	private String formatTheDate(Date userTime) {
		if (showTime) {
        	return (new SimpleDateFormat(definition.getDateTimeFormat())).format(userTime) + " " + DateHelper.getTimeZoneShortName(userTime, definition.getTimeZone());
        } else {
        	return (new SimpleDateFormat(definition.getDateFormat())).format(userTime);
        }
	}


	private Date convertDate() {
		Date userTime;
		if (convertTimeZone) {
			userTime = DateHelper.convertToUserTimeZone(date, definition.getTimeZone());
        } else {
        	userTime = date; 
        }
		return userTime;
	}

}
