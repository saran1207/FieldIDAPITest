package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;

public class NonWicketLink extends Border {

    public NonWicketLink(String id, String path) {
        super(id);

        WebMarkupContainer linkContainer = new WebMarkupContainer("link");
        linkContainer.add(new SimpleAttributeModifier("href", "/fieldid/"+path));
        add(linkContainer);
    }

}
