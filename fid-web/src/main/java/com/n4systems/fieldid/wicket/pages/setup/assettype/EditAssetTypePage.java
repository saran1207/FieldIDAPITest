package com.n4systems.fieldid.wicket.pages.setup.assettype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.AssetType;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class EditAssetTypePage extends AddAssetTypePage {

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
        List<NavigationItem> navItems = Lists.newArrayList();

        navItems.add(aNavItem().label("nav.view_all").page(AssetTypeListPage.class).build());
        navItems.add(aNavItem().label("nav.edit").page(EditAssetTypePage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build());

        if(getTenant().getSettings().isInspectionsEnabled()) {
            navItems.add(aNavItem().label("nav.event_type_associations").page(EventTypeAssociationsPage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build());
            navItems.add(aNavItem().label("nav.schedules").page(AssetTypeSchedulesPage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build());
        }
        navItems.add(aNavItem().label("nav.add").page(AddAssetTypePage.class).onRight().build());

        add(new NavigationBar(navBarId, navItems.toArray(new NavigationItem[0])));
    }

    @Override
    public boolean isEdit() {
        return true;
    }
}
