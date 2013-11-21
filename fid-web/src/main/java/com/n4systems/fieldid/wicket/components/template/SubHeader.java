package com.n4systems.fieldid.wicket.components.template;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class SubHeader extends Panel {

    public SubHeader(String id) {
        super(id);
        add(new Label("subHeaderTitle", "Optional Sub-header"));
        add(new Label("subHeaderText", "Content here can use any kind of HTML tags but should be related page header information."));

    }
}
