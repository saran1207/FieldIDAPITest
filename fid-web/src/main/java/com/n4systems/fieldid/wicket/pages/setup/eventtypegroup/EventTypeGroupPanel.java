package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.EventTypeGroup;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Date;
import java.util.List;

/**
 * Created by rrana on 2014-08-05.
 */
public class EventTypeGroupPanel extends Panel {

    public static final int EVENT_TYPE_GROUPS_PER_PAGE = 25;

    private FieldIDDataProvider<EventTypeGroup> dataProvider;

    public EventTypeGroupPanel(String id, FieldIDDataProvider<EventTypeGroup> dataProvider) {
        super(id);

        this.dataProvider = dataProvider;

        SimpleDefaultDataTable table;
        add(table = new SimpleDefaultDataTable<EventTypeGroup>("eventTypeGroupTable", getEventTypeGroupTableColumns(), dataProvider, EVENT_TYPE_GROUPS_PER_PAGE));

        table.add(new AttributeAppender("class", getTableStyle()).setSeparator(" "));
    }

    private IModel<String> getTableStyle() {

        return  new Model<String>() {
            @Override
            public String getObject() {
                String attribute = "";
                if(dataProvider.size() == 0) {
                    attribute = "no_records";
                }else if ( (dataProvider.size()/EVENT_TYPE_GROUPS_PER_PAGE) == 0 || dataProvider.size() == EVENT_TYPE_GROUPS_PER_PAGE) {
                    attribute = "no_paging";
                }
                return attribute;
            }
        };
    }

    private List<IColumn<? extends EventTypeGroup>> getEventTypeGroupTableColumns() {
        List<IColumn<? extends EventTypeGroup>> columns = Lists.newArrayList();

        columns.add(new EventTypeGroupColumn(new FIDLabelModel("label.name"),"name", "name"));

        columns.add(new PropertyColumn<EventTypeGroup>(new FIDLabelModel("label.report_title"),"reportTitle", "reportTitle"));

        columns.add(new PropertyColumn<EventTypeGroup>(new FIDLabelModel("label.createdby"),"createdBy.firstName", "createdBy.firstName"));

        columns.add(new PropertyColumn<EventTypeGroup>(new FIDLabelModel("label.created_on"), "created", "created") {
            @Override
            public void populateItem(Item<ICellPopulator<EventTypeGroup>> item, String componentId, IModel<EventTypeGroup> rowModel) {
                Date created = rowModel.getObject().getCreated();
                item.add(new Label(componentId, new DayDisplayModel(Model.of(created)).includeTime().withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone())));
            }
        });

        columns.add(new PropertyColumn<EventTypeGroup>(new FIDLabelModel("label.modifiedby"),"modifiedBy.firstName", "modifiedBy.firstName"));

        columns.add(new PropertyColumn<EventTypeGroup>(new FIDLabelModel("label.modified_on"), "modified", "modified") {
            @Override
            public void populateItem(Item<ICellPopulator<EventTypeGroup>> item, String componentId, IModel<EventTypeGroup> rowModel) {
                Date modified = rowModel.getObject().getModified();
                item.add(new Label(componentId, new DayDisplayModel(Model.of(modified)).includeTime().withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone())));
            }
        });

        addCustomColumns(columns);
        addActionColumn(columns);
        return columns;
    }

    protected void addActionColumn(List<IColumn<? extends EventTypeGroup>> columns) {}

    protected void addCustomColumns(List<IColumn<? extends EventTypeGroup>> columns) {}

    protected FIDFeedbackPanel getErrorFeedbackPanel() { return null; }

    public FieldIDDataProvider<EventTypeGroup> getDataProvider() {
        return dataProvider;
    }

}
