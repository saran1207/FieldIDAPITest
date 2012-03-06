package com.n4systems.fieldid.wicket.pages;

import org.apache.wicket.request.flow.RedirectToUrlException;

public class SessionBootedPage extends FieldIDWicketPage {

	public SessionBootedPage() {
		throw new RedirectToUrlException("/booted.action");
	}
	
}
