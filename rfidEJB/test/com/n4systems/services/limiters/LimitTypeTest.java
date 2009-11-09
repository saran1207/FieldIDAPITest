package com.n4systems.services.limiters;

import static org.junit.Assert.*;

import org.junit.Test;

public class LimitTypeTest {

	@Test
	public void should_find_first_limit_to_match_the_highest_one() throws Exception {
		LimitType sut = LimitType.DISK_SPACE;
		ResourceLimit oneHundredPercentUsed = new SecondaryOrgResourceLimit();
		oneHundredPercentUsed.setMaximum(20);
		oneHundredPercentUsed.setUsed(20);
		assertEquals(99, sut.getLastPassedThreshold(oneHundredPercentUsed));
	}

	
	@Test
	public void should_find_first_limit_to_match_the_second_highest_one() throws Exception {
		LimitType sut = LimitType.DISK_SPACE;
		ResourceLimit ninetyFivePercentUsed = new SecondaryOrgResourceLimit();
		ninetyFivePercentUsed.setMaximum(20);
		ninetyFivePercentUsed.setUsed(19);
		assertEquals(95, sut.getLastPassedThreshold(ninetyFivePercentUsed));
	}
	
	@Test
	public void should_find_first_limit_to_match_the_lowest_one() throws Exception {
		LimitType sut = LimitType.DISK_SPACE;
		ResourceLimit ninetyPercentUsed = new SecondaryOrgResourceLimit();
		ninetyPercentUsed.setMaximum(20);
		ninetyPercentUsed.setUsed(18);
		assertEquals(90, sut.getLastPassedThreshold(ninetyPercentUsed));
	}
}
