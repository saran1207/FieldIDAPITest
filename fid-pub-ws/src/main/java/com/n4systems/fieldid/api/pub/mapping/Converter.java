package com.n4systems.fieldid.api.pub.mapping;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public interface Converter {

	static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault());
	static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.systemDefault());

	public static <T> ValueConverter<T, T> noop() {
		return (x) -> x;
	}

	public static <T> ValueConverter<T, String> convertToString() {
		return (x) -> (x != null) ? x.toString() : null;
	}

	public static ValueConverter<String, BigDecimal> convertToBigDecimal() {
		return (x) -> (x != null && x.length() > 0) ? new BigDecimal(x) : null;
	}

	public static ValueConverter<Date, String> convertDateTimeToString() {
		return (x) -> (x != null) ? dateTimeFormatter.format(x.toInstant()) : null;
	}

	public static ValueConverter<String, Date> convertStringToDateTime() {
		return (x) -> (x != null) ? Date.from(Instant.from(dateTimeFormatter.parse(x))) : null;
	}

	public static ValueConverter<Date, String> convertDateToString() {
		return (x) -> (x != null) ? dateFormatter.format(x.toInstant()) : null;
	}

	public static ValueConverter<String, Date> convertStringToDate() {
		return (x) -> (x != null) ? Date.from(Instant.from(dateFormatter.parse(x))) : null;
	}
}
