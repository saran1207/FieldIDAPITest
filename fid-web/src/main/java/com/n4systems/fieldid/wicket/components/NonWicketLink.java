package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;

import javax.servlet.ServletContext;

public class NonWicketLink extends Border {

	protected WebMarkupContainer linkContainer;

	public NonWicketLink(String id, String path) {
		super(id);

		setRenderBodyOnly(true);

		linkContainer = new WebMarkupContainer("link");
        linkContainer.setOutputMarkupId(true);

		String contextRoot = determineContext();
		if (!path.startsWith("/")) {
			contextRoot += '/';
		}
		linkContainer.add(new SimpleAttributeModifier("href", contextRoot + path));

		add(linkContainer);
		linkContainer.add(getBodyContainer());
	}

    private String determineContext() {
        ServletContext servletContext = WebApplication.get().getServletContext();
        if (servletContext != null) {
            return ((WebRequest)getRequest()).getHttpServletRequest().getContextPath();
        }
        return "/fieldid";
    }

    protected String getLinkMarkupId() {
        return linkContainer.getMarkupId();
    }

}
