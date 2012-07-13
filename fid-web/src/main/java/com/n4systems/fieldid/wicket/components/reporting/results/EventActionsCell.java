package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketIframeLink;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.event.ResolveEventPage;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.views.RowView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EventActionsCell extends Panel {

    public EventActionsCell(String id, IModel<RowView> rowModel) {
        super(id);

        WebMarkupContainer actionsLink = new WebMarkupContainer("actionsLink");
        actionsLink.setOutputMarkupId(true);
        actionsLink.add(new ContextImage("dropDownArrow", "images/dropdown_arrow.png"));

        add(actionsLink);

        Event event = (Event) rowModel.getObject().getEntity();
        Long eventId = event.getId();

        boolean localEvent = event.getSecurityLevel(FieldIDSession.get().getSessionUser().getSecurityFilter().getOwner()).isLocal();
        boolean localEndUser = event.getSecurityLevel(FieldIDSession.get().getSessionUser().getSecurityFilter().getOwner()) == SecurityLevel.LOCAL_ENDUSER;
        boolean printable = event.isEventCertPrintable();
        boolean isReadOnly = FieldIDSession.get().getSessionUser().isReadOnlyUser();
        boolean hasCreateEvent = FieldIDSession.get().getSessionUser().hasAccess("createevent");
        boolean hasEditEvent = FieldIDSession.get().getSessionUser().hasAccess("editevent");
        boolean hasTag = FieldIDSession.get().getSessionUser().hasAccess("tag");

        WebMarkupContainer completeEventActionsList = createCompleteEventActionsList(event, eventId, localEvent, localEndUser, printable, hasCreateEvent, hasEditEvent, hasTag);
        WebMarkupContainer incompleteEventActionsList = createIncompleteEventActionsList(event, isReadOnly, hasCreateEvent, hasTag);

        completeEventActionsList.setVisible(event.getEventState() == Event.EventState.COMPLETED);
        incompleteEventActionsList.setVisible(event.getEventState() != Event.EventState.COMPLETED);

        add(completeEventActionsList);
        add(incompleteEventActionsList);
    }

    private WebMarkupContainer createIncompleteEventActionsList(final Event event, boolean isReadOnly, boolean hasCreateEvent, boolean hasTag) {
        WebMarkupContainer incompleteEventActionsList = new WebMarkupContainer("incompleteEventActionsList");
        incompleteEventActionsList.setOutputMarkupId(true);

        Link resolveEventLink = new Link("resolveEventLink") {
            @Override public void onClick() {
                setResponsePage(new ResolveEventPage(event.getId()));
            }
        };

        NonWicketLink startEventLink = new NonWicketLink("startEventLink", "selectEventAdd.action?scheduleId="+event.getId()+"&type="+event.getType().getId()+"&assetId="+event.getAsset().getId());
        NonWicketLink viewSchedulesLink = new NonWicketLink("viewSchedulesLink", "eventScheduleList.action?assetId="+event.getAsset().getId());

        NonWicketLink editSchedulesLink = new NonWicketLink("editScheduleLink", "eventScheduleList.action?assetId="+event.getAsset().getId());
        NonWicketLink deleteScheduleLink = new NonWicketLink("deleteScheduleLink", "eventScheduleDelete.action?uniqueID="+event.getId() +"&assetId="+event.getAsset().getId());

        BookmarkablePageLink viewAssetLink = new BookmarkablePageLink<Void>("viewAssetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(event.getAsset().getId()));

        NonWicketLink editAssetLink = new NonWicketLink("editAssetLink", "assetEdit.action?uniqueID="+event.getAsset().getId());

        editSchedulesLink.setVisible(!isReadOnly);
        deleteScheduleLink.setVisible(!isReadOnly);
        startEventLink.setVisible(hasCreateEvent);
        editAssetLink.setVisible(hasTag);

        incompleteEventActionsList.add(startEventLink);
        incompleteEventActionsList.add(viewSchedulesLink);

        incompleteEventActionsList.add(editSchedulesLink);
        incompleteEventActionsList.add(deleteScheduleLink);

        incompleteEventActionsList.add(editSchedulesLink);
        incompleteEventActionsList.add(deleteScheduleLink);

        incompleteEventActionsList.add(viewAssetLink);
        incompleteEventActionsList.add(editAssetLink);

        incompleteEventActionsList.add(resolveEventLink);

        return incompleteEventActionsList;
    }

    private WebMarkupContainer createCompleteEventActionsList(Event event, Long eventId, boolean localEvent, boolean localEndUser, boolean printable, boolean hasCreateEvent, boolean hasEditEvent, boolean hasTag) {
        WebMarkupContainer completeEventActionsList = new WebMarkupContainer("completeEventActionsList");
        completeEventActionsList.setOutputMarkupId(true);

        NonWicketLink viewLink = new NonWicketIframeLink("viewLink", "aHtml/iframe/event.action?uniqueID="+eventId, true, 650, 600);
        NonWicketLink editLink = new NonWicketLink("editLink", "selectEventEdit.action?uniqueID="+eventId);
        NonWicketLink printReportLink = new NonWicketLink("printReportLink", "file/downloadEventCert.action?uniqueID="+eventId + "&reportType=INSPECTION_CERT");

        NonWicketLink startEventLink = new NonWicketLink("startEventLink", "quickEvent.action?assetId="+event.getAsset().getId());
        BookmarkablePageLink viewAssetLink = new BookmarkablePageLink<Void>("viewAssetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(event.getAsset().getId()));
        NonWicketLink editAssetLink = new NonWicketLink("editAssetLink", "assetEdit.action?uniqueID="+event.getAsset().getId());

        viewLink.setVisible(localEvent || localEndUser);
        editLink.setVisible(hasEditEvent && localEvent);
        printReportLink.setVisible(printable);

        startEventLink.setVisible(hasCreateEvent && localEvent);
        editAssetLink.setVisible(hasTag && localEvent);

        completeEventActionsList.add(viewLink);
        completeEventActionsList.add(editLink);
        completeEventActionsList.add(printReportLink);

        completeEventActionsList.add(startEventLink);
        completeEventActionsList.add(viewAssetLink);
        completeEventActionsList.add(editAssetLink);

        return completeEventActionsList;
    }

}
