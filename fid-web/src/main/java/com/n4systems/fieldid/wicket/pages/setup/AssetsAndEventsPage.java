package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.wicket.pages.setup.eventStatus.EventStatusListPage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class AssetsAndEventsPage extends SetupPage {

    public AssetsAndEventsPage() {
        WebMarkupContainer allOptionsContainer = new WebMarkupContainer("allOptionsContainer");
        allOptionsContainer.setVisible(getSessionUser().hasAccess("managesystemconfig"));
        allOptionsContainer.setRenderBodyOnly(true);
        allOptionsContainer.add(new BookmarkablePageLink("eventStatusList", EventStatusListPage.class));
        add(allOptionsContainer);
    }
}
