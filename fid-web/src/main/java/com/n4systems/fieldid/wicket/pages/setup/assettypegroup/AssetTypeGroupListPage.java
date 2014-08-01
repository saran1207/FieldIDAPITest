package com.n4systems.fieldid.wicket.pages.setup.assettypegroup;

import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.assettypegroup.AssetTypeGroupListPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.fieldid.wicket.pages.setup.assettype.AddAssetTypePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * Created by tracyshi on 2014-07-31.
 */
public class AssetTypeGroupListPage extends FieldIDTemplatePage {

    private AssetTypeGroupListPanel listPanel;
    private WebMarkupContainer noResults;

    public AssetTypeGroupListPage() {

//        add(new AssetTypeGroupListReorderPanel("reorderPanel") {
//
//        });
        listPanel = new AssetTypeGroupListPanel("assetTypeGroupListPanel");
        add(listPanel);
        listPanel.setOutputMarkupId(true);
        add(noResults = new WebMarkupContainer("noResults"));
        noResults.setOutputMarkupPlaceholderTag(true);
        noResults.setVisible(listPanel.isEmpty());

    }

    @Override
    protected void addNavBar(String navBarId) {
        NavigationBar navBar = new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(AssetTypeGroupListPage.class).build());
//                aNavItem().label("nav.add").page(AddAssetTypeGroupPage.class).onRight().build());
        add(navBar);
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_asset_type_groups.plural"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

}
