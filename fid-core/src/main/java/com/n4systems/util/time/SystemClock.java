package com.n4systems.util.time;

import java.util.Date;

public class SystemClock implements Clock {

	
	public Date currentTime() {
		
		return new Date();
	}
	
	
}
