package com.n4systems.util.time;



public class SystemClockTest extends ClockTestAbstract {

	@Override
	protected Clock getClock() {
		return new SystemClock();
	}

	
}
