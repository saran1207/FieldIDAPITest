package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.Event;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

public class EventByNetworkIdProvider extends FieldIDDataProvider<Event> {

    @SpringBean
    private EventService eventService;

    private Long networkId;
    private String order;
    private List<Event.EventState> states;

    public EventByNetworkIdProvider(Long networkId, String order, SortOrder sortOrder, List<Event.EventState> states) {
        this.networkId = networkId;
        this.order = order;
        this.states = states;

        setSort(order, sortOrder);
    }

    @Override
    public Iterator<? extends Event> iterator(int first, int count) {
        List<Event> events = eventService.getEventsByNetworkId(networkId, getSort().getProperty(), getSort().isAscending(), states);
        events = events.subList(first, first + count);
        return events.iterator();
    }

    @Override
    public int size() {
        return eventService.countEventsByNetworkId(networkId, states).intValue();
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

    public void setStates(List<Event.EventState> states) {
        this.states = states;
    }
}
