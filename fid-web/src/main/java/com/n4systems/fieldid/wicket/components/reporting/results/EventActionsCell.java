package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketIframeLink;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.views.RowView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EventActionsCell extends Panel {

    public EventActionsCell(String id, IModel<RowView> rowModel) {
        super(id);

        WebMarkupContainer actionsLink = new WebMarkupContainer("actionsLink");
        actionsLink.setOutputMarkupId(true);
        actionsLink.add(new ContextImage("dropDownArrow", "images/dropdown_arrow.png"));

        add(actionsLink);

        WebMarkupContainer actionsList = new WebMarkupContainer("actionsList");
        actionsList.setOutputMarkupId(true);

        EventSchedule eventSchedule = (EventSchedule) rowModel.getObject().getEntity();
        Event event = eventSchedule.getEvent();
        Long eventId = event == null ? null : event.getId();

        NonWicketLink viewLink = new NonWicketIframeLink("viewLink", "aHtml/iframe/event.action?uniqueID="+eventId, true, 650, 600);
        NonWicketLink editLink = new NonWicketLink("editLink", "selectEventEdit.action?uniqueID="+eventId);
        NonWicketLink printReportLink = new NonWicketLink("printReportLink", "file/downloadEventCert.action?uniqueID="+eventId + "&reportType=INSPECTION_CERT");

        NonWicketLink startEventLink = new NonWicketLink("startEventLink", "quickEvent.action?assetId="+eventSchedule.getAsset().getId());
        NonWicketLink viewAssetLink = new NonWicketLink("viewAssetLink", "asset.action?uniqueID="+eventSchedule.getAsset().getId());
        NonWicketLink editAssetLink = new NonWicketLink("editAssetLink", "assetEdit.action?uniqueID="+eventSchedule.getAsset().getId());

        boolean localEvent = event != null && event.getSecurityLevel(FieldIDSession.get().getSessionUser().getSecurityFilter().getOwner()).isLocal();
        boolean localEndUser = event != null && event.getSecurityLevel(FieldIDSession.get().getSessionUser().getSecurityFilter().getOwner()) == SecurityLevel.LOCAL_ENDUSER;
        boolean printable = event != null && event.isEventCertPrintable();
        boolean hasCreateEvent = FieldIDSession.get().getSessionUser().hasAccess("createevent");
        boolean hasEditEvent = FieldIDSession.get().getSessionUser().hasAccess("editevent");
        boolean hasTag = FieldIDSession.get().getSessionUser().hasAccess("tag");

        viewLink.setVisible(localEvent || localEndUser);
        editLink.setVisible(hasEditEvent && localEvent);
        printReportLink.setVisible(printable);

        startEventLink.setVisible(hasCreateEvent && localEvent);
        editAssetLink.setVisible(hasTag && localEvent);

        actionsList.add(viewLink);
        actionsList.add(editLink);
        actionsList.add(printReportLink);

        actionsList.add(startEventLink);
        actionsList.add(viewAssetLink);
        actionsList.add(editAssetLink);

        add(actionsList);
    }

}
