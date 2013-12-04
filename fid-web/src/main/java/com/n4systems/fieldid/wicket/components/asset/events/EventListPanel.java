package com.n4systems.fieldid.wicket.components.asset.events;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.asset.events.table.*;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Event;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EventListPanel extends Panel {

    public static final int EVENTS_PER_PAGE = 10;
    @SpringBean
    private EventService eventService;

    private FieldIDDataProvider<Event> dataProvider;

    public EventListPanel(String id, FieldIDDataProvider<Event> dataProvider) {
        super(id);

        this.dataProvider = dataProvider;

        SimpleDefaultDataTable table;
        add(table = new SimpleDefaultDataTable<Event>("eventsTable", getEventTableColumns(), dataProvider, EVENTS_PER_PAGE));

        table.add(new AttributeAppender("class", getTableStyle(table.getPageCount())).setSeparator(" "));
    }

    private IModel<String> getTableStyle(final int pageCount) {

        return  new Model<String>() {
            @Override
            public String getObject() {
                String attribute = "";
                if(dataProvider.size() == 0) {
                    attribute = "no_records";
                }else if (pageCount < 2) {
                    attribute = "no_paging";
                }
                return attribute;
            }
        };
    }

    private List<IColumn<? extends Event>> getEventTableColumns() {
        List<IColumn<? extends Event>> columns = Lists.newArrayList();

        columns.add(new ResultIconColumn("status"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.event.state"),"workflowState", "workflowState.label"));
        columns.add(new EventCompletedColumn(new FIDLabelModel("label.completed"), "completedDate", "date"));
        columns.add(new EventDueColumn(new FIDLabelModel("label.due"), "dueDate", "date"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("title.viewevent"), "type.name", "type.name"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.completed_by"), "performedBy.firstName", "performedBy.fullName"));
        columns.add(new ResultColumn(new FIDLabelModel("label.result"), "eventResult", "eventResult.displayName"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.event_status"), "eventStatus", "eventStatus.displayName"));
        addCustomColumns(columns);

        columns.add(new GpsIconColumn("latitude"));
        addActionColumn(columns);
        return columns;
    }

    protected void addActionColumn(List<IColumn<? extends Event>> columns) {}

    protected void addCustomColumns(List<IColumn<? extends Event>> columns) {}

    public FieldIDDataProvider<Event> getDataProvider() {
        return dataProvider;
    }


}
