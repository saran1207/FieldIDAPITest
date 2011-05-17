package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.markup.html.link.Link;

public class NonWicketLink extends Link {

    private String path;

    public NonWicketLink(String id, String path) {
        super(id);
        this.path = path;
    }

    @Override
    public void onClick() {
        getResponse().redirect("/fieldid/"+path);
    }

}
