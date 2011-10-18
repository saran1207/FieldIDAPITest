package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class Widget extends Panel {

    private static final String CONTENT_ID = "content";

    public Widget(String id, IModel<String> title) {
        super(id);
        setOutputMarkupId(true);
        add(new Label("titleLabel", title));
    }

    public void addContent(Component content) {
        add(content);
    }

    public String getContentId() {
        return CONTENT_ID;
    }

}
