package com.n4systems.fieldid.wicket.components.asset.events;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.asset.events.table.*;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.EventByNetworkIdProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class EventListPanel extends Panel {
    
    @SpringBean
    private EventService eventService;

    private EventByNetworkIdProvider dataProvider;

    public EventListPanel(String id, IModel<Asset> assetModel, List<Event.EventState> states) {
        super(id, assetModel);

        Asset asset = assetModel.getObject();

        dataProvider = new EventByNetworkIdProvider(asset.getNetworkId(), "schedule.completedDate", SortOrder.DESCENDING, states);

        SimpleDefaultDataTable table;
        add(table = new SimpleDefaultDataTable<Event>("eventsTable", getEventTableColumns(),dataProvider, 10));

        table.add(new AttributeAppender("class", getTableStyle(asset, states, table)).setSeparator(" "));


    }

    private IModel<String> getTableStyle(final Asset asset, final List<Event.EventState> states, final SimpleDefaultDataTable table) {

        return  new Model<String>() {
            @Override
            public String getObject() {
                String attribute = "";
                if(eventService.countEventsByNetworkId(asset.getNetworkId(), states).intValue() == 0) {
                    attribute = "no_records";
                }else if (table.getPageCount() < 2) {
                    attribute = "no_paging";
                }
                return attribute;
            }

        };
    }

    private List<IColumn<Event>> getEventTableColumns() {
        List<IColumn<Event>> columns = new ArrayList<IColumn<Event>>();

        columns.add(new ResultIconColumn(new FIDLabelModel(""), "status"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.event.state"),"eventState", "eventState.label"));
        columns.add(new EventCompletedColumn(new FIDLabelModel("label.completed"), "schedule.completedDate", "date"));
        columns.add(new EventDueColumn(new FIDLabelModel("label.due"), "schedule.nextDate", "date"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("title.viewevent"), "type.name", "type.name"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.completed_by"), "performedBy", "performedBy.fullName"));
        columns.add(new ResultColumn(new FIDLabelModel("label.result"), "status", "status.displayName"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.event_status"), "eventStatus", "eventStatus.displayName"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.assetstatus"), "assetStatus", "assetStatus.displayName"));

        columns.add(new GpsIconColumn(new FIDLabelModel(""), "latitude"));
        columns.add(new ActionsColumn(new FIDLabelModel(""), "id", this));
        return columns;
    }


    public EventByNetworkIdProvider getDataProvider() {
        return dataProvider;
    }


}
