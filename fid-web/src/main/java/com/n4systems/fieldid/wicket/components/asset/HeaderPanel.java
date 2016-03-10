package com.n4systems.fieldid.wicket.components.asset;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketIframeLink;
import com.n4systems.fieldid.wicket.components.schedule.ProcedurePicker;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.LocalizeAround;
import com.n4systems.fieldid.wicket.model.LocalizeModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.OopsPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.event.QuickEventPage;
import com.n4systems.fieldid.wicket.pages.identify.IdentifyOrEditAssetPage;
import com.n4systems.fieldid.wicket.pages.identify.LimitedEditAsset;
import com.n4systems.fieldid.wicket.pages.loto.ProcedureResultsPage;
import com.n4systems.fieldid.wicket.pages.loto.ProceduresListPage;
import com.n4systems.model.*;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.user.UserGroup;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Set;
import java.util.concurrent.Callable;

public class HeaderPanel extends Panel {

    @SpringBean protected UserService userService;
    @SpringBean protected AssetService assetService;
    @SpringBean private EventScheduleService eventScheduleService;
    @SpringBean private EventTypeService eventTypeService;
    @SpringBean private ProcedureService procedureService;
    @SpringBean private ProcedureDefinitionService procedureDefinitionService;

    private Boolean useContext;
    private ThingEvent scheduleToAdd;
    private Procedure procedureToSchedule;

    public  HeaderPanel(String id, IModel<Asset> assetModel, Boolean isView, Boolean useContext) {
        super(id, assetModel);
        this.useContext = useContext;

        final Asset asset = assetModel.getObject();

        add(new Label("assetType", asset.getType().getName()));
        add(new Label("assetIdentifier", asset.getIdentifier()));
        if(asset.getAssetStatus() != null) {
            add(new Label("assetStatus", asset.getAssetStatus().getDisplayName()));
        } else {
            Label assetStatus;
            add(assetStatus = new Label("assetStatus"));
            assetStatus.setVisible(false);
        }

        BaseOrg owner = asset.getOwner();

        add(new Label("ownerInfo", getOwnerLabel(owner, asset.getAdvancedLocation())));

        BookmarkablePageLink summaryLink;
        BookmarkablePageLink eventHistoryLink;
        NonWicketIframeLink traceabilityLink;
        boolean hasProcedures = FieldIDSession.get().getSecurityGuard().isLotoEnabled()
                                    && asset.getType().hasProcedures();

        add(summaryLink = new BookmarkablePageLink<Void>("summaryLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(asset.getId())));

        add(traceabilityLink = new NonWicketIframeLink("traceabilityLink", "aHtml/iframe/assetTraceability.action?uniqueID=" + asset.getId() + "&useContext=false", false, 1000, 600, new AttributeModifier("class", "mattButtonMiddle")));
        traceabilityLink.setVisible(assetService.hasLinkedAssets(asset) || isInVendorContext());

        eventHistoryLink = new BookmarkablePageLink<Void>("eventHistoryLink", AssetEventsPage.class, PageParametersBuilder.uniqueId(asset.getId()));
        eventHistoryLink.setVisible(FieldIDSession.get().getSecurityGuard().isInspectionsEnabled());

        add(eventHistoryLink);

        add(new BookmarkablePageLink<ProceduresListPage>("lotoProceduresLink", ProceduresListPage.class, PageParametersBuilder.uniqueId(asset.getId()))
                .setVisible(hasProcedures));

        if (isView) {
            summaryLink.add(new AttributeAppender("class", "mattButtonPressed").setSeparator(" "));
        } else {
           eventHistoryLink.add(new AttributeAppender("class", "mattButtonPressed").setSeparator(" "));
        }



        if (hasProcedures) {
            eventHistoryLink.add(new AttributeAppender("class", "mattButtonMiddle").setSeparator(" "));
        } else {
            eventHistoryLink.add(new AttributeAppender("class", "mattButtonRight").setSeparator(" "));
        }

        //Its strange that we are using the EditEvent permission to display this link
        if (FieldIDSession.get().getUserSecurityGuard().isAllowedEditEvent() && !FieldIDSession.get().getSessionUser().isReadOnlyUser())
            add(new BookmarkablePageLink<Void>("editAssetLink", IdentifyOrEditAssetPage.class, PageParametersBuilder.id(asset.getId())));
        else
            add(new BookmarkablePageLink<Void>("editAssetLink", LimitedEditAsset.class, PageParametersBuilder.id(asset.getId())) {
                @Override
                public boolean isVisible() {
                    return FieldIDSession.get().getUserSecurityGuard().isAllowedEditAssetDetails();
                }
            });
        // Necessary for localization stuff to not break on this page.
        final IModel<Set<EventType>> assocEventTypesModel = new LocalizeModel<Set<EventType>>(new PropertyModel<Set<EventType>>(asset.getType(), "associatedEventTypes"));
        boolean hasAssociatedEventTypes = new LocalizeAround<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !assocEventTypesModel.getObject().isEmpty();
            }
        }).call();
        boolean isLotoEnabled = FieldIDSession.get().getSecurityGuard().isLotoEnabled();
        boolean hasCreateEvent = FieldIDSession.get().getUserSecurityGuard().isAllowedCreateEvent();
        boolean hasMaintainLotoSchedule = FieldIDSession.get().getUserSecurityGuard().isAllowedMaintainLotoSchedule();

        BookmarkablePageLink startEventLink;
        add(startEventLink = new BookmarkablePageLink<Void>("startEventLink", QuickEventPage.class, PageParametersBuilder.id(asset.getId())));
        startEventLink.setVisible(hasCreateEvent &&
                                  hasAssociatedEventTypes &&
                                  FieldIDSession.get().getSecurityGuard().isInspectionsEnabled());

        scheduleToAdd = createNewSchedule(asset);

        final SchedulePicker schedulePicker = new SchedulePicker<ThingEvent>("schedulePicker", new PropertyModel<ThingEvent>(HeaderPanel.this, "scheduleToAdd"), new EventTypesForAssetTypeModel(new PropertyModel<AssetType>(asset, "type")), new EventJobsForTenantModel()) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                scheduleToAdd.setTenant(FieldIDSession.get().getSessionUser().getTenant());
                eventScheduleService.createSchedule(scheduleToAdd);
                refreshContentPanel(target);
                scheduleToAdd = createNewSchedule(asset);
            }
        };

        procedureToSchedule = createNewProcedure(asset);

        final ProcedurePicker procedurePicker = new ProcedurePicker("procedurePicker", new PropertyModel<Procedure>(HeaderPanel.this, "procedureToSchedule")) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                procedureService.createSchedule(procedureToSchedule);
                refreshContentPanel(target);
                procedureToSchedule = createNewProcedure(asset);
            }
        };

        boolean showScheduleEventLink;
        boolean showScheduleProcedureLink;

        if (isLotoEnabled &&  hasMaintainLotoSchedule) {
            showScheduleEventLink = hasAssociatedEventTypes && !asset.getType().hasProcedures();
            showScheduleProcedureLink = !hasAssociatedEventTypes && asset.getType().hasProcedures();
        } else {
            showScheduleEventLink = hasCreateEvent && hasAssociatedEventTypes;
            showScheduleProcedureLink = false;
        }
        add(new AjaxLink("scheduleEventLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                    schedulePicker.show(target);
            }
        }.setVisible(showScheduleEventLink));

        add(new AjaxLink("scheduleProcedureLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                    procedurePicker.show(target);
            }
        }.setVisible(showScheduleProcedureLink));

        WebMarkupContainer scheduleMenu = new WebMarkupContainer("scheduleMenu");

        scheduleMenu.add(new AjaxLink("menuScheduleEventLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                schedulePicker.show(target);
            }
        });

        scheduleMenu.add(new AjaxLink("menuScheduleProcedureLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                procedurePicker.show(target);
            }
        });

        scheduleMenu.setVisible(hasCreateEvent &&
                                isLotoEnabled && //We may no longer need this, since we're determining if LOTO is enabled differently.
                                hasAssociatedEventTypes &&
                                asset.getType().hasProcedures() &&
                                hasMaintainLotoSchedule);

        add(scheduleMenu);

        add(schedulePicker);
        add(procedurePicker);

        add(createWorkflowStateLabel("workflowState", asset));
    }


    private WebMarkupContainer createWorkflowStateLabel(String id, Asset asset) {

        Procedure procedureModel = procedureService.getLockedProcedure(asset);

        WebMarkupContainer labelAndLinkContainer = new WebMarkupContainer("labelAndLinkContainer");
        Label workflowStateLabel;
        BookmarkablePageLink resultsPageBookmarkablePageLink;

        //If the asset is not locked out
        if(procedureModel == null) {
            workflowStateLabel = new Label(id, new FIDLabelModel(""));
            resultsPageBookmarkablePageLink = new BookmarkablePageLink("viewLink", OopsPage.class);

            workflowStateLabel.setVisible(false);
            resultsPageBookmarkablePageLink.setVisible(false);
        } else {
            workflowStateLabel = new Label(id, new FIDLabelModel("CURRENTLY LOCKED OUT BY " + procedureModel.getLockedBy().getDisplayName().toUpperCase()));
            resultsPageBookmarkablePageLink = new BookmarkablePageLink<ProcedureResultsPage>("viewLink", ProcedureResultsPage.class, PageParametersBuilder.id(procedureModel.getId()));

            workflowStateLabel.setVisible(procedureModel.getWorkflowState().equals(ProcedureWorkflowState.LOCKED));
            resultsPageBookmarkablePageLink.setVisible(procedureModel.getWorkflowState().equals(ProcedureWorkflowState.LOCKED));

            labelAndLinkContainer.add(new AttributeAppender("class", "actionButtons "));
            labelAndLinkContainer.add(new AttributeAppender("class", "workflowState "));
            labelAndLinkContainer.add(new AttributeAppender("class", Model.of(procedureModel.getWorkflowState().name()), " "));

            resultsPageBookmarkablePageLink.add(new AttributeAppender("class", "mattButton "));
            resultsPageBookmarkablePageLink.add(new AttributeAppender("class", "blueButton "));
            resultsPageBookmarkablePageLink.add(new AttributeAppender("class", "workflowButton "));
        }

        labelAndLinkContainer.add(workflowStateLabel);
        labelAndLinkContainer.add(resultsPageBookmarkablePageLink);

        return labelAndLinkContainer;
    }

    private Procedure createNewProcedure(Asset asset) {
        Procedure procedure = new Procedure();
        procedure.setAsset(asset);
        procedure.setTenant(FieldIDSession.get().getTenant());
        procedure.setWorkflowState(ProcedureWorkflowState.OPEN);
        return procedure;
    }

    private ThingEvent createNewSchedule(Asset asset) {
        ThingEvent schedule = new ThingEvent();
        schedule.setEventResult(EventResult.VOID);
        schedule.setAsset(asset);
        Set<UserGroup> groups = userService.getUser(FieldIDSession.get().getSessionUser().getId()).getGroups();
        if (!groups.isEmpty()) {
            schedule.setAssignedUserOrGroup(groups.iterator().next());
        }
        return schedule;
    }

    protected void refreshContentPanel(AjaxRequestTarget target) {};

    private String getOwnerLabel(BaseOrg owner, Location advancedLocation) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(owner.getHierarchicalDisplayName());

        if(advancedLocation != null && !advancedLocation.getFullName().isEmpty()) {
            stringBuilder.append(", ").append(advancedLocation.getFullName());
        }
        return stringBuilder.toString();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/newCss/asset/header.css");
        response.renderCSSReference("style/legacy/pageStyles/procedureResults.css");
    }

    public boolean isInVendorContext() {
        return (FieldIDSession.get().getVendorContext() != null && useContext );
    }

}
