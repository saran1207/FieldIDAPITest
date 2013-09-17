package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventCreationService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.EventStatusService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.DisableButtonBeforeSubmit;
import com.n4systems.fieldid.wicket.behavior.JavaScriptAlertConfirmBehavior;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.*;
import com.n4systems.fieldid.wicket.components.event.EventFormEditPanel;
import com.n4systems.fieldid.wicket.components.event.attributes.AttributesEditPanel;
import com.n4systems.fieldid.wicket.components.event.book.NewOrExistingEventBook;
import com.n4systems.fieldid.wicket.components.event.prooftest.ProofTestEditPanel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.fileupload.AttachmentsPanel;
import com.n4systems.fieldid.wicket.components.location.LocationPicker;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.components.user.GroupedUserPicker;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import com.n4systems.fieldid.wicket.model.assetstatus.AssetStatusesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.fieldid.wicket.model.user.GroupedVisibleUsersModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.*;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class EventPage extends FieldIDFrontEndPage {

    @SpringBean protected EventService eventService;
    @SpringBean protected EventCreationService eventCreationService;
    @SpringBean protected PersistenceService persistenceService;
    @SpringBean protected EventStatusService eventStatusService;

    protected IModel<Event> event;

    private List<Event> schedules = new ArrayList<Event>();
    private Event scheduleToAdd;
    private List<AbstractEvent.SectionResults> sectionResults;
    protected List<FileAttachment> fileAttachments;
    private User assignedTo;
    protected ProofTestEditPanel proofTestEditPanel;
    private IModel<ProofTestInfo> proofTestInfo;
    private SchedulePicker schedulePicker;

    protected EventResult eventResult;

    private WebMarkupContainer schedulesContainer;

    @Override
    protected void onInitialize() {
        super.onInitialize();
        sectionResults = event.getObject().getSectionResults();
        scheduleToAdd = createNewOpenEvent();
        proofTestInfo = new PropertyModel<ProofTestInfo>(event, "proofTestInfo");
        if (proofTestInfo.getObject() == null) {
            proofTestInfo = new Model<ProofTestInfo>(new ProofTestInfo());
        }
        if(event.getObject().hasAssignToUpdate()) {
            assignedTo = event.getObject().getAssignedTo().getAssignedUser();
        }
        add(schedulePicker = createSchedulePicker());
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new OuterEventForm("outerEventForm"));
    }

    private SchedulePicker createSchedulePicker() {
        return new SchedulePicker("schedulePicker", new PropertyModel<Event>(EventPage.this, "scheduleToAdd"), new EventTypesForAssetTypeModel(new PropertyModel<AssetType>(event, "asset.type")), new EventJobsForTenantModel()) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                schedules.add(scheduleToAdd);
                scheduleToAdd = createNewOpenEvent();
                // CAVEAT : shouldn't use enclosures for ajax component - results in javascript noise if component not visible.
                // see http://jawher.net/2009/09/17/wicket-enclosures-and-ajax-no-no/
                // use InlineEnclosure instead.
                target.add(schedulesContainer);
            }
        };
    }

    private Event createNewOpenEvent() {
        Event openEvent = new Event();
        openEvent.setAsset(event.getObject().getAsset());
        return openEvent;
    }

    class OuterEventForm extends Form {

        private LocationPicker locationPicker;
		private NewOrExistingEventBook newOrExistingEventBook;

        public OuterEventForm(String id) {
            super(id);
            add(new Label("assetTypeName", new PropertyModel<String>(event, "asset.type.name")));

            add(new Label("description", new PropertyModel<String>(event, "asset.description")));
            add(new IdentifierLabel("identifierLabel", new PropertyModel<AssetType>(event, "asset.type")));
            BookmarkablePageLink<Void> assetLink = new BookmarkablePageLink<Void>("assetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(event.getObject().getAsset().getId()));
            assetLink.add(new Label("identifier", new PropertyModel<Object>(event, "asset.identifier")));
            add(assetLink);

            add(new Label("rfidNumber", new PropertyModel<String>(event, "asset.rfidNumber")));
            add(new Label("referenceNumber", new PropertyModel<String>(event, "asset.customerRefNumber")));

            GroupedUserPicker groupedUserPicker;
            add(groupedUserPicker = new GroupedUserPicker("assignedTo", new PropertyModel<User>(EventPage.this, "assignedTo"), new GroupedVisibleUsersModel()));
            groupedUserPicker.setNullValid(true);
            groupedUserPicker.setVisible(event.getObject().getType().isAssignedToAvailable());

            add(new OrgLocationPicker("orgPicker", new PropertyModel<BaseOrg>(event, "owner")) {
                @Override
                protected void onChanged(AjaxRequestTarget target) {
                    doAutoSchedule();
                    target.add(schedulesContainer);
                    BaseOrg selectedOrg = (BaseOrg) getDefaultModel().getObject();
                    locationPicker.setOwner(selectedOrg);
					newOrExistingEventBook.setOwner(selectedOrg);
					target.add(newOrExistingEventBook);
                }
            }.withAutoUpdate());

            schedulesContainer = new WebMarkupContainer("schedulesContainer");
            schedulesContainer.setOutputMarkupPlaceholderTag(true);
            schedulesContainer.setVisible(event.getObject().isNew() || !event.getObject().isCompleted());
            schedulesContainer.add(new ListView<Event>("schedules", new PropertyModel<List<Event>>(EventPage.this, "schedules")) {
                @Override
                protected void populateItem(final ListItem<Event> item) {
                    item.add(new Label("addScheduleDate", new DayDisplayModel(new PropertyModel<Date>(item.getModel(), "dueDate"))));
                    item.add(new Label("addScheduleLabel", new PropertyModel<Object>(item.getModel(), "eventType.name")));
                    item.add(new FlatLabel("addScheduleJob", new PropertyModel<Object>(item.getModel(), "project.name")));
                    item.add(new AjaxLink<Void>("removeLink") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            schedules.remove(item.getIndex());
                            target.add(schedulesContainer);
                        }
                    });
                }
            });
            add(schedulesContainer);


            add(new SimpleAjaxButton("openSchedulePickerButton", new FIDLabelModel("label.add_a_schedule")) {
                { setVisible(event.getObject().isNew() || !event.getObject().isCompleted()); }
                @Override
                public void onClick(AjaxRequestTarget target) {
                    schedulePicker.show(target);
                }
            });

            add(new Label("eventTypeName", new PropertyModel<String>(event, "type.name")));
            
            WebMarkupContainer proofTestContainer = new WebMarkupContainer("proofTestContainer");

            proofTestContainer.add(proofTestEditPanel = new ProofTestEditPanel("proofTest", event.getObject().getType(), proofTestInfo));
            proofTestContainer.setVisible(supportsProofTests());

            add(proofTestContainer);

            PropertyModel<User> performedByModel = new PropertyModel<User>(event, "performedBy");
            DropDownChoice<User> performedBy = new DropDownChoice<User>("performedBy", performedByModel, new ExaminersModel(performedByModel), new ListableChoiceRenderer<User>());
            DateTimePicker datePerformedPicker = new DateTimePicker("datePerformed", new UserToUTCDateModel(new PropertyModel<Date>(event, "date")), true).withNoAllDayCheckbox();
            datePerformedPicker.addToDateField(createUpdateAutoschedulesOnChangeBehavior());


            DateTimePicker dateScheduledPicker = new DateTimePicker("dateScheduled", new PropertyModel<Date>(event, "dueDate"), true).withNoAllDayCheckbox();
			newOrExistingEventBook = new NewOrExistingEventBook("newOrExistingEventBook", new PropertyModel<EventBook>(event, "book"));
            newOrExistingEventBook.setOwner(event.getObject().getOwner());

            AttributesEditPanel attributesEditPanel = new AttributesEditPanel("eventAttributes", event);

            add(newOrExistingEventBook);
            add(attributesEditPanel);
            add(datePerformedPicker);
            add(dateScheduledPicker);
            add(performedBy);

            WebMarkupContainer jobsContainer = new WebMarkupContainer("jobsContainer");
            add(jobsContainer);
            jobsContainer.setVisible(getSessionUser().getOrganization().getPrimaryOrg().getExtendedFeatures().contains(ExtendedFeature.Projects));
            DropDownChoice<Project> jobSelect = new DropDownChoice<Project>("job", new PropertyModel<Project>(event, "project"), new EventJobsForTenantModel(), new ListableChoiceRenderer<Project>());
            jobSelect.setNullValid(true);
            jobsContainer.add(jobSelect);

            add(locationPicker = new LocationPicker("locationPicker", new PropertyModel<Location>(event, "advancedLocation")).withRelativePosition());
            locationPicker.setOwner(new PropertyModel<BaseOrg>(event, "owner").getObject());

            add(new Comment("comments", new PropertyModel<String>(event, "comments")).addMaxLengthValidation(2500));

            DropDownChoice assetStatus = new DropDownChoice<AssetStatus>("assetStatus", new PropertyModel<AssetStatus>(event, "assetStatus"), new AssetStatusesForTenantModel(), new ListableChoiceRenderer<AssetStatus>());
            assetStatus.add(new UpdateComponentOnChange());
            assetStatus.setNullValid(true);
            add(assetStatus);

            if (event.getObject() instanceof Event) {
                Event masterEvent = (Event) event.getObject();
                DropDownChoice resultSelect = new DropDownChoice<EventResult>("eventResult", new PropertyModel<EventResult>(EventPage.this, "eventResult"), EventResult.getValidEventResults(), new ListableLabelChoiceRenderer<EventResult>());
                resultSelect.add(new UpdateComponentOnChange());
                resultSelect.setNullValid(masterEvent.isResultFromCriteriaAvailable());
                add(resultSelect);
            }

            List<EventStatus> eventStatuses = eventStatusService.getActiveStatuses();
            DropDownChoice workflowStateSelect = new DropDownChoice<EventStatus>("eventStatus", new PropertyModel<EventStatus>(event, "eventStatus"), eventStatuses, new ListableLabelChoiceRenderer<EventStatus>());
            workflowStateSelect.add(new UpdateComponentOnChange());
            workflowStateSelect.setNullValid(true);
            add(workflowStateSelect);

            add(new CheckBox("printable", new PropertyModel<Boolean>(event, "printable")).add(new UpdateComponentOnChange()));

            EventForm form = event.getObject().getEventForm();
            add(new EventFormEditPanel("eventFormPanel", new PropertyModel<List<AbstractEvent.SectionResults>>(EventPage.this, "sectionResults")).setVisible(form!=null && form.getAvailableSections().size()>0));
            add(new AttachmentsPanel("attachmentsPanel", new PropertyModel<List<FileAttachment>>(EventPage.this, "fileAttachments")));

            Button saveButton = new Button("saveButton");
            saveButton.add(new DisableButtonBeforeSubmit());
            add(saveButton);
            add(createCancelLink("cancelLink"));
            add(createDeleteLink("deleteLink"));
        }

        @Override
        protected void onSubmit() {
            // SUGGESTION DD : put this in a form validator instead of onSubmit.
            if (event.getObject().getOwner()==null) {
                error(getString("error.event_owner_null"));
                return;
            }

            if (event.getObject().getDate()==null) {
                error(getString("error.event_date_null"));
                return;
            }


            if (persistenceService.findUsingTenantOnlySecurityWithArchived(Asset.class, event.getObject().getAsset().getId()).isArchived()) {
                error(getString("error.asset_has_been_archived"));
                return;
            }
            event.getObject().setSectionResults(sectionResults);
            event.getObject().setProofTestInfo(proofTestInfo.getObject());
            saveAssignedToIfNecessary();
            saveEventBookIfNecessary();
            if (doPostSubmitValidation()) {
                AbstractEvent savedEvent = doSave();
                FieldIDSession.get().storeInfoMessageForStruts(getString("message.eventsaved"));
                throw new RedirectToUrlException("/event.action?uniqueID="+savedEvent.getId());
            }
        }
    }

    protected Behavior createUpdateAutoschedulesOnChangeBehavior() {
        return new UpdateComponentOnChange() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                doAutoSchedule();
                target.add(schedulesContainer);
            }
        };
    }

    private boolean supportsProofTests() {
        Event event = this.event.getObject();
        return event.getType().getSupportedProofTests().size()>0 && event.getOwner().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.ProofTestIntegration);
    }

    protected abstract Component createCancelLink(String cancelLink);

    private Link createDeleteLink(String linkId) {
        return new Link(linkId) {
            {
                add(new JavaScriptAlertConfirmBehavior(new FIDLabelModel("label.confirm_event_delete")));
                setVisible(!event.getObject().isNew());
            }
            @Override
            public void onClick() {
                eventService.retireEvent(event.getObject());
                FieldIDSession.get().info(getString("message.eventdeleted"));
                setResponsePage(AssetSummaryPage.class, PageParametersBuilder.uniqueId(event.getObject().getAsset().getId()));
            }
        };
    }

    protected void saveEventBookIfNecessary() {
        EventBook book = event.getObject().getBook();
        if (book != null && book.getId() == null) {
            book.setTenant(getCurrentUser().getTenant());
            book.setOwner(event.getObject().getOwner());
            persistenceService.save(book);
        }
    }
    
    protected void saveAssignedToIfNecessary() {
        if (assignedTo != null) {
            event.getObject().setAssignedTo(AssignedToUpdate.assignAssetToUser(assignedTo));
        }
    }

    protected abstract AbstractEvent doSave();

    protected List<EventScheduleBundle> createEventScheduleBundles() {
        List<EventScheduleBundle> scheduleBundles = new ArrayList<EventScheduleBundle>();

        for (Event sched : schedules) {
            EventScheduleBundle bundle = new EventScheduleBundle(sched.getAsset(), sched.getEventType(), sched.getProject(), sched.getDueDate());
            scheduleBundles.add(bundle );
        }

        return scheduleBundles;
    }

    private boolean doPostSubmitValidation() {
        if (event instanceof Event) {
            Event masterEvent = (Event) event.getObject();
            if (masterEvent.getBook() != null && masterEvent.getBook().getId() == null && StringUtils.isBlank(masterEvent.getBook().getName())) {
                error(new FIDLabelModel("error.event_book_title_required").getObject());
                return false;
            }

            if (masterEvent.getOwner() == null) {
                error(new FIDLabelModel("error.event_book_title_required").getObject());
                return false;
            }


        }

        if (event.getObject().containsUnfilledScoreCriteria()) {
            error(new FIDLabelModel("error.scores.required").getObject());
            return false;
        }


        return true;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/event/event_base.css");
        response.renderCSSReference("style/newCss/event/event_schedule.css");
    }

    @Override
    protected boolean useLegacyCss() {
        return false;
    }

    protected void doAutoSchedule() {
        Event e = event.getObject();

        if (null == e.getDate()) {
            return;
        }

        AssetTypeSchedule schedule = e.getAsset().getType().getSchedule(e.getType(), e.getOwner());
        schedules.clear();
        if (schedule != null) {
            Event eventSchedule = new Event();
            eventSchedule.setAsset(e.getAsset());
            eventSchedule.setType(e.getType());
            eventSchedule.setDueDate(schedule.getNextDate(e.getDate()));
            schedules.add(eventSchedule);
        }
    }

    public EventResult getEventResult() {
        return eventResult;
    }

    public void setEventResult(EventResult eventResult) {
        this.eventResult = eventResult;
    }
}
