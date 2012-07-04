package com.n4systems.fieldid.wicket.components.assetsearch.results;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetViewPage;
import com.n4systems.util.views.RowView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class AssetActionsCell extends Panel {

    public AssetActionsCell(String id, IModel<RowView> rowModel, String cellMarkupId) {
        super(id);

        WebMarkupContainer actionsLink = new WebMarkupContainer("actionsLink");
        actionsLink.setOutputMarkupId(true);
        actionsLink.add(new ContextImage("dropwDownArrow", "images/dropdown_arrow.png"));

        add(actionsLink);

        WebMarkupContainer actionsList = new WebMarkupContainer("actionsList");
        actionsList.setOutputMarkupId(true);

        BookmarkablePageLink viewLink = new BookmarkablePageLink<Void>("viewLink", AssetViewPage.class, PageParametersBuilder.uniqueId(rowModel.getObject().getId()));
        NonWicketLink viewEventsLink = new NonWicketLink("viewEventsLink", "assetEvents.action?uniqueID="+rowModel.getObject().getId());
        NonWicketLink viewSchedulesLink = new NonWicketLink("viewSchedulesLink", "eventScheduleList.action?assetId="+rowModel.getObject().getId());


        NonWicketLink startEventLink = new NonWicketLink("startEventLink", "quickEvent.action?assetId="+rowModel.getObject().getId());

        NonWicketLink editAssetLink = new NonWicketLink("editAssetLink", "assetEdit.action?uniqueID="+rowModel.getObject().getId());
        NonWicketLink mergeAssetLink = new NonWicketLink("mergeAssetLink", "assetMergeAdd.action?uniqueID="+rowModel.getObject().getId());

        boolean hasCreateEvent = FieldIDSession.get().getSessionUser().hasAccess("createevent");
        boolean hasTag = FieldIDSession.get().getSessionUser().hasAccess("tag");

        startEventLink.setVisible(hasCreateEvent);
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
