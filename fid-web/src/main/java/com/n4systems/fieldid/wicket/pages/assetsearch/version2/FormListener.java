package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

public interface FormListener {
	
	public void handleEvent(AjaxRequestTarget target, Component component, Form<?> form);
	
}
