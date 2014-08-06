package com.n4systems.fieldid.wicket.components.setup.assetstatus;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.assetstatus.AssetStatusListAllPage;
import com.n4systems.model.AssetStatus;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * This is the Asset List Panel used by both <b>AssetStatusListAllPage</b> and <b>AssetStatusListArchivedPage</b>.
 *
 * It displays statuses provided in the constructor and also attaches an appropriate <b>AssetStatusActionCell</b> based
 * on whether or not the Asset Status is in an ARCHIVED or ACTIVE state.
 *
 * To make use of this component, you'll want to override <i>getAssetStatuses()</i> and ensure that a
 * <b>LoadableDetachableModel<List<AssetStatus>></b> object is returned, populated with the applicable Asset Statuses.
 *
 * Created by Jordan Heath on 31/07/14.
 */
public class AssetStatusListPanel extends Panel {

    /**
     * This is the main constructor for the <b>AssetStatusListPanel</b>.  It simply needs a <b>String</b> parameter
     * representing the wicket:id of the component.
     *
     * @param id - A <b>String</b> value representing the wicket:id of the component.
     */
    public AssetStatusListPanel(String id) {
        super(id);

        final AssetStatusListPanel myself = this;

        add(new ListView<AssetStatus>("list", getAssetStatuses()) {
            @Override
            protected void populateItem(ListItem<AssetStatus> item) {
                AssetStatus assetStatus = item.getModelObject();
                TimeZone timeZone = FieldIDSession.get()
                                                  .getSessionUser()
                                                  .getTimeZone();

                //Asset Status column
                item.add(new Label("name",
                                   new PropertyModel<String>(assetStatus,
                                                             "displayName")));

                //Created column
                item.add(new Label("createdBy",
                         new PropertyModel<String>(assetStatus,
                                                   "createdBy.displayName")).setVisible(assetStatus.getCreatedBy() != null));
                item.add(new Label("createdDate",
                         new DayDisplayModel(new PropertyModel<Date>(assetStatus,
                                                                     "created"),
                                             true,
                                             timeZone)));

                //Modified column
                item.add(new Label("modifiedBy",
                        new PropertyModel<String>(assetStatus,
                                                  "modifiedBy.displayName")).setVisible(assetStatus.getModifiedBy() != null));
                item.add(new Label("modifiedDate",
                         new DayDisplayModel(new PropertyModel<Date>(assetStatus,
                                                                     "modified"),
                                             true,
                                             timeZone)));

                //Action column
                item.add(new AssetStatusActionCell("actionCell",
                                                   item.getModel(),
                                                   myself));
            }
        });

        WebMarkupContainer noResults;
        add(noResults = new WebMarkupContainer("noResults"));

        noResults.setVisible(getAssetStatuses().getObject().size() < 1);
    }

    /**
     * This method needs to be overridden by the class that implements this Panel.  This provides an easy way for the
     * List to be loaded with appropriate data.
     *
     * @return A null value, because you need to Override this method, not just use the default.
     */
    protected LoadableDetachableModel<List<AssetStatus>> getAssetStatuses() {
        return null;
    }

    /**
     * This method needs to be overridden by the class that implements this Panel.  This provides an easy way for the
     * internal cells to access the main page.  Why?  Sorcery!!
     *
     * @return A null value, because you need to Override this method, not just use the default.
     */
    protected FIDFeedbackPanel getFeedbackPanel() {
        return null;
    }
}
