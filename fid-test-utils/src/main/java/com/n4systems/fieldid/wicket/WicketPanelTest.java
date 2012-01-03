package com.n4systems.fieldid.wicket;


import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.ITestPanelSource;
import org.junit.Before;

@SuppressWarnings("deprecation")
public abstract class WicketPanelTest<T extends WicketHarness, F extends Panel> extends WicketTest<T,F> {

	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		setPathContext(PANEL_CONTEXT);
	}

	@SuppressWarnings({"serial"})
	@Override
	public void renderFixture(final IFixtureFactory<F> factory) {
		getWicketTester().startPanel( new ITestPanelSource() {
			@Override
			public Panel getTestPanel(String id) {
				return factory.createFixture(id);
			}
		});
	}
}

