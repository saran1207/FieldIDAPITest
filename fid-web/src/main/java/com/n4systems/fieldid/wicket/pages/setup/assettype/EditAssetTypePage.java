package com.n4systems.fieldid.wicket.pages.setup.assettype;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.AssetType;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class EditAssetTypePage extends AddAssetTypePage{

    private Long assetTypeId;

    public EditAssetTypePage(PageParameters params) {
        super(params);
        assetTypeId = params.get("uniqueID").toLong();
    }

    @Override
    protected AssetType getAssetType(PageParameters params) {
        return assetTypeService.getAssetType(params.get("uniqueID").toLong());
    }

    @Override
    protected void addNavBar(String navBarId) {
        assetTypeId = assetType.getObject().getId();
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(AssetTypeListPage.class).build(),
                aNavItem().label("nav.view").page("assetType.action").params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.edit").page(EditAssetTypePage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.event_type_associations").page(EventTypeAssociationsPage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.schedules").page(AssetTypeSchedulesPage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("label.subassets").page("assetTypeConfiguration.action").params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.add").page(AddAssetTypePage.class).onRight().build()
        ));
    }

    @Override
    public boolean isEdit() {
        return true;
    }
}
