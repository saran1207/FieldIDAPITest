package com.n4systems.fieldid.wicket.pages.setup.assetstatus;

import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.setup.assetstatus.AssetStatusListPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.api.Archivable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * This page displays all active (ie. not archived) Asset Statuses.  On this page, the user can edit or archive any
 * statuses listed.
 *
 * It extends <b>AssetStatusListPage</b> so that some functionality and design can be shared with
 * <b>AssetStatusListArchivedPage</b>.
 *
 * Created by Jordan Heath on 31/07/14.
 */
public class AssetStatusListAllPage extends AssetStatusListPage {
    private FIDFeedbackPanel feedbackPanel;

    /**
     * This is the default constructor for this page.  Since we don't actually need any parameters to open the page,
     * it should be the only constructor you ever need.
     */
    public AssetStatusListAllPage() {
        super(new IModel<Archivable.EntityState>() {
            @Override
            public Archivable.EntityState getObject() {
                return Archivable.EntityState.ACTIVE;
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
     * Initialize the page, ensuring that only ACTIVE <b>AssetStatus</b> entities are displayed.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        //add the List Panel...
        add(new AssetStatusListPanel("assetStatusList") {
            //...then perform the necessary Overrides to provide the required data.
            @Override
            protected LoadableDetachableModel<List<AssetStatus>> getAssetStatuses() {
                return new LoadableDetachableModel<List<AssetStatus>>() {
                    @Override
                    protected List<AssetStatus> load() {
                        return assetStatusService.getActiveStatuses();
                    }
                };
            }

            @Override
            protected FIDFeedbackPanel getFeedbackPanel() {
                return feedbackPanel;
            }
        });
    }
}