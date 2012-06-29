package com.n4systems.fieldid.wicket.components.asset.events;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.asset.events.table.*;
import com.n4systems.fieldid.wicket.data.EventByNetworkIdProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class EventListPanel extends Panel {
    
    @SpringBean
    private EventService eventService;

    public EventListPanel(String id, IModel<Asset> assetModel) {
        super(id, assetModel);

        Asset asset = assetModel.getObject();
        
        DefaultDataTable table;
        add(table = new DefaultDataTable<Event>("eventsTable", getEventTableColumns(), new EventByNetworkIdProvider(asset.getNetworkId(), "schedule.completedDate", SortOrder.DESCENDING), 10));
        if(eventService.countEventsByNetworkId(asset.getNetworkId()).intValue() == 0) {
            table.add(new AttributeAppender("class", " noRecords"));
        }
    }

    private List<IColumn<Event>> getEventTableColumns() {
        List<IColumn<Event>> columns = new ArrayList<IColumn<Event>>();

        columns.add(new ResultIconColumn(new FIDLabelModel(""), "status"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.event.state"),"schedule.status", "schedule.status.label"));
        columns.add(new EventCompletedColumn(new FIDLabelModel("label.completed"), "schedule.completedDate", "date"));
        columns.add(new EventDueColumn(new FIDLabelModel("label.due"), "schedule.nextDate", "date"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("title.viewevent"), "type.name", "type.name"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.completed_by"), "performedBy", "performedBy.fullName"));
        columns.add(new ResultColumn(new FIDLabelModel("label.result"), "status", "status.displayName"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.event_status"), "eventStatus", "eventStatus.displayName"));
        columns.add(new PropertyColumn<Event>(new FIDLabelModel("label.assetstatus"), "assetStatus", "assetStatus.displayName"));

        columns.add(new GpsIconColumn(new FIDLabelModel(""), "latitude"));
        columns.add(new ActionsColumn(new FIDLabelModel(""), "id"));
        return columns;
    }
}
