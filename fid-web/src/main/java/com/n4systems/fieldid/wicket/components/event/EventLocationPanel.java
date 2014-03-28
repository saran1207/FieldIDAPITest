package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.components.BigDecimalFmtLabel;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.model.Event;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.math.BigDecimal;

public class EventLocationPanel extends Panel {

    public EventLocationPanel(String id, IModel<? extends Event> eventModel) {
        super(id);

        setVisible(eventModel.getObject().getGpsLocation() != null && !eventModel.getObject().getGpsLocation().isEmpty());

        add(new GoogleMap<Event>("eventLocation", eventModel.getObject()));

        add(new BigDecimalFmtLabel("latitude", new PropertyModel<BigDecimal>(eventModel, "gpsLocation.latitude")));
        add(new BigDecimalFmtLabel("longitude", new PropertyModel<BigDecimal>(eventModel, "gpsLocation.longitude")));
    }
}
