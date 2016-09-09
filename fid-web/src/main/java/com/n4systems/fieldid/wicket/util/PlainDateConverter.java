package com.n4systems.fieldid.wicket.util;

import com.n4systems.model.utils.PlainDate;
import org.apache.wicket.util.convert.converter.DateConverter;

import java.util.Date;
import java.util.Locale;

public class PlainDateConverter extends DateConverter {
	
	@Override
	public Date convertToObject(String value, Locale locale) {
		return new PlainDate(super.convertToObject(value, locale));
	}
	
}
