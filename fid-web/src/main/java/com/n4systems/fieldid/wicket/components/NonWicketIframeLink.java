package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.behavior.SimpleAttributeModifier;

public class NonWicketIframeLink extends NonWicketLink {

    public NonWicketIframeLink(String id, String path, boolean scrolling, int width, int height) {
        super(id, path);

        linkContainer.add(new SimpleAttributeModifier("class", "lightview"));
        linkContainer.add(new SimpleAttributeModifier("rel", "iframe"));
        linkContainer.add(new SimpleAttributeModifier("title", ":: :: scrolling:"+scrolling+", width: "+width+", height: "+height));
    }

}
