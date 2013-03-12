package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;

public class ExternalImage extends WebComponent {

    private final String imageUrl;

    public ExternalImage(String id, String imageUrl) {
        super(id);
        this.imageUrl = imageUrl;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new AttributeModifier("src", getImageUrl()));
    }

    protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag(tag);
            checkComponentTag(tag, "img");
    }

    protected String getImageUrl() {
        return imageUrl;
    }
}
