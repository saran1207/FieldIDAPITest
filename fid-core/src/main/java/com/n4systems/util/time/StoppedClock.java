package com.n4systems.util.time;

import java.util.Date;

public class StoppedClock implements Clock {

	private final Date date;

	public StoppedClock() {
		this(new Date());
	}

	public StoppedClock(Date date) {
		this.date = date;
	}
	
	public Date currentTime() {
		return (Date)date.clone();
	}

}
