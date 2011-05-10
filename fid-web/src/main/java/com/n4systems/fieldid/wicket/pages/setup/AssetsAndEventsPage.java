package com.n4systems.fieldid.wicket.pages.setup;

import org.apache.wicket.markup.html.WebMarkupContainer;

public class AssetsAndEventsPage extends SetupPage {

    public AssetsAndEventsPage() {
        WebMarkupContainer allOptionsContainer = new WebMarkupContainer("allOptionsContainer");
        allOptionsContainer.setVisible(getSessionUser().hasAccess("managesystemconfig"));
        allOptionsContainer.setRenderBodyOnly(true);
        add(allOptionsContainer);
    }
}
