package com.n4systems.subscription.netsuite.client;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.subscription.netsuite.model.UpgradeSubscriptionResponse;


public class GSONParsingTest {

	
	@Test
	public void should_parse_the_upgrade_cost_value_in() throws Exception {
		UpgradeSubscriptionClient sut = new UpgradeSubscriptionClient();
		UpgradeSubscriptionResponse actual = sut.serializeJson("{upgrade_cost:300.12}");
		assertEquals(300.12, actual.getUpgrade_cost(), 0.001);
	}
}
