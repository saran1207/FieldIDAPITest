package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.wicket.pages.setup.assetstatus.AssetStatusListAllPage;
import com.n4systems.fieldid.wicket.pages.setup.assettype.AssetTypeListPage;
import com.n4systems.fieldid.wicket.pages.setup.eventbook.EventBooksListAllPage;
import com.n4systems.fieldid.wicket.pages.setup.assettypegroup.AssetTypeGroupListPage;
import com.n4systems.fieldid.wicket.pages.setup.eventstatus.EventStatusListPage;
import com.n4systems.fieldid.wicket.pages.setup.eventtypegroup.EventTypeGroupListPage;
import com.n4systems.fieldid.wicket.pages.setup.prioritycode.PriorityCodePage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class AssetsAndEventsPage extends SetupPage {

    public AssetsAndEventsPage() {
        WebMarkupContainer allOptionsContainer = new WebMarkupContainer("allOptionsContainer");
        allOptionsContainer.setVisible(getSessionUser().hasAccess("managesystemconfig"));
        allOptionsContainer.setRenderBodyOnly(true);
        allOptionsContainer.add(new BookmarkablePageLink("assetTypeGroupsList", AssetTypeGroupListPage.class));

        allOptionsContainer.add(new BookmarkablePageLink("eventTypeGroup", EventTypeGroupListPage.class));
        allOptionsContainer.add(new BookmarkablePageLink("eventStatusList", EventStatusListPage.class));
        allOptionsContainer.add(new BookmarkablePageLink("eventBooksListLink", EventBooksListAllPage.class));
        allOptionsContainer.add(new BookmarkablePageLink("assetTypesList", AssetTypeListPage.class));
        allOptionsContainer.add(new BookmarkablePageLink("assetStatusList", AssetStatusListAllPage.class));

        WebMarkupContainer priorityCodeListContainer = new WebMarkupContainer("priorityCodeListContainer");
        priorityCodeListContainer.add(new BookmarkablePageLink("priorityCodeList", PriorityCodePage.class));
        priorityCodeListContainer.setVisible(getSecurityGuard().isInspectionsEnabled());
        allOptionsContainer.add(priorityCodeListContainer);

        add(allOptionsContainer);
    }
}
