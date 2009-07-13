package com.n4systems.util;

import java.util.TimeZone;

public interface DateTimeDefinition {

	public abstract String getDateFormat();

	public abstract String getDateTimeFormat();

	public abstract TimeZone getTimeZone();

}