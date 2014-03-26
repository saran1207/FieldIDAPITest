package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.model.Event;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class EventLocationPanel extends Panel {

    public EventLocationPanel(String id, IModel<? extends Event> eventModel) {
        super(id);

        setVisible(eventModel.getObject().getGpsLocation() != null && !eventModel.getObject().getGpsLocation().isEmpty());

        add(new GoogleMap<Event>("eventLocation", eventModel.getObject()));

        add(new Label("latitude", new PropertyModel<Long>(eventModel, "gpsLocation.latitude")));
        add(new Label("longitude", new PropertyModel<Long>(eventModel, "gpsLocation.longitude")));
    }
}
