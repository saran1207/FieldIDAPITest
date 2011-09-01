package com.n4systems.fieldid.wicket;

import org.apache.wicket.protocol.http.WebApplication;

public interface IApplicationFactory {

	public WebApplication  createTestApplication(ComponentTestInjector injector);
}
