package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.Event;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;

public class EventByNetworkIdProvider extends FieldIDDataProvider<Event> {

    @SpringBean
    private EventService eventService;

    private Long networkId;
    private String order;

    
    public EventByNetworkIdProvider(Long networkId, String order, SortOrder sortOrder) {
        this.networkId = networkId;
        this.order = order;

        setSort(order, sortOrder);

    }

    @Override
    public Iterator<? extends Event> iterator(int first, int count) {
        return eventService.getEventsByNetworkId(networkId, getSort().getProperty(), getSort().isAscending()).subList(first, first + count).iterator();
    }

    @Override
    public int size() {
        return eventService.countEventsByNetworkId(networkId).intValue();
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
