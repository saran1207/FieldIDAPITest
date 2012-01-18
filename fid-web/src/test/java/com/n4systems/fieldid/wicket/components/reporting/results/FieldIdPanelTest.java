package com.n4systems.fieldid.wicket.components.reporting.results;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebApplication;

import com.n4systems.fieldid.wicket.ComponentTestInjector;
import com.n4systems.fieldid.wicket.FieldIdTestContextHandler;
import com.n4systems.fieldid.wicket.FieldIdTestableApp;
import com.n4systems.fieldid.wicket.FieldIdWicketTestContext;
import com.n4systems.fieldid.wicket.TestContextHandler;
import com.n4systems.fieldid.wicket.WicketHarness;
import com.n4systems.fieldid.wicket.WicketPanelTest;

public abstract class FieldIdPanelTest<T extends WicketHarness, F extends Panel> extends WicketPanelTest<T,F,FieldIdWicketTestContext> {

	@Override
	protected TestContextHandler<FieldIdWicketTestContext> getContextHandler() {
		return new FieldIdTestContextHandler();
	}
	
	@Override
	public WebApplication createApp(ComponentTestInjector injector) {
		return new FieldIdTestableApp(injector);
	}	

}
