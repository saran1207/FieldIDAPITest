package com.n4systems.fieldid.wicket;

import org.apache.wicket.Component;
import org.junit.Before;


public abstract class WicketComponentTest<T extends WicketHarness, F extends Component> extends WicketTest<T,F> {
	
	@Override
	@Before 
	public void setUp() throws Exception {
		super.setUp();
		setPathContext(COMPONENT_CONTEXT);
	}
	
	@Override
	public void renderFixture(IFixtureFactory<F> factory) {
		getWicketTester().startComponent(factory.createFixture("testComponentId"));
	}

}
