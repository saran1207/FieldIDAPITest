package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.Event;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EventActionsCell extends Panel {
    public EventActionsCell(String id, IModel<Event> eventModel) {
        super(id);
        
        Event event = eventModel.getObject();

        WebMarkupContainer menu = new WebMarkupContainer("menu");
        
        NonWicketLink editLink;
        menu.add(editLink = new NonWicketLink("editLink", "selectEventEdit.action?uniqueID=" + event.getID()));
        editLink.setVisible(FieldIDSession.get().getSessionUser().hasAccess("editevent"));
        
        NonWicketLink printLink;
        menu.add(printLink = new NonWicketLink("printReportLink", "file/downloadEventCert.action?uniqueID=" + event.getID() + "&reportType=INSPECTION_CERT"));
        printLink.setVisible(event.isEventCertPrintable());

        String viewButtonStyle;
        if(!editLink.isVisible() && !printLink.isVisible()) {
            viewButtonStyle = "mattButton";
            menu.setVisible(false);
        } else {
            viewButtonStyle = "mattButtonLeft";
        }

        add(menu);

        add(new NonWicketLink("viewLink", "event.action?uniqueID=" + event.getID(), new AttributeModifier("class", viewButtonStyle)));


        
    }
}
