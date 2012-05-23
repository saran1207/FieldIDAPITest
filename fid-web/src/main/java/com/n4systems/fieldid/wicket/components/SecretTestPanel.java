package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class SecretTestPanel extends Panel {
    public SecretTestPanel(String id) {
        super(id);
        add(createComponent("content"));
    }

    protected abstract Component createComponent(String content);

}
