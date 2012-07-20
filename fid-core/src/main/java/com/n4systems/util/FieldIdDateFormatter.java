package com.n4systems.util;
import com.n4systems.model.utils.PlainDate;

import java.text.SimpleDateFormat;
import java.util.Date;



public class FieldIdDateFormatter {
	private Date date;
	private boolean convertTimeZone = true;
	private boolean showTime = true;
	private DateTimeDefinition definition;
	
	public FieldIdDateFormatter(Date date, DateTimeDefinition definition, boolean convertTimeZone, boolean showTime) {
		this.date = date;
		this.convertTimeZone = convertTimeZone;
		this.showTime = showTime;
		this.definition = definition;
	}
	
	public FieldIdDateFormatter(Date date, DateTimeDefinition definition, boolean showAndConvertTime) {
		this(date,
                definition,
                showAndConvertTime,
                showAndConvertTime
        );
	}
	
	public FieldIdDateFormatter(Date date, DateTimeDefinition definition) {
		this(
			date,
			definition,
			!(date instanceof PlainDate)
		);
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
            String timeZone = convertTimeZone ? " " + DateHelper.getTimeZoneShortName(userTime, definition.getTimeZone()) : "";
        	return (new SimpleDateFormat(definition.getDateTimeFormat())).format(userTime) + timeZone;
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
