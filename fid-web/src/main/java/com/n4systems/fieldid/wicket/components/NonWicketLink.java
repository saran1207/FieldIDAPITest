package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;

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
		linkContainer.add(new AttributeModifier("href", contextRoot + path));

		addToBorder(linkContainer);
		linkContainer.add(getBodyContainer());
	}

    private String determineContext() {
        ServletContext servletContext = WebApplication.get().getServletContext();
        if (servletContext != null) {
            return ((ServletWebRequest)getRequest()).getContainerRequest().getContextPath();
        }
        return "/fieldid";
    }

    protected String getLinkMarkupId() {
        return linkContainer.getMarkupId();
    }

}
