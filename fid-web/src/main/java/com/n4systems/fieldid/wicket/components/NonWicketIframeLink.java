package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;

public class NonWicketIframeLink extends NonWicketLink implements IHeaderContributor {

    private boolean scrolling;
    private int width;
    private int height;

    public NonWicketIframeLink(String id, String path, boolean scrolling, int width, int height) {
        super(id, path);
        this.scrolling = scrolling;
        this.width = width;
        this.height = height;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        String options = "{ width: " + width + ", height: " + height + ", scrolling:" + scrolling + ", iframe: true }";
        response.renderOnDomReadyJavaScript("$('#"+getLinkMarkupId()+"').colorbox("+options+");");
    }

}
