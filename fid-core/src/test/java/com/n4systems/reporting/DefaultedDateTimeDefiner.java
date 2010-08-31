/**
 * 
 */
package com.n4systems.reporting;

import java.util.TimeZone;

import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.util.DateTimeDefinition;

public class DefaultedDateTimeDefiner implements DateTimeDefinition {
	private final DateTimeDefiner definer = new DateTimeDefiner("yyyy-MM-dd", TimeZone.getDefault());

	public boolean equals(Object obj) {
		return definer.equals(obj);
	}

	public String getDateFormat() {
		return definer.getDateFormat();
	}

	public String getDateTimeFormat() {
		return definer.getDateTimeFormat();
	}

	public TimeZone getTimeZone() {
		return definer.getTimeZone();
	}

	public int hashCode() {
		return definer.hashCode();
	}

	public String toString() {
		return definer.toString();
	}
}