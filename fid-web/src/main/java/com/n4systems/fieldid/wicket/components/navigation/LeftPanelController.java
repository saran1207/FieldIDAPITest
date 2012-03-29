package com.n4systems.fieldid.wicket.components.navigation;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.panel.Panel;

/* default implementation.  toggles visibility left panel if user clicks on it.
  */
public class LeftPanelController extends Panel {

    public LeftPanelController(String id) {
        super(id);
        add(new AttributeAppender("class", "left-panel-controller"));
    }
}
