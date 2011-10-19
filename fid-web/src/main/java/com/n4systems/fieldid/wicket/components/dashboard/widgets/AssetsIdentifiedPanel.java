package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public class AssetsIdentifiedPanel extends Panel {
    public AssetsIdentifiedPanel(String id) {
        super(id);
        WebMarkupContainer graph = new WebMarkupContainer("graph");
        graph.setOutputMarkupId(true).setMarkupId(graph.getId());
		add(graph);
    }
}
