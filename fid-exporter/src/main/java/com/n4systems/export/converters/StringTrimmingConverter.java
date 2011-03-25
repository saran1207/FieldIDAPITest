package com.n4systems.export.converters;

import org.apache.commons.lang.StringUtils;

public class StringTrimmingConverter extends ExportValueConverter<String> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(String.class);
	}

	@Override
	public String convertValue(String str) {
		return StringUtils.trimToEmpty(str);
	}

}
