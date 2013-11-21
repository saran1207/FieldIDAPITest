package com.n4systems.fieldid.wicket.components.template;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

public class ActionGroup extends Panel{
    public ActionGroup(String id) {
        super(id);

        add(new Link<Void>("proceduresLink") {
            @Override
            public void onClick() {
            }
        });

        add(new Link<Void>("editLink") {
            @Override
            public void onClick() {
            }
        });

    }
}
