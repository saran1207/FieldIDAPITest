package com.n4systems.fieldid.wicket.components.navigation;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;

/* default implementation.  toggles visibility left panel if user clicks on it.
  */
public class LeftPanelController extends Panel {

    public LeftPanelController(String id) {
        super(id);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/leftPanelController.js");
        response.renderOnLoadJavaScript("leftPanelController.init();");
    }
}
