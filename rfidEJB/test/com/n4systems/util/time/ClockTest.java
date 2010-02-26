package com.n4systems.util.time;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public abstract class ClockTest {

	protected Clock sut;
	
	@Before
	public void createClock() {
		sut = getClock();
	}
	
	@Test
	public void should_return_a_date_from_current_time() throws Exception {
		assertNotNull(sut.currentTime());
	}

	protected abstract Clock getClock();
}
