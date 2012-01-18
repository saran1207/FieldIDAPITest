package com.n4systems.util.time;

import java.io.Serializable;
import java.util.Date;

public class SystemClock implements Clock, Serializable {

	
	public Date currentTime() {
		
		return new Date();
	}
	
	
}
