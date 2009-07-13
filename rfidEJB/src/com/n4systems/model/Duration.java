package com.n4systems.model;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 	Represents a time duration.  A time duration
 *  consists of a Long time length and a TimeUnit unit for the length.
 *  The getEndDate methods can be used to calculate a Date relative to 
 *  now or the given date
 */
@Embeddable
public class Duration implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final long SECONDS_IN_MINUTE = 60L;
	public static final long SECONDS_IN_HOUR = SECONDS_IN_MINUTE * 60L;
	public static final long SECONDS_IN_DAY = SECONDS_IN_HOUR * 24L;
	public static final long SECONDS_IN_WEEK = SECONDS_IN_DAY * 7L;	
	
	@Column(nullable=false)
	private Long length = 0L;
	
	@Enumerated(EnumType.STRING)
	private TimeUnit unit = TimeUnit.SECONDS;
	
	public Duration() {}

	public Long getLength() {
		return length;
	}

	public Long getLength(TimeUnit toUnit) {
		return toUnit.convert(length, unit);
	}
	
	public void setLength(Long length) {
		this.length = length;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

	/**
	 * Returns the end date for a given start date based on this duration
	 * @param startDate	Date to start the duration calculation from
	 * @return			Date
	 */
	public Date getEndDate(Date startDate) {
		return new Date(startDate.getTime() + getLength(TimeUnit.MILLISECONDS));
	}
	
}
