package com.n4systems.fieldid.wicket.pages.setup.assettypegroup;

import com.n4systems.fieldid.service.asset.AssetTypeGroupService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.assettypegroup.AssetTypeGroupDetailsPanel;
import com.n4systems.fieldid.wicket.components.assettypegroup.GroupAssetTypeListPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * Created by tracyshi on 2014-07-31.
 */
public class ViewAssetTypeGroupPage extends FieldIDTemplatePage {

    @SpringBean
    private AssetTypeGroupService assetTypeGroupService;

    private Long assetTypeGroupId;
    private AssetTypeGroup assetTypeGroup;
    private AssetTypeGroupDetailsPanel detailsPanel;
    private GroupAssetTypeListPanel assetTypeListPanel;
    private WebMarkupContainer noAssetTypesUnderGroup;

    public ViewAssetTypeGroupPage(PageParameters params) {
        assetTypeGroupId = params.get("uniqueID").toLong();
        assetTypeGroup = assetTypeGroupService.getAssetTypeGroupById(assetTypeGroupId);
        detailsPanel = new AssetTypeGroupDetailsPanel("assetTypeGroupDetailsPanel", assetTypeGroup);
        add(detailsPanel);
        detailsPanel.setOutputMarkupId(true);

        assetTypeListPanel = new GroupAssetTypeListPanel("groupAssetTypeListPanel", assetTypeGroupId);
        add(assetTypeListPanel);
        add(noAssetTypesUnderGroup = new WebMarkupContainer("noAssetTypesUnderGroup"));
        noAssetTypesUnderGroup.setOutputMarkupPlaceholderTag(true);
        noAssetTypesUnderGroup.setVisible(assetTypeListPanel.isEmpty());

    }

    @Override
    protected void addNavBar(String navBarId) {
        NavigationBar navBar = new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(AssetTypeGroupListPage.class).build(),
                aNavItem().label("nav.view").page(ViewAssetTypeGroupPage.class).params(PageParametersBuilder.uniqueId(assetTypeGroupId)).build(),
                aNavItem().label("nav.edit").page(EditAssetTypeGroupPage.class).params(PageParametersBuilder.uniqueId(assetTypeGroupId)).build(),
                aNavItem().label("nav.add").page(AddAssetTypeGroupPage.class).onRight().build());
        add(navBar);
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.view_asset_type_group"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

}
