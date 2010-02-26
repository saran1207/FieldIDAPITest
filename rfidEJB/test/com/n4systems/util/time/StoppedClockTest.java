package com.n4systems.util.time;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;


public class StoppedClockTest extends ClockTestAbstract {

	
	@Test
	public void should_return_the_same_date_instance_on_each_call_to_current_time() throws Exception {
		Date call1 = sut.currentTime();
		waitOneClockCycle();
		Date call2 = sut.currentTime();
		
		assertEquals(call1, call2);
	}

	private void waitOneClockCycle() throws InterruptedException {
		Thread.sleep(1);
	}
	
	@Test
	public void should_have_immutable_date_objects() throws Exception {
		Date call1 = sut.currentTime();
		Date call2 = sut.currentTime();
		
		call1.setTime(12311123);
		assertFalse(12311123 == call2.getTime());
	}

	protected StoppedClock getClock() {
		return new StoppedClock();
	}
}
