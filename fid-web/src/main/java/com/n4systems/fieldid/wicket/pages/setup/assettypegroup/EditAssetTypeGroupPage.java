package com.n4systems.fieldid.wicket.pages.setup.assettypegroup;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * Created by tracyshi on 2014-07-31.
 */
public class EditAssetTypeGroupPage extends AddAssetTypeGroupPage {

    private Long assetTypeGroupId;

    public EditAssetTypeGroupPage(PageParameters params) {
        super(params);
        assetTypeGroupId = params.get("uniqueID").toLong();
}

    @Override
    protected AssetTypeGroup getAssetTypeGroup(PageParameters params) {
        return assetTypeGroupService.getAssetTypeGroupById(params.get("uniqueID").toLong());
    }

    @Override
    protected void addNavBar(String navBarId) {
        assetTypeGroupId = assetTypeGroup.getObject().getId();
        NavigationBar navBar = new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(AssetTypeGroupListPage.class).build(),
                aNavItem().label("nav.view").page(ViewAssetTypeGroupPage.class).params(PageParametersBuilder.uniqueId(assetTypeGroupId)).build(),
                aNavItem().label("nav.edit").page(EditAssetTypeGroupPage.class).params(PageParametersBuilder.uniqueId(assetTypeGroupId)).build(),
                aNavItem().label("nav.add").page(AddAssetTypeGroupPage.class).onRight().build());
        add(navBar);
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.edit_asset_type_group"));
    }

    @Override
    protected boolean isEdit() {
        return true;
    }

}
