package com.n4systems.fieldid.wicket.pages.setup.assetstatus;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.AssetStatus;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * This page is used to edit an existing Asset.  It extends the <b>AddAssetStatusPage</b> and overrides some of the
 * functionality so that the form may easily be used for editing.
 *
 * Created by Jordan Heath on 05/08/14.
 */
public class EditAssetStatusPage extends AddAssetStatusPage {

    private Long assetStatusId;

    public EditAssetStatusPage(PageParameters params) {
        super(params);
        assetStatusId = params.get("assetStatusId").toLong();
    }

    /**
     * This method retrieves the <b>AssetStatus</b> entity using a provided <b>PageParameter</b> object to determine the
     * ID.
     *
     * @param params - A <b>PageParameter</b> object with a Long value keyed, "assetStatusId"
     * @return An <b>AssetStatus</b> object representing the AssetStatus with the provided ID.
     */
    @Override
    protected AssetStatus getAssetStatus(PageParameters params) {
        assetStatusId = params.get("assetStatusId").toLong();
        return assetStatusService.getStatusById(assetStatusId);
    }

    /**
     * This method controls whether or not the form is used for editing an existing AssetStatus.
     *
     * We must override it to set this page to "Edit" mode.
     *
     * @return A boolean value representing whether (true) or not (false) the page is for editing an existing AssetStatus.
     */
    @Override
    public boolean isEdit() {
        return true;
    }

    /**
     * Create a custom navigation bar that includes an additional "Edit" tab.
     *
     * @param navBarId - A String value representing the Wicket ID of the navBar component.
     */
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
                        aNavItem().label(new FIDLabelModel("nav.edit"))
                                .page(EditAssetStatusPage.class)
                                .params(PageParametersBuilder.param("assetStatusId", thisStatus.getObject().getId()))
                                .build(),
                        aNavItem().label(new FIDLabelModel("nav.add"))
                                .page(AddAssetStatusPage.class)
                                .onRight()
                                .build()
                )
        );
    }


}
