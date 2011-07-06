package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;

public class NonWicketLink extends Border {

    protected WebMarkupContainer linkContainer;

    public NonWicketLink(String id, String path) {
        super(id);

        setRenderBodyOnly(true);

        linkContainer = new WebMarkupContainer("link");
        linkContainer.add(new SimpleAttributeModifier("href", "/fieldid/"+path));

        add(linkContainer);
        linkContainer.add(getBodyContainer());
    }

}
