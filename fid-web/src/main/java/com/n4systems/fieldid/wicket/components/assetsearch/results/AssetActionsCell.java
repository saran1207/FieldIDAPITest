package com.n4systems.fieldid.wicket.components.assetsearch.results;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.event.QuickEventPage;
import com.n4systems.fieldid.wicket.pages.identify.IdentifyOrEditAssetPage;
import com.n4systems.util.views.RowView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class AssetActionsCell extends Panel {

    public AssetActionsCell(String id, IModel<RowView> rowModel) {
        super(id);

        WebMarkupContainer actionsLink = new WebMarkupContainer("actionsLink");
        actionsLink.setOutputMarkupId(true);
        actionsLink.add(new ContextImage("dropwDownArrow", "images/dropdown_arrow.png"));

        add(actionsLink);

        WebMarkupContainer actionsList = new WebMarkupContainer("actionsList");
        actionsList.setOutputMarkupId(true);

        BookmarkablePageLink viewLink = new BookmarkablePageLink<Void>("viewLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(rowModel.getObject().getId()));
        BookmarkablePageLink viewEventsLink = new BookmarkablePageLink<Void>("viewEventsLink", AssetEventsPage.class, PageParametersBuilder.uniqueId(rowModel.getObject().getId()));
        BookmarkablePageLink viewSchedulesLink = new BookmarkablePageLink<Void>("viewSchedulesLink", AssetEventsPage.class, PageParametersBuilder.uniqueId(rowModel.getObject().getId()));

        BookmarkablePageLink startEventLink = new BookmarkablePageLink("startEventLink", QuickEventPage.class, PageParametersBuilder.id(rowModel.getObject().getId()));

        BookmarkablePageLink editAssetLink = new BookmarkablePageLink<Void>("editAssetLink", IdentifyOrEditAssetPage.class, PageParametersBuilder.id(rowModel.getObject().getId()));
        NonWicketLink mergeAssetLink = new NonWicketLink("mergeAssetLink", "assetMergeAdd.action?uniqueID="+rowModel.getObject().getId());

        boolean hasCreateEvent = FieldIDSession.get().getSessionUser().hasAccess("createevent");
        boolean hasTag = FieldIDSession.get().getSessionUser().hasAccess("tag");

        viewEventsLink.setVisible(FieldIDSession.get().getSecurityGuard().isInspectionsEnabled());
        startEventLink.setVisible(hasCreateEvent && FieldIDSession.get().getSecurityGuard().isInspectionsEnabled());
        editAssetLink.setVisible(hasTag);
        mergeAssetLink.setVisible(hasTag);

        actionsList.add(viewLink);
        actionsList.add(viewEventsLink);
        actionsList.add(viewSchedulesLink);

        actionsList.add(startEventLink);

        actionsList.add(editAssetLink);
        actionsList.add(mergeAssetLink);

        add(actionsList);

    }

}
