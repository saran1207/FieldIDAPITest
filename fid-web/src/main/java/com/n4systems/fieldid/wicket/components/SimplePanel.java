package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

// For some reason Panel was made abstract in Wicket 1.5
public class SimplePanel extends Panel {
    public SimplePanel(String id) {
        super(id);
    }

    public SimplePanel(String id, IModel<?> model) {
        super(id, model);
    }
}
