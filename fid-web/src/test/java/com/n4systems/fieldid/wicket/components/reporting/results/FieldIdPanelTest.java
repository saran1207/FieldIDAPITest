package com.n4systems.fieldid.wicket.components.reporting.results;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebApplication;
import org.junit.Before;

import com.n4systems.fieldid.wicket.ComponentTestInjector;
import com.n4systems.fieldid.wicket.FieldIdTestableApp;
import com.n4systems.fieldid.wicket.IApplicationFactory;
import com.n4systems.fieldid.wicket.WicketHarness;
import com.n4systems.fieldid.wicket.WicketPanelTest;

public abstract class FieldIdPanelTest<T extends WicketHarness, F extends Panel> extends WicketPanelTest<T,F> {

	@Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		initializeApplication(new IApplicationFactory() {			
			@Override public WebApplication createTestApplication(ComponentTestInjector injector) {
				return new FieldIdTestableApp(injector);
			}
		});
	}	
	


}
