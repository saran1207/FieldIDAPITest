package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;

public class NonWicketLink extends Border {

	protected WebMarkupContainer linkContainer;

    public NonWicketLink(String id, String path) {
        this(id, path, null);
    }

	public NonWicketLink(String id, String path, AttributeModifier attributeModifier) {
		super(id);

		setRenderBodyOnly(true);

		linkContainer = new WebMarkupContainer("link");
        linkContainer.setOutputMarkupId(true);

        String absolutePath = ContextAbsolutizer.toContextAbsoluteUrl(path);

		linkContainer.add(new AttributeModifier("href", absolutePath));

        if (attributeModifier != null) {
            linkContainer.add(attributeModifier);
        }

		addToBorder(linkContainer);
		linkContainer.add(getBodyContainer());
	}

    protected String getLinkMarkupId() {
        return linkContainer.getMarkupId();
    }

}
