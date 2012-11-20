package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.event.CloseEventPage;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.views.RowView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class EventActionsCell extends Panel {

    @SpringBean
    private AssetService assetService;

    public EventActionsCell(String id, IModel<RowView> rowModel) {
        super(id);

        WebMarkupContainer actionsLink = new WebMarkupContainer("actionsLink");
        actionsLink.setOutputMarkupId(true);
        actionsLink.add(new ContextImage("dropDownArrow", "images/dropdown_arrow.png"));

        add(actionsLink);

        Event event = (Event) rowModel.getObject().getEntity();
        Long eventId = event.getId();

        boolean localEvent = event.getSecurityLevel(FieldIDSession.get().getSessionUser().getSecurityFilter().getOwner()).isLocal();
        boolean localUser = event.getSecurityLevel(FieldIDSession.get().getSessionUser().getSecurityFilter().getOwner()) == SecurityLevel.LOCAL;
        boolean printable = event.isEventCertPrintable();
        boolean isReadOnly = FieldIDSession.get().getSessionUser().isReadOnlyUser();
        boolean hasCreateEvent = FieldIDSession.get().getSessionUser().hasAccess("createevent");
        boolean hasEditEvent = FieldIDSession.get().getSessionUser().hasAccess("editevent");
        boolean hasTag = FieldIDSession.get().getSessionUser().hasAccess("tag");

        WebMarkupContainer completeEventActionsList = createCompleteEventActionsList(event, eventId, localEvent, localUser, printable, hasCreateEvent, hasEditEvent, hasTag);
        WebMarkupContainer incompleteEventActionsList = createIncompleteEventActionsList(event, isReadOnly, hasCreateEvent, hasTag);


        WebMarkupContainer safetyNetworkActionsList;
        safetyNetworkActionsList = createSafetyNetworkActionsList(event, localEvent);

        completeEventActionsList.setVisible(localEvent && event.getEventState() == Event.EventState.COMPLETED);
        incompleteEventActionsList.setVisible(localEvent && event.getEventState() != Event.EventState.COMPLETED);
        safetyNetworkActionsList.setVisible(!localEvent);

        add(completeEventActionsList);
        add(incompleteEventActionsList);
        add(safetyNetworkActionsList);
    }

    private WebMarkupContainer createIncompleteEventActionsList(final Event event, boolean isReadOnly, boolean hasCreateEvent, boolean hasTag) {
        WebMarkupContainer incompleteEventActionsList = new WebMarkupContainer("incompleteEventActionsList");
        incompleteEventActionsList.setOutputMarkupId(true);

        Link resolveEventLink = new Link("closeEventLink") {
            @Override public void onClick() {
                setResponsePage(new CloseEventPage(PageParametersBuilder.uniqueId(event.getId()), (FieldIDFrontEndPage) getPage()));
            }
        };

        NonWicketLink startEventLink = new NonWicketLink("startEventLink", "selectEventAdd.action?scheduleId="+event.getId()+"&type="+event.getType().getId()+"&assetId="+event.getAsset().getId());
        BookmarkablePageLink viewSchedulesLink = new BookmarkablePageLink("viewLink", AssetEventsPage.class, PageParametersBuilder.uniqueId(event.getAsset().getId()));

        NonWicketLink deleteScheduleLink = new NonWicketLink("deleteScheduleLink", "eventScheduleDelete.action?uniqueID="+event.getId() +"&assetId="+event.getAsset().getId());

        BookmarkablePageLink viewAssetLink = new BookmarkablePageLink<Void>("viewAssetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(event.getAsset().getId()));

        NonWicketLink editAssetLink = new NonWicketLink("editAssetLink", "assetEdit.action?uniqueID="+event.getAsset().getId());

        resolveEventLink.setVisible(!isReadOnly && Event.EventState.OPEN.equals(event.getEventState()));
        deleteScheduleLink.setVisible(!isReadOnly);
        startEventLink.setVisible(hasCreateEvent && Event.EventState.OPEN.equals(event.getEventState()));
        editAssetLink.setVisible(hasTag);

        incompleteEventActionsList.add(startEventLink);
        incompleteEventActionsList.add(viewSchedulesLink);

        incompleteEventActionsList.add(deleteScheduleLink);

        incompleteEventActionsList.add(viewAssetLink);
        incompleteEventActionsList.add(editAssetLink);

        incompleteEventActionsList.add(resolveEventLink);

        return incompleteEventActionsList;
    }

    private WebMarkupContainer createCompleteEventActionsList(Event event, Long eventId, boolean localEvent, boolean localEndUser, boolean printable, boolean hasCreateEvent, boolean hasEditEvent, boolean hasTag) {
        WebMarkupContainer completeEventActionsList = new WebMarkupContainer("completeEventActionsList");
        completeEventActionsList.setOutputMarkupId(true);

        NonWicketLink viewLink = new NonWicketLink("viewLink", "event.action?uniqueID="+eventId);

        NonWicketLink editLink = new NonWicketLink("editLink", "selectEventEdit.action?uniqueID="+eventId);
        NonWicketLink printReportLink = new NonWicketLink("printReportLink", "file/downloadEventCert.action?uniqueID="+eventId + "&reportType=INSPECTION_CERT");

        NonWicketLink startEventLink = new NonWicketLink("startEventLink", "quickEvent.action?assetId="+event.getAsset().getId());
        BookmarkablePageLink viewAssetLink = new BookmarkablePageLink<Void>("viewAssetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(event.getAsset().getId()));
        NonWicketLink editAssetLink = new NonWicketLink("editAssetLink", "assetEdit.action?uniqueID="+event.getAsset().getId());

        viewLink.setVisible(localEndUser);
        editLink.setVisible(hasEditEvent);
        printReportLink.setVisible(printable);

        startEventLink.setVisible(hasCreateEvent);
        editAssetLink.setVisible(hasTag);

        completeEventActionsList.add(viewLink);
        completeEventActionsList.add(editLink);
        completeEventActionsList.add(printReportLink);

        completeEventActionsList.add(startEventLink);
        completeEventActionsList.add(viewAssetLink);
        completeEventActionsList.add(editAssetLink);

        return completeEventActionsList;
    }

    private WebMarkupContainer createSafetyNetworkActionsList(Event event, boolean localEvent) {
        WebMarkupContainer safetyNetworkActionsList = new WebMarkupContainer("safetyNetworkActionsList");

        if (localEvent) {
            safetyNetworkActionsList.setVisible(false);
            return safetyNetworkActionsList;
        }

        Asset networkAsset = event.getAsset();

        NonWicketLink viewLink = new NonWicketLink("viewLink", "event.action?uniqueID=" + event.getId());
        viewLink.setVisible(event.getEventState() == Event.EventState.COMPLETED);
        safetyNetworkActionsList.add(viewLink);

        Asset localAsset = assetService.findLocalAssetFor(networkAsset);
        if (localAsset != null) {
            safetyNetworkActionsList.add(new BookmarkablePageLink<Void>("viewAssetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(localAsset.getId())));
        } else {
            safetyNetworkActionsList.add(new WebMarkupContainer("viewAssetLink").setVisible(false));
        }

        return safetyNetworkActionsList;
    }

}
