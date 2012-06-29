package com.n4systems.fieldid.wicket.components.asset.events;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EventMapPanel extends Panel {

    @SpringBean
    private EventService eventService;
    
    public EventMapPanel(String id, IModel<Asset> assetModel) {
        super(id, assetModel);
        
        Asset asset = assetModel.getObject();

        List<Event> events = eventService.getEventsByNetworkId(asset.getNetworkId());

        GoogleMap map;
        add(map = new GoogleMap("map"));
        
        for (Event event: events) {
            if(event.getGpsLocation() != null) {
                map.addLocation(event.getGpsLocation().getLatitude().doubleValue(), event.getGpsLocation().getLongitude().doubleValue());
            }
        }
    }
}
