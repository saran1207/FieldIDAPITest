package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.certificate.CertificateService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.event.*;
import com.n4systems.fieldid.wicket.pages.identify.IdentifyOrEditAssetPage;
import com.n4systems.fieldid.wicket.pages.masterevent.EditMasterEventPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunLastReportPage;
import com.n4systems.model.Asset;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.reporting.EventReportType;
import com.n4systems.util.views.RowView;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class EventActionsCell extends Panel {

    private static final Logger logger = Logger.getLogger(EventActionsCell.class);

    @SpringBean
    private AssetService assetService;

    @SpringBean
    private EventService eventService;

    @SpringBean
    private CertificateService certificateService;

    public EventActionsCell(String id, IModel<RowView> rowModel) {
        super(id);

        WebMarkupContainer actionsLink = new WebMarkupContainer("actionsLink");
        actionsLink.setOutputMarkupId(true);
        actionsLink.add(new ContextImage("dropDownArrow", "images/dropdown_arrow.png"));

        add(actionsLink);

        ThingEvent event = (ThingEvent) rowModel.getObject().getEntity();
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

        completeEventActionsList.setVisible(localEvent && event.getWorkflowState() == WorkflowState.COMPLETED);
        incompleteEventActionsList.setVisible(localEvent && event.getWorkflowState() != WorkflowState.COMPLETED);
        safetyNetworkActionsList.setVisible(!localEvent);

        add(completeEventActionsList);
        add(incompleteEventActionsList);
        add(safetyNetworkActionsList);
    }

    private WebMarkupContainer createIncompleteEventActionsList(final ThingEvent event, boolean isReadOnly, boolean hasCreateEvent, boolean hasTag) {
        WebMarkupContainer incompleteEventActionsList = new WebMarkupContainer("incompleteEventActionsList");
        incompleteEventActionsList.setOutputMarkupId(true);

        Link resolveEventLink = new Link("closeEventLink") {
            @Override
            public void onClick() {
                setResponsePage(new CloseEventPage(PageParametersBuilder.uniqueId(event.getId()), (FieldIDFrontEndPage) getPage()));
            }
        };

        PageParameters nextParams = new PageParameters().add("assetId", event.getTarget().getId())
                .add("type", event.getType().getId())
                .add("scheduleId", event.getId());

        BookmarkablePageLink startEventLink = new BookmarkablePageLink<Void>("startEventLink", StartRegularOrMasterEventPage.class, nextParams);


        BookmarkablePageLink viewSchedulesLink = new BookmarkablePageLink("viewLink", AssetEventsPage.class, PageParametersBuilder.uniqueId(event.getAsset().getId()));

        AjaxLink deleteScheduleLink = new AjaxLink<Void>("deleteScheduleLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                try {
                    ThingEvent ev = eventService.lookupExistingEvent(ThingEvent.class, event.getId());
                    eventService.retireEvent(ev);
                    new RunLastReportPage(new FIDLabelModel("message.eventdeleted").getObject(), false);
                } catch (Exception e) {
                    new RunLastReportPage(new FIDLabelModel("error.eventdeleting").getObject(), true);
                }
            }
        };

        BookmarkablePageLink viewAssetLink = new BookmarkablePageLink<Void>("viewAssetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(event.getAsset().getId()));

        BookmarkablePageLink editAssetLink = new BookmarkablePageLink<Void>("editAssetLink", IdentifyOrEditAssetPage.class, PageParametersBuilder.id(event.getAsset().getId()));

        resolveEventLink.setVisible(!isReadOnly && WorkflowState.OPEN.equals(event.getWorkflowState()));
        deleteScheduleLink.setVisible(!isReadOnly);
        startEventLink.setVisible(hasCreateEvent && WorkflowState.OPEN.equals(event.getWorkflowState()));
        editAssetLink.setVisible(hasTag);

        incompleteEventActionsList.add(startEventLink);
        incompleteEventActionsList.add(viewSchedulesLink);

        incompleteEventActionsList.add(deleteScheduleLink);

        incompleteEventActionsList.add(viewAssetLink);
        incompleteEventActionsList.add(editAssetLink);

        incompleteEventActionsList.add(resolveEventLink);

        return incompleteEventActionsList;
    }

    private WebMarkupContainer createCompleteEventActionsList(ThingEvent event, Long eventId, boolean localEvent, boolean localEndUser, boolean printable, boolean hasCreateEvent, boolean hasEditEvent, boolean hasTag) {
        WebMarkupContainer completeEventActionsList = new WebMarkupContainer("completeEventActionsList");
        completeEventActionsList.setOutputMarkupId(true);

        BookmarkablePageLink viewLink = new BookmarkablePageLink<ThingEventSummaryPage>("viewLink", ThingEventSummaryPage.class, PageParametersBuilder.id(event.getID()));

        BookmarkablePageLink editLink;

        if (!event.getType().isActionEventType() && event.getThingType().isMaster()) {
            add(editLink = new BookmarkablePageLink<EditEventPage>("editLink", EditMasterEventPage.class, PageParametersBuilder.uniqueId(event.getId())));
        } else {
            add(editLink = new BookmarkablePageLink<EditEventPage>("editLink", EditEventPage.class, PageParametersBuilder.uniqueId(event.getId())));
        }

        Link printReportLink = new Link("printReportLink") {
            @Override
            public void onClick() {
                File report = null;
                try {
                    report = generateCert(eventId);

                    //Call back to the base page that implements this.  We'll actually universally handle this, in the
                    //off change that we're not calling back to the right page...
                    if(this.getPage() instanceof FieldIDFrontEndPage) {
                        ((FieldIDFrontEndPage) this.getPage()).handleDownload(report, createReportName(event));
                    } else {
                        ((FieldIDTemplatePage) this.getPage()).handleDownload(report, createReportName(event));
                    }
                } catch (IOException | ReportException e) {
                    if(report != null) Files.remove(report);
                    logger.error("There was an error when generating a Certificate for Event with ID " + eventId, e);
                    getPage().error("Error!  Certificate could not be generated!!");
                }
            }
        };

        printReportLink.setOutputMarkupId(true);

        BookmarkablePageLink startEventLink = new BookmarkablePageLink("startEventLink", QuickEventPage.class, PageParametersBuilder.id(event.getAsset().getId()));
        BookmarkablePageLink viewAssetLink = new BookmarkablePageLink<Void>("viewAssetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(event.getAsset().getId()));
        BookmarkablePageLink editAssetLink = new BookmarkablePageLink<Void>("editAssetLink", IdentifyOrEditAssetPage.class, PageParametersBuilder.id(event.getAsset().getId()));

        viewLink.setVisible(localEndUser);
        editLink.setVisible(hasEditEvent);
        printReportLink.setVisible(printable && !event.getType().isActionEventType());

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

    private String createReportName(ThingEvent event) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        return EventReportType.INSPECTION_CERT.getReportNamePrefix() + "-" + dateFormatter.format(event.getDate()) + ".pdf";
    }

    private File generateCert(Long eventId) throws IOException, ReportException {
        byte[] contents = certificateService.generateEventCertificatePdf(EventReportType.INSPECTION_CERT, eventId);

        File tempFile = File.createTempFile("temp-file", ".tmp");

        FileOutputStream fos = new FileOutputStream(tempFile);

        fos.write(contents);
        fos.close();

        return tempFile;
    }

    private WebMarkupContainer createSafetyNetworkActionsList(ThingEvent event, boolean localEvent) {
        WebMarkupContainer safetyNetworkActionsList = new WebMarkupContainer("safetyNetworkActionsList");

        if (localEvent) {
            safetyNetworkActionsList.setVisible(false);
            return safetyNetworkActionsList;
        }

        Asset networkAsset = event.getAsset();

        NonWicketLink viewLink = new NonWicketLink("viewLink", "event.action?uniqueID=" + event.getId());
        viewLink.setVisible(event.getWorkflowState() == WorkflowState.COMPLETED);
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
