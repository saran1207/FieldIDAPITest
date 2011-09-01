package com.n4systems.fieldid.wicket;


import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Before;

public abstract class WicketPanelTestCase<T extends WicketHarness> extends WicketTestCase<T> {

	
	@Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		setPathContext(PANEL_CONTEXT);
	}
	
	@SuppressWarnings({"serial"})
	@Override
	public void renderFixture(final IFixtureFactory<?> factory) {
		getWicketTester().startPanel( new TestPanelSource() {
			@Override
			public Panel getTestPanel(String id) {
				return (Panel) factory.createFixture(id);
			}
		});
	}
}

