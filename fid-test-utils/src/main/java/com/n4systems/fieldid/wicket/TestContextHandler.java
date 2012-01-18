package com.n4systems.fieldid.wicket;

import org.apache.wicket.Session;

public interface TestContextHandler<C> {
	
	void initializeContext(Session session, C tc);

}
