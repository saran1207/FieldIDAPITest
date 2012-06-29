package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Event;
import com.n4systems.model.GpsLocation;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class GpsIconCell extends Panel {

    public GpsIconCell(String id, IModel<Event> eventModel) {
        super(id);

        GpsLocation gpsLocation = eventModel.getObject().getGpsLocation();

        ContextImage image;
        add(image = new ContextImage("gpsIcon", "images/gps-recorded.png"));
        
        if(gpsLocation != null) {
            image.add(new AttributeAppender("title", new FIDLabelModel("label.gps_recorded", gpsLocation.getLatitude(), gpsLocation.getLongitude()).getObject()));
        } else {
            image.setVisible(false);
        }
    }
}
