package com.n4systems.fieldid.wicket.util;

import java.util.Date;
import java.util.Locale;

import org.apache.wicket.util.convert.converter.DateConverter;

import com.n4systems.model.utils.PlainDate;

public class PlainDateConverter extends DateConverter {
	
	@Override
	public Date convertToObject(String value, Locale locale) {
		return new PlainDate(super.convertToObject(value, locale));
	}
	
}
