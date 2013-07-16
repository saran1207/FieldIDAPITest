package com.n4systems.fieldid.wicket.pages.setup.assettype;

import com.n4systems.fieldid.wicket.components.assettype.AssetTypeListFilterPanel;
import com.n4systems.fieldid.wicket.components.assettype.AssetTypeListPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class AssetTypeListPage extends FieldIDFrontEndPage {

    AssetTypeListPanel listPanel;

    public AssetTypeListPage() {

        add(new AssetTypeListFilterPanel("filterPanel"){
            @Override
            public void onFilter(AjaxRequestTarget target, String name, AssetTypeGroup group) {
                listPanel.setName(name);
                listPanel.setAssetTypeGroupId(group !=  null ? group.getId() : null);
                target.add(listPanel);
            }
        });
        add(listPanel = new AssetTypeListPanel("listPanel"));
        listPanel.setOutputMarkupId(true);

    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(AssetTypeListPage.class).build(),
                aNavItem().label("nav.add").page("assetTypeEdit.action").onRight().build()
        ));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_asset_types.plural"));
    }
}
