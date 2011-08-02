package com.n4systems.fieldid.service.user;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.n4systems.exceptions.LoginFailureInfo;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;


public class LoginServiceTest {

	private LoginService fixture = null;  //initialized in each test differently.
	
	// Note : this test implicitly uses default values of AccountPolicy, 
	@Test
	public void testTrackFailedLoginAttempts_until_locking_required() { 
		fixture = new LoginService();
				
		String attemptedUserId = "xxx";
		LoginFailureInfo failureInfo = new LoginFailureInfo(attemptedUserId);
		LoginFailureInfo updatedFailureInfo = fixture.trackLoginFailure(failureInfo).getLoginFailureInfo();
		
		assertEquals(failureInfo.getAttempts(), updatedFailureInfo.getAttempts());
		assertEquals(failureInfo.getDuration(), updatedFailureInfo.getDuration());
		assertEquals(failureInfo.getUserId(), updatedFailureInfo.getUserId());
		
		updatedFailureInfo = fixture.trackLoginFailure(failureInfo).getLoginFailureInfo();
		assertEquals(2, updatedFailureInfo.getAttempts()); 
		assertFalse(updatedFailureInfo.requiresLocking());
				
		updatedFailureInfo = fixture.trackLoginFailure(failureInfo).getLoginFailureInfo();
		assertEquals(3, updatedFailureInfo.getAttempts()); 
		assertFalse(updatedFailureInfo.requiresLocking());
				
		updatedFailureInfo = fixture.trackLoginFailure(failureInfo).getLoginFailureInfo();
		assertEquals(4, updatedFailureInfo.getAttempts()); 
		assertFalse(updatedFailureInfo.requiresLocking());
				
		updatedFailureInfo = fixture.trackLoginFailure(failureInfo).getLoginFailureInfo();
		assertEquals(5, updatedFailureInfo.getAttempts());
		assertTrue(updatedFailureInfo.requiresLocking());				
	}
	
	@Test
	public void testTrackFailedLoginAttempts_for_locked_user() {
		fixture = new LoginService();
		
		User user = UserBuilder.aFullUser().withLocked(true).build();
		LoginFailureInfo failureInfo = new LoginFailureInfo(user, 3, 20);
		
		LoginFailureInfo updatedFailureInfo = fixture.trackLoginFailure(failureInfo).getLoginFailureInfo();		
		assertEquals(1, updatedFailureInfo.getAttempts());
		assertTrue(updatedFailureInfo.isLocked());		
	}
	
	@Test
	public void testTrackFailedLoginAttempts_transient_entries() throws InterruptedException {
		// entries should only stay in memory for a limited time.
		
		fixture = new LoginService() {
			@Override long getExpiryTime() { return 25; }
			@Override TimeUnit getTimeUnits() { return TimeUnit.MILLISECONDS; }			
		};
				
		String attemptedUserId = "xxx";
		LoginFailureInfo failureInfo = new LoginFailureInfo(attemptedUserId);

		// do it twice within time limit..should update it. 
		LoginFailureInfo updatedFailureInfo = fixture.trackLoginFailure(failureInfo).getLoginFailureInfo();
		updatedFailureInfo = fixture.trackLoginFailure(failureInfo).getLoginFailureInfo();
		assertEquals(2, updatedFailureInfo.getAttempts());
		
		// delay...should clear it out of memory by this time. next time we try it will be like the first time again. 
		Thread.sleep(150);		
		
		LoginFailureInfo afterDelayFailureInfo = fixture.trackLoginFailure(failureInfo).getLoginFailureInfo();
		assertEquals(1, afterDelayFailureInfo.getAttempts());		
	}
		
	@Test
	public void test_default_timeout_values() throws InterruptedException {
		fixture = new LoginService();				
		assertEquals(30, fixture.getExpiryTime());
		assertEquals(TimeUnit.MINUTES, fixture.getTimeUnits());		
	}	
	
	@Test
	public void testReset() { 
		fixture = new LoginService();
				
		String attemptedUserId = "xxx";
		LoginFailureInfo failureInfo = new LoginFailureInfo(attemptedUserId);
		LoginFailureInfo updatedFailureInfo = fixture.trackLoginFailure(failureInfo).getLoginFailureInfo();		
		assertEquals(1, updatedFailureInfo.getAttempts());
		
		updatedFailureInfo = fixture.trackLoginFailure(failureInfo).getLoginFailureInfo();
		assertEquals(2, updatedFailureInfo.getAttempts());

		// reset it...next time we do it we'll start from the top.
		fixture.resetFailedLoginAttempts(attemptedUserId);
		updatedFailureInfo = fixture.trackLoginFailure(failureInfo).getLoginFailureInfo();
		assertEquals(1, updatedFailureInfo.getAttempts());  // would have been "3" if we didn't reset.				
	}
	
}
