package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;

public class ExternalImage extends WebComponent {

    public ExternalImage(String id, String imageUrl) {
        super(id);
        add(new AttributeModifier("src", imageUrl));
    }

    protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag(tag);
            checkComponentTag(tag, "img");
    }
}
