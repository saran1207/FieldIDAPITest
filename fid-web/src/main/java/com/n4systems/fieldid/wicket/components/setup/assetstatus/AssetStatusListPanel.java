package com.n4systems.fieldid.wicket.components.setup.assetstatus;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetStatus;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

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

    public static final int ASSETS_PER_PAGE = 200;

    @SpringBean
    protected AssetStatusService assetStatusService;

    private FieldIDDataProvider<AssetStatus> dataProvider;

    /**
     * This is the main constructor for the <b>AssetStatusListPanel</b>.  It simply needs a <b>String</b> parameter
     * representing the wicket:id of the component.
     *
     * @param id - A <b>String</b> value representing the wicket:id of the component.
     */
    public AssetStatusListPanel(String id,
                                FieldIDDataProvider<AssetStatus> dataProvider) {
        super(id);

        this.dataProvider = dataProvider;

        add(new SimpleDefaultDataTable<AssetStatus>("assetStatusTable",
                                                    getAssetStatusColumns(),
                                                    dataProvider,
                                                    ASSETS_PER_PAGE));



        WebMarkupContainer noResults;
        add(noResults = new WebMarkupContainer("noResults"));

        noResults.setVisible(dataProvider.size() < 1);
    }

    private List<IColumn<AssetStatus>> getAssetStatusColumns() {
        List<IColumn<AssetStatus>> columns = Lists.newArrayList();

        columns.add(new PropertyColumn<AssetStatus>(new FIDLabelModel("label.name"), "name", "name"));

        columns.add(new PropertyColumn<AssetStatus>(new FIDLabelModel("label.createdby"), "createdBy.lastName", "createdBy.displayName"));

        columns.add(new PropertyColumn<AssetStatus>(new FIDLabelModel("label.created"), "created", "created") {
            @Override
            public void populateItem(Item<ICellPopulator<AssetStatus>> item, String componentId, IModel<AssetStatus> rowModel) {
                Date created = rowModel.getObject().getCreated();
                item.add(new Label(componentId, new DayDisplayModel(Model.of(created)).includeTime().withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone())));
            }
        });

        columns.add(new PropertyColumn<AssetStatus>(new FIDLabelModel("label.modifiedby"), "modifiedBy.lastName", "modifiedBy.displayName"));

        columns.add(new PropertyColumn<AssetStatus>(new FIDLabelModel("label.modified"), "modified", "modified") {
            @Override
            public void populateItem(Item<ICellPopulator<AssetStatus>> item, String componentId, IModel<AssetStatus> rowModel) {
                Date created = rowModel.getObject().getCreated();
                item.add(new Label(componentId, new DayDisplayModel(Model.of(created)).includeTime().withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone())));
            }
        });

        addActionColumn(columns);

        return columns;
    }

    protected void addActionColumn(List<IColumn<AssetStatus>> columns) {
        //This does nothing... you need to override it on the implementing page.
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
