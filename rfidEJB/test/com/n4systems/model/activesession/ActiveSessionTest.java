package com.n4systems.model.activesession;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.n4systems.model.builders.UserBuilder;
import com.n4systems.test.helpers.DateHelper;
import com.n4systems.util.time.StoppedClock;
import com.n4systems.util.time.SystemClock;



public class ActiveSessionTest {

	
	private static final int TIMEOUT = 30;
	private static final String SOME_SESSION_ID = "alksdjflaksdjflaksdjfaldskjf";

	@Test
	public void should_find_that_an_active_session_last_touch_less_than_timeout_has_not_expired() throws Exception {
		ActiveSession sut = new ActiveSession(UserBuilder.aUser().build(), SOME_SESSION_ID);
		assertFalse(sut.isExpired(TIMEOUT, new SystemClock()));
	}
	
	@Test
	public void should_find_that_an_active_session_last_touched_more_than_timeout_has_expired() throws Exception {
		ActiveSession sut = new ActiveSession(UserBuilder.aUser().build(), SOME_SESSION_ID);
		int minutesLargerThanTheTimeout = TIMEOUT + 10;
		
		sut.lastTouched = DateHelper.addMinutesToDate(new Date(), -minutesLargerThanTheTimeout);
		
		assertTrue(sut.isExpired(TIMEOUT, new SystemClock()));
	}
	
	@Test
	public void should_find_that_an_active_session_last_touched_is_equal_to_the_timeout_has_expired() throws Exception {
		StoppedClock clock = new StoppedClock();
		ActiveSession sut = new ActiveSession(UserBuilder.aUser().build(), SOME_SESSION_ID);
		
		sut.lastTouched = DateHelper.addMinutesToDate(clock.currentTime(), -TIMEOUT);
		assertTrue(sut.isExpired(TIMEOUT, clock));
	}
	
	@Test
	public void should_find_that_an_active_session_last_touched_is_just_less_than_the_timeout_has_not_expired() throws Exception {
		StoppedClock clock = new StoppedClock();
		ActiveSession sut = new ActiveSession(UserBuilder.aUser().build(), SOME_SESSION_ID);
		
		sut.lastTouched = DateHelper.addMinutesToDate(clock.currentTime(), -(TIMEOUT - 1));
		assertFalse(sut.isExpired(TIMEOUT, clock));
	}
	
	
	
}
