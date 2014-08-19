package com.n4systems.fieldid.wicket.pages.setup.assetstatus;

import com.n4systems.model.api.Archivable;
import org.apache.wicket.model.Model;

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
        super(Model.of(Archivable.EntityState.ARCHIVED));
    }
}