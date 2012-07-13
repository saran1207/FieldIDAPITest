package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.components.NonWicketIframeLink;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.Event;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EventActionsCell extends Panel {
    public EventActionsCell(String id, IModel<Event> eventModel) {
        super(id);
        
        Event event = eventModel.getObject();
        add(new NonWicketIframeLink("viewLink", "aHtml/iframe/event.action?uniqueID=" + event.getID(), true, 650, 600, new AttributeModifier("class", "mattButtonLeft")));
        add(new NonWicketLink("editLink", "selectEventEdit.action?uniqueID=" + event.getID()));
        add(new NonWicketLink("printReportLink", "file/downloadEventCert.action?uniqueID=" + event.getID() + "&reportType=INSPECTION_CERT"));

        
    }
}
