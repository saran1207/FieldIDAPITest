package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.wicket.pages.setup.columnlayout.ColumnsLayoutPage;
import com.n4systems.model.columns.ReportType;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;

public class TemplatesPage extends SetupPage {

    public TemplatesPage() {
        add(new WebMarkupContainer("assetCodeMappingContainer").setVisible(getSecurityGuard().isIntegrationEnabled()));

        add(new BookmarkablePageLink<ColumnsLayoutPage>("assetLayoutLink", ColumnsLayoutPage.class, param("type", ReportType.ASSET)));
        add(new BookmarkablePageLink<ColumnsLayoutPage>("eventLayoutLink", ColumnsLayoutPage.class, param("type", ReportType.EVENT)));
        add(new BookmarkablePageLink<ColumnsLayoutPage>("scheduleLayoutLink", ColumnsLayoutPage.class, param("type", ReportType.SCHEDULE)));
    }

}
