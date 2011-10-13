package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketIframeLink;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.Event;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.views.RowView;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EventActionsCell extends Panel {

    public EventActionsCell(String id, IModel<RowView> rowModel, String cellMarkupId) {
        super(id);

        WebMarkupContainer actionsLink = new WebMarkupContainer("actionsLink");
        actionsLink.setOutputMarkupId(true);
        actionsLink.add(new ContextImage("dropwDownArrow", "images/dropdown_arrow.png"));

        add(actionsLink);

        WebMarkupContainer actionsList = new WebMarkupContainer("actionsList");
        actionsList.setOutputMarkupId(true);

        Event event = (Event) rowModel.getObject().getEntity();

        NonWicketLink viewLink = new NonWicketIframeLink("viewLink", "aHtml/iframe/event.action?uniqueID="+rowModel.getObject().getId(), true, 650, 600);
        NonWicketLink editLink = new NonWicketLink("editLink", "selectEventEdit.action?uniqueID="+rowModel.getObject().getId());
        NonWicketLink printReportLink = new NonWicketLink("printReportLink", "file/downloadEventCert.action?uniqueID="+rowModel.getObject().getId() + "&reportType=INSPECTION_CERT");
        NonWicketLink startEventLink = new NonWicketLink("startEventLink", "quickEvent.action?assetId="+event.getAsset().getId());
        NonWicketLink viewAssetLink = new NonWicketLink("viewAssetLink", "asset.action?uniqueID="+event.getAsset().getId());
        NonWicketLink editAssetLink = new NonWicketLink("editAssetLink", "assetEdit.action?uniqueID="+event.getAsset().getId());

        boolean localEvent = event.getSecurityLevel(FieldIDSession.get().getSessionUser().getSecurityFilter().getOwner()).isLocal();
        boolean localEndUser = event.getSecurityLevel(FieldIDSession.get().getSessionUser().getSecurityFilter().getOwner()) == SecurityLevel.LOCAL_ENDUSER;
        boolean printable = event.isEventCertPrintable();
        boolean hasCreateEvent = FieldIDSession.get().getSessionUser().hasAccess("createevent");
        boolean hasEditEvent = FieldIDSession.get().getSessionUser().hasAccess("editevent");
        boolean hasTag = FieldIDSession.get().getSessionUser().hasAccess("tag");

        editLink.setVisible(hasEditEvent && localEvent);
        printReportLink.setVisible(printable);
        startEventLink.setVisible(hasCreateEvent && localEvent);
        viewLink.setVisible(localEvent || localEndUser);
        editAssetLink.setVisible(hasTag && localEvent);

        actionsList.add(viewLink);
        actionsList.add(editLink);
        actionsList.add(printReportLink);

        actionsList.add(startEventLink);
        actionsList.add(viewAssetLink);
        actionsList.add(editAssetLink);

        add(actionsList);

        addRepositionMenuJs(actionsLink, cellMarkupId, actionsList);
    }

    private void addRepositionMenuJs(WebMarkupContainer actionsLink, String cellMarkupId, Component actionsList) {
        actionsLink.add(new SimpleAttributeModifier("onmouseover", "positionDropDownForElements($('#"+actionsLink.getMarkupId()+"'),"
                +"$('#"+actionsList.getMarkupId()+"'), $('#"+cellMarkupId+"'));"));
    }

}
