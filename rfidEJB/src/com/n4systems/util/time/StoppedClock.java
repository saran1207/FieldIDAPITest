package com.n4systems.util.time;

import java.util.Date;

public class StoppedClock implements Clock {

	private final Date date;

	public StoppedClock() {
		date = new Date();
	}
	
	public Date currentTime() {
		return (Date)date.clone();
	}

}
