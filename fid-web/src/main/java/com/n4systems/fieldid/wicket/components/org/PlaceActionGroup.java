package com.n4systems.fieldid.wicket.components.org;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

public class PlaceActionGroup extends Panel {

    public PlaceActionGroup(String id) {
        super(id);

        add(new Link<Void>("scheduleLink") {
            @Override
            public void onClick() {
            }
        });

        add(new Link<Void>("scheduleLink") {
            @Override
            public void onClick() {
            }
        });

    }
}
