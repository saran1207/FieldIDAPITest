package com.n4systems.fieldid.actions.helpers;

import java.util.Date;

public interface UserDateConverter {

	public String convertDate(Date date);

	public Date convertDate(String date);

	public Date convertToEndOfDay(String date);

	public String convertDateTime(Date date);

	public Date convertDateTime(String date);

	public boolean isValidDate(String date, boolean usingTime);

}