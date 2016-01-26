package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.event.EditEventPage;
import com.n4systems.fieldid.wicket.pages.event.ThingEventSummaryPage;
import com.n4systems.fieldid.wicket.pages.masterevent.EditMasterEventPage;
import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EventActionsCell extends Panel {
    public EventActionsCell(String id, IModel<ThingEvent> eventModel) {
        super(id);
        
        Event event = eventModel.getObject();

        WebMarkupContainer menu = new WebMarkupContainer("menu");

        BookmarkablePageLink editLink;

        if (!event.getType().isActionEventType() && ((ThingEvent)event).getThingType().isMaster()) {
            menu.add(editLink = new BookmarkablePageLink<EditEventPage>("editLink", EditMasterEventPage.class, PageParametersBuilder.uniqueId(event.getId())));
        } else {
            menu.add(editLink = new BookmarkablePageLink<EditEventPage>("editLink", EditEventPage.class, PageParametersBuilder.uniqueId(event.getId())));
        }

        editLink.setVisible(FieldIDSession.get().getSessionUser().hasAccess("editevent"));

        //TODO replace this with the handleDownload once WEB-5997 is completed
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

        add(new BookmarkablePageLink<ThingEventSummaryPage>("viewLink", ThingEventSummaryPage.class, PageParametersBuilder.id(event.getID()))
                .add(new AttributeModifier("class", viewButtonStyle)));
    }

}
