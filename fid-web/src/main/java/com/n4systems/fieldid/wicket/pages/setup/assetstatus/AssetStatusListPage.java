package com.n4systems.fieldid.wicket.pages.setup.assetstatus;

import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.model.api.Archivable;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * This is the parent of the two Asset Status list pages; for Unarchived (ie. active) and Archived Asset Statuses.
 *
 * This just defines the basic things that are shared between the two, like navigation bar components, the page title
 * and any other shared components between the two pages.
 *
 * Created by Jordan Heath on 31/07/14.
 */
public abstract class AssetStatusListPage extends FieldIDFrontEndPage {

    protected IModel<Archivable.EntityState> archivableState;

    @SpringBean
    protected AssetStatusService assetStatusService;

    public AssetStatusListPage(IModel<Archivable.EntityState> model) {
        archivableState = model;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_asset_statuses"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }


    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count",
                                                   assetStatusService.getActiveStatusCount()))
                          .page(AssetStatusListAllPage.class)
                          .build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count",
                                                   assetStatusService.getArchivedStatusCount()))
                          .page(AssetStatusListArchivedPage.class)
                          .build(),
                aNavItem().label(new FIDLabelModel("nav.add"))
                          .page(AddAssetStatusPage.class)
                          .onRight()
                          .build()
            )
        );
    }
}