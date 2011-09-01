package com.n4systems.fieldid.wicket;

import org.apache.wicket.Component;
import org.junit.Before;



public abstract class WicketComponentTestCase<T extends WicketHarness> extends WicketTestCase<T> {
	
	@Override
	@Before 
	public void setUp() throws Exception { 
		super.setUp();
		setPathContext(COMPONENT_CONTEXT);
	}
	
	@Override
	public void renderFixture(IFixtureFactory<?> factory) {
		getWicketTester().startComponent((Component) factory.createFixture("testComponentId"));
	}


}
