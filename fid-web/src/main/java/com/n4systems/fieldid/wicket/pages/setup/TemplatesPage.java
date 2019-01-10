package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.autoattributes.AutoAttributeActionsPage;
import com.n4systems.fieldid.wicket.pages.setup.columnlayout.ColumnsLayoutPage;
import com.n4systems.fieldid.wicket.pages.setup.comment.CommentTemplateListPage;
import com.n4systems.model.columns.ReportType;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;

public class TemplatesPage extends SetupPage {

    public TemplatesPage() {
        add(new WebMarkupContainer("assetCodeMappingContainer").setVisible(getSecurityGuard().isIntegrationEnabled()));

        add(new BookmarkablePageLink<WebPage>("autoAttributesViewAllLink", AutoAttributeActionsPage.class,
                PageParametersBuilder.param(AutoAttributeActionsPage.INITIAL_TAB_SELECTION_KEY, AutoAttributeActionsPage.SHOW_AUTO_ATTRIBUTES_VIEW_ALL_PAGE)));

        add(new BookmarkablePageLink<ColumnsLayoutPage>("assetLayoutLink", ColumnsLayoutPage.class, param("type", ReportType.ASSET)));

        add(new BookmarkablePageLink("commentTemplateLink",
                                     CommentTemplateListPage.class));

        WebMarkupContainer eventColumnsLayoutContainer = new WebMarkupContainer("eventColumnsLayoutContainer");
        eventColumnsLayoutContainer.add(new BookmarkablePageLink<ColumnsLayoutPage>("eventLayoutLink", ColumnsLayoutPage.class, param("type", ReportType.EVENT)));

        eventColumnsLayoutContainer.setVisible(getSecurityGuard().isInspectionsEnabled());
        add(eventColumnsLayoutContainer);
    }

}
