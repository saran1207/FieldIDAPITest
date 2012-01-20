package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;

public interface Mediator {
	
	public void handleEvent(AjaxRequestTarget target, Component component);
	
}
