package com.n4systems.fieldid.wicket.components.asset.events;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.model.Event;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EventMapPanel extends Panel {

    @SpringBean
    private EventService eventService;
    
    public EventMapPanel(String id, List<? extends Event> events) {
        super(id);

        GoogleMap map;
        add(map = new GoogleMap("map", events));
    }
}
