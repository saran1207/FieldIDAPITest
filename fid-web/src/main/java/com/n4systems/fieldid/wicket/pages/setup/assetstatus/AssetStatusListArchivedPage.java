package com.n4systems.fieldid.wicket.pages.setup.assetstatus;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.setup.assetstatus.AssetStatusListPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.api.Archivable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * This page displays all ARCHIVED Asset Statuses.  On this page, the user can Unarchive any of the listed Asset
 * Statuses.
 *
 * It extends <b>AssetStatusListPage</b> so that some functionality and design can be shared with
 * <b>AssetStatusListArchivedPage</b>.
 *
 * Created by Jordan Heath on 31/07/14.
 */
public class AssetStatusListArchivedPage extends AssetStatusListPage {
    /**
     * This is the default constructor for this page.  Since we don't actually need any parameters to open the page,
     * it should be the only constructor you ever need.
     */
    public AssetStatusListArchivedPage() {
        super(new IModel<Archivable.EntityState>() {
            @Override
            public Archivable.EntityState getObject() {
                return Archivable.EntityState.ARCHIVED;
            }

            @Override
            public void setObject(Archivable.EntityState object) {

            }

            @Override
            public void detach() {

            }
        });
    }

    /**
     * Initialize the page and ensure that all ARCHIVED <b>AssetStatus</b> entities are displayed.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        //add the List Panel...
        add(new AssetStatusListPanel("assetStatusList") {
            //...then perform the necessary Overrides to provide the required data.
            @Override
            protected LoadableDetachableModel<List<AssetStatus>> getAssetStatuses() {
                return new LoadableDetachableModel<List<AssetStatus>>() {
                    @Override
                    protected List<AssetStatus> load() {
                        return assetStatusService.getArchivedStatuses();
                    }
                };
            }
        });
    }
}