package com.n4systems.model.tenant;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.services.limiters.LimitType;


public class AlertStatusTest {

	
	private static final int A_LIMIT_LEVEL = 90;
	private AlertStatus sut = new AlertStatus();

	
	@Test
	public void should_update_limit_for_secondary_org_limit() throws Exception {
		sut.setLevel(LimitType.SECONDARY_ORGS, A_LIMIT_LEVEL);
		assertEquals(A_LIMIT_LEVEL, sut.alertLevel(LimitType.SECONDARY_ORGS));
	}
	
	
	@Test
	public void should_clear_status_for_secondary_org() throws Exception {
		sut.setLevel(LimitType.SECONDARY_ORGS, A_LIMIT_LEVEL);
		
		sut.clearStatus(LimitType.SECONDARY_ORGS);
		
		assertEquals(AlertStatus.NORMAL_STATUS, sut.alertLevel(LimitType.SECONDARY_ORGS));
	}
	
	
	@Test
	public void should_find_secondary_org_value_to_be_normal_by_default() throws Exception {
		assertTrue(sut.isLevelAtNormal(LimitType.SECONDARY_ORGS));
	}
	
	@Test
	public void should_find_secondary_org_value_not_to_be_normal_after_being_set() throws Exception {
		sut.setLevel(LimitType.SECONDARY_ORGS, A_LIMIT_LEVEL);
		assertFalse(sut.isLevelAtNormal(LimitType.SECONDARY_ORGS));
	}
	
	
	
}
