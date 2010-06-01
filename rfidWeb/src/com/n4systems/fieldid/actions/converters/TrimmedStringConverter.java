package com.n4systems.fieldid.actions.converters;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.n4systems.fieldid.viewhelpers.TrimmedString;

public class TrimmedStringConverter extends StrutsTypeConverter {

	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		return new TrimmedString(values[0]);
	}

	@Override
	public String convertToString(Map context, Object o) {
		return o != null ? o.toString() : "";
	}

}
