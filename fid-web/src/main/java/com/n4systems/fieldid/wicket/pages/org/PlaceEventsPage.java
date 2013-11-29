package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.asset.events.EventListPanel;
import com.n4systems.fieldid.wicket.components.org.events.table.ActionsColumn;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Event;
import com.n4systems.model.WorkflowState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlaceEventsPage extends PlacePage {

    @SpringBean
    private PlaceService placeService;

    private EventListPanel eventPanel;

    private boolean open = true;
    private boolean completed = true;
    private boolean closed = true;

    public PlaceEventsPage(PageParameters params) {
        super(params);

        add(eventPanel = new EventListPanel("eventPanel", getWorkflowStates(), new PlaceEventDataProvider()){
            @Override
            protected void addCustomColumns(List<IColumn<? extends Event>> columns) {
                //add place status
            }

            @Override
            protected void addActionColumn(List<IColumn<? extends Event>> columns) {
                columns.add(new ActionsColumn("id", this));
            }
        });
    }

    private List<WorkflowState> getWorkflowStates() {
        List<WorkflowState> states = new ArrayList<WorkflowState>();

        if(open)
            states.add(WorkflowState.OPEN);
        if(completed)
            states.add(WorkflowState.COMPLETED);
        if(closed)
            states.add(WorkflowState.CLOSED);

        if (states.size() == 0) {
            states.add(WorkflowState.NONE);
        }

        return states;
    }

    private class PlaceEventDataProvider extends FieldIDDataProvider<Event> {

        @Override
        public Iterator<? extends Event> iterator(int first, int count) {
            List<? extends Event> eventsList = placeService.getEventsFor(orgModel.getObject());
            eventsList = eventsList.subList(first, count);
            return eventsList.iterator();
        }

        @Override
        public int size() {
            return placeService.countEventsFor(orgModel.getObject());
        }

        @Override
        public IModel<Event> model(final Event object) {
            return new AbstractReadOnlyModel<Event>() {
                @Override
                public Event getObject() {
                    return (Event) object;
                }
            };
        }
    }
}
