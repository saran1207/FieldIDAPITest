package com.n4systems.fieldid.actions.helpers;

import com.n4systems.util.DateHelper;
import rfid.web.helper.SessionUser;

import java.util.Date;
import java.util.TimeZone;

public class SessionUserDateConverter implements UserDateConverter {

	private final SessionUser sessionUser;
	
	public SessionUserDateConverter(SessionUser sessionUser) {
		super();
		this.sessionUser = sessionUser;
	}

	public String convertDate(Date date) {
		return DateHelper.date2String(getDateFormat(), date);
	}

    public Date convertDate(String date) {
        return DateHelper.string2Date(getDateFormat(), date);
    }

    public Date convertDateWithOptionalTime(String date) {
        Date d = convertDateTime(date);  // it *might* have the time, but not required.  if not, just try to parse date stuff.
        if (d==null) {
            d = DateHelper.string2Date(getDateFormat(), date, null);
        }
        return d;
    }


    public Date convertToEndOfDay(String date) {
		Date day = convertDate(date);
		return (day != null) ? DateHelper.getEndOfDay(day) : null;
	}

    public String convertDateTime(Date date) {
        return DateHelper.date2String(getDateTimeFormat(), date, getTimeZone());
    }

    public Date convertDateTime(String date) {
        return DateHelper.string2DateTime(getDateTimeFormat(), date, getTimeZone());
    }

    public String convertDateTimeWithNoTimeZone(Date date) {
        return DateHelper.date2String(getDateTimeFormat(), date);
    }

    public Date convertDateTimeWithNoTimeZone(String date) {
        return DateHelper.string2DateTime(getDateTimeFormat(), date, null);
    }




    public boolean isValidDate(String date, boolean usingTime) {
		// blank dates are ok
		if(date == null || date.trim().length() == 0) {
			return true;
		}
		String format = ( usingTime ) ? getDateTimeFormat() : getDateFormat();
		return  DateHelper.isDateValid(format, date);
	}

	private String getDateFormat() {
		return sessionUser.getDateFormat();
	}
	
	private String getDateTimeFormat() {
		return sessionUser.getDateTimeFormat();
	}

	private TimeZone getTimeZone() {
		return sessionUser.getTimeZone();
	}

	@Override
	public String convertDate(Date date, boolean includeTime) {
		return includeTime ? convertDateTime(date) : convertDate(date);
	}

	@Override
	public Date convertDate(String date, boolean includeTime) {
		return includeTime ? convertDateTime(date) : convertDate(date);
	}
}
