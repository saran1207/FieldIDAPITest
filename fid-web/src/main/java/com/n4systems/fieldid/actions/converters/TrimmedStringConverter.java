package com.n4systems.fieldid.actions.converters;

import com.n4systems.fieldid.viewhelpers.TrimmedString;
import org.apache.struts2.util.StrutsTypeConverter;

import java.util.Map;

public class TrimmedStringConverter extends StrutsTypeConverter {

	@SuppressWarnings("unchecked")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		return new TrimmedString(values[0]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String convertToString(Map context, Object o) {
		return o != null ? o.toString() : "";
	}

}
