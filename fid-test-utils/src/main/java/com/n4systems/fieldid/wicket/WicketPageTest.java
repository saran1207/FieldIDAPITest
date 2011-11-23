package com.n4systems.fieldid.wicket;

import static com.google.common.base.Preconditions.*;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.junit.Before;


public abstract class WicketPageTest<T extends WicketHarness, F extends WebPage> extends WicketTest<T,F> {

	@Override
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		setPathContext(PAGE_CONTEXT);
	}
	
	@Override
	public void renderFixture(IFixtureFactory<F> factory) {
		checkNotNull(wicketTester, "you must call initializeApplication() in setup method (@Before) in order for test to run.");		
		Page page = createFixture(factory);
		getWicketTester().startPage(page);
	}
	
}

