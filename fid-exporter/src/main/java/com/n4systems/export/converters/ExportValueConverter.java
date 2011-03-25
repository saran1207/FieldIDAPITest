package com.n4systems.export.converters;

import org.apache.commons.lang.StringUtils;

import com.n4systems.exceptions.NotImplementedException;
import com.thoughtworks.xstream.converters.SingleValueConverter;

public abstract class ExportValueConverter<T> implements SingleValueConverter {

	@SuppressWarnings("unchecked")
	@Override
	public String toString(Object obj) {
		return (obj == null) ? "" : StringUtils.trimToEmpty(convertValue((T)obj));
	}

	public abstract String convertValue(T obj);

	@Override
	public Object fromString(String str) {
		throw new NotImplementedException();
	}
	
}
