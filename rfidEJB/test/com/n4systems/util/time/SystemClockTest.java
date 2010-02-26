package com.n4systems.util.time;



public class SystemClockTest extends ClockTest {

	@Override
	protected Clock getClock() {
		return new SystemClock();
	}

	
}
