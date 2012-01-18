package com.n4systems.fieldid.wicket;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.junit.Before;

public abstract class WicketPanelTest<T extends WicketHarness, F extends Panel, C> extends WicketTest<T,F,C> {

	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		setPathContext(PANEL_CONTEXT);
	}

	@Override
	public void renderFixture(final IFixtureFactory<F> factory) {
        getWicketTester().startComponentInPage(factory.createFixture(DummyPanelPage.TEST_PANEL_ID));
	}
}

