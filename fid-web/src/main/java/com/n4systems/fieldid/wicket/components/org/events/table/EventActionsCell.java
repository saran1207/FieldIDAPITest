package com.n4systems.fieldid.wicket.components.org.events.table;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.event.EditPlaceEventPage;
import com.n4systems.fieldid.wicket.pages.event.PlaceEventSummaryPage;
import com.n4systems.model.Event;
import com.n4systems.model.PlaceEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EventActionsCell extends Panel {
    public EventActionsCell(String id, IModel<PlaceEvent> eventModel) {
        super(id);
        
        Event event = eventModel.getObject();

        add(new BookmarkablePageLink<PlaceEventSummaryPage>("viewLink", PlaceEventSummaryPage.class, PageParametersBuilder.id(event.getID())));

        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

        BookmarkablePageLink editLink;
        optionsContainer.add(editLink = new BookmarkablePageLink<Void>("editLink", EditPlaceEventPage.class, PageParametersBuilder.uniqueId(event.getID())));
        editLink.setVisible(FieldIDSession.get().getSessionUser().hasAccess("editevent"));
        
        NonWicketLink printLink;
        optionsContainer.add(printLink = new NonWicketLink("printReportLink", "file/downloadEventCert.action?uniqueID=" + event.getID() + "&reportType=INSPECTION_CERT"));
        printLink.setVisible(event.isEventCertPrintable());

        optionsContainer.setVisible(editLink.isVisible() || printLink.isVisible());

        add(optionsContainer);
    }

}
