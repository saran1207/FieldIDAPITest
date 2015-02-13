package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
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
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.fieldid.wicket.model.user.GroupedVisibleUsersModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.*;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public abstract class EventPage<T extends Event> extends FieldIDFrontEndPage {

    @SpringBean
    protected EventService eventService;
    @SpringBean
    protected PersistenceService persistenceService;
    @SpringBean
    protected EventStatusService eventStatusService;
    @SpringBean
    protected S3Service s3Service;

    protected IModel<T> event;

    protected List<T> schedules = new ArrayList<T>();
    private T scheduleToAdd;
    private List<AbstractEvent.SectionResults> sectionResults;
    protected List<FileAttachment> fileAttachments;
    private User assignedTo;
    protected ProofTestEditPanel proofTestEditPanel;
    private SchedulePicker schedulePicker;

    protected EventResult eventResult;
    protected WebMarkupContainer ownerSection;
    protected WebMarkupContainer jobsContainer;
    protected WebMarkupContainer eventBookContainer;

    protected WebMarkupContainer schedulesContainer;

    private Boolean assetOwnerUpdate;

    private String latVal = "";
    private String longVal = "";
    boolean hasDefaultVal = false;

    @Override
    protected void onInitialize() {
        super.onInitialize();
        sectionResults = event.getObject().getSectionResults();
        scheduleToAdd = createNewOpenEvent();
        if(event.getObject().hasAssignToUpdate()) {
            assignedTo = event.getObject().getAssignedTo().getAssignedUser();
        }
        add(schedulePicker = createSchedulePicker());
        add(new FIDFeedbackPanel("feedbackPanel"));

        if (event.getObject().getType().isThingEventType()) {
            Asset asset =  ((ThingEvent)event.getObject()).getAsset();

            NumberFormat numberFormat = new DecimalFormat();
            numberFormat.setMaximumFractionDigits(6);
            numberFormat.setMinimumFractionDigits(0);

            if (null != asset && null != asset.getGpsLocation()) {

                hasDefaultVal = true;

                if (null != asset.getGpsLocation().getLongitude()) {
                    longVal = numberFormat.format(asset.getGpsLocation().getLongitude());
                }

                if (null != asset.getGpsLocation().getLatitude()) {
                    latVal =  numberFormat.format(asset.getGpsLocation().getLatitude());
                }

            }
        }
        OuterEventForm form;
        add(form = new OuterEventForm("outerEventForm"){

            private static final long serialVersionUID = 1L;

            @Override
            protected void onValidate() {
                super.onValidate();

                BigDecimal latField = (BigDecimal)latitude.getConvertedInput();
                BigDecimal longField = (BigDecimal)longitude.getConvertedInput();

                //Check for validity of the longitude and latitude values
                //Longitude: -180 to +180
                if (null != latField) {
                    if (null == longField) {
                        error(new FIDLabelModel("error.longitude").getObject());
                    }else if(longField.compareTo(BigDecimal.valueOf(180)) == 1 || longField.compareTo(BigDecimal.valueOf(-180)) == -1) {
                        error(new FIDLabelModel("error.longitude_value").getObject());
                    }
                }
                //Latitude: -85 to +85
                if (null != longField) {
                    if (null == latField) {
                        error(new FIDLabelModel("error.latitude").getObject());
                    } else if(latField.compareTo(BigDecimal.valueOf(85)) == 1 || latField.compareTo(BigDecimal.valueOf(-85)) == -1) {
                        error(new FIDLabelModel("error.latitude_value").getObject());
                    }
                }
            }
        });

        if (event.getObject().isAction()) {
            form.add(new AttributeAppender("class", "event-form-column-left"));
        }

        WebMarkupContainer actionsColumn;
        add(actionsColumn = new WebMarkupContainer("actionsColumn"));
        actionsColumn.setVisible(event.getObject().isAction());

        actionsColumn.add(new Label("priority", new PropertyModel<String>(event, "priority.name")));
        actionsColumn.add(new Label("notes", new PropertyModel<String>(event, "notes")));

        if (event.getObject().getDueDate() != null) {
            actionsColumn.add(new TimeAgoLabel("dueDate", new PropertyModel<Date>(event, "dueDate"),getCurrentUser().getTimeZone()));
        } else {
            actionsColumn.add(new Label("dueDate"));
        }
        actionsColumn.add(new Label("assignee", new PropertyModel<String>(event, "assignedUserOrGroup.assignToDisplayName")));

        actionsColumn.add(new ListView<CriteriaResultImage>("criteriaResultImage", new PropertyModel<List<? extends CriteriaResultImage>>(event, "sourceCriteriaResult.criteriaImages")) {

            @Override
            protected void populateItem(ListItem<CriteriaResultImage> item) {
                CriteriaResultImage image = item.getModelObject();
                item.add(new ExternalImage("image", s3Service.getCriteriaResultImageThumbnailURL(image).toString()));
            }
        });


    }

    protected abstract SchedulePicker<T> createSchedulePicker();

    protected void onSchedulePickComplete(AjaxRequestTarget target) {
        schedules.add(scheduleToAdd);
        scheduleToAdd = createNewOpenEvent();
        target.add(schedulesContainer);
    }

    protected abstract T createNewOpenEvent();

    public Boolean getAssetOwnerUpdate() {

        if (null == assetOwnerUpdate) {
            return false;
        }

        return assetOwnerUpdate;
    }

    public void setAssetOwnerUpdate(Boolean assetOwnerUpdate) {
        this.assetOwnerUpdate = assetOwnerUpdate;
    }

    class OuterEventForm extends Form {

        private LocationPicker locationPicker;
		private NewOrExistingEventBook newOrExistingEventBook;
        GpsTextField<BigDecimal> latitude;
        GpsTextField<BigDecimal> longitude;



        public OuterEventForm(String id) {
            super(id);

            add(createTargetDetailsPanel(event));

            ownerSection = new WebMarkupContainer("ownerSection");
            ownerSection.setVisible(isJobsContainerVisible());
            add(ownerSection);

            GroupedUserPicker groupedUserPicker;
            ownerSection.add(groupedUserPicker = new GroupedUserPicker("assignedTo", new PropertyModel<User>(EventPage.this, "assignedTo"), new GroupedVisibleUsersModel()));
            groupedUserPicker.setNullValid(true);
            groupedUserPicker.setVisible(event.getObject().getType().isAssignedToAvailable());

            ownerSection.add(new OrgLocationPicker("orgPicker", new PropertyModel<BaseOrg>(event, "owner")) {
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

            // checkbox
            ownerSection.add(new CheckBox("assetOwnerUpdate", new PropertyModel<Boolean>(EventPage.this, "assetOwnerUpdate")).add(new UpdateComponentOnChange()));

            schedulesContainer = new WebMarkupContainer("schedulesContainer");
            schedulesContainer.setOutputMarkupPlaceholderTag(true);
            schedulesContainer.setVisible(isScheduleVisible());
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


            schedulesContainer.add(new AjaxLink("openSchedulePickerLink") {
                { setVisible(event.getObject().isNew() || !event.getObject().isCompleted()); }
                @Override
                public void onClick(AjaxRequestTarget target) {
                    schedulePicker.show(target);
                }
            });

            add(new Label("eventTypeName", new PropertyModel<String>(event, "type.name")));

            WebMarkupContainer proofTestContainer = new WebMarkupContainer("proofTestContainer");

            if (event.getObject().getType().isThingEventType()) {
                proofTestContainer.add(proofTestEditPanel = createProofTestEditPanel("proofTest"));
                proofTestContainer.setVisible(supportsProofTests());
            } else {
                proofTestContainer.setVisible(false);
            }

            add(proofTestContainer);

            PropertyModel<User> performedByModel = new PropertyModel<User>(event, "performedBy");
            DropDownChoice<User> performedBy = new DropDownChoice<User>("performedBy", performedByModel, new ExaminersModel(performedByModel), new ListableChoiceRenderer<User>());
            DateTimePicker datePerformedPicker = new DateTimePicker("datePerformed", new UserToUTCDateModel(new PropertyModel<Date>(event, "date")), true).withNoAllDayCheckbox();
            datePerformedPicker.addToDateField(createUpdateAutoschedulesOnChangeBehavior());


            DateTimePicker dateScheduledPicker = new DateTimePicker("dateScheduled", new PropertyModel<Date>(event, "dueDate"), true).withNoAllDayCheckbox();

            eventBookContainer = new WebMarkupContainer("eventBookContainer");
            eventBookContainer.setVisible(isEventBookVisible());
			newOrExistingEventBook = new NewOrExistingEventBook("newOrExistingEventBook", new PropertyModel<EventBook>(event, "book"));
            newOrExistingEventBook.setOwner(event.getObject().getOwner());

            AttributesEditPanel attributesEditPanel = new AttributesEditPanel("eventAttributes", event);

            eventBookContainer.add(newOrExistingEventBook);
            add(eventBookContainer);
            add(attributesEditPanel);
            add(datePerformedPicker);
            add(dateScheduledPicker);
            add(performedBy);

            jobsContainer = new WebMarkupContainer("jobsContainer");
            add(jobsContainer);
            jobsContainer.setVisible(getSessionUser().getOrganization().getPrimaryOrg().getExtendedFeatures().contains(ExtendedFeature.Projects));
            jobsContainer.setVisible(isJobsContainerVisible());
            DropDownChoice<Project> jobSelect = new DropDownChoice<Project>("job", new PropertyModel<Project>(event, "project"), new EventJobsForTenantModel(), new ListableChoiceRenderer<Project>());
            jobSelect.setNullValid(true);
            jobsContainer.add(jobSelect);

            ownerSection.add(locationPicker = new LocationPicker("locationPicker", new PropertyModel<Location>(event, "advancedLocation")).withRelativePosition());
            locationPicker.setOwner(new PropertyModel<BaseOrg>(event, "owner").getObject());

            add(new Comment("comments", new PropertyModel<String>(event, "comments")).addMaxLengthValidation(2500));

            add(createPostEventPanel(event));

            Event masterEvent = event.getObject();
            DropDownChoice resultSelect = new DropDownChoice<EventResult>("eventResult", new PropertyModel<EventResult>(EventPage.this, "eventResult"), EventResult.getValidEventResults(), new ListableLabelChoiceRenderer<EventResult>());
            resultSelect.add(new UpdateComponentOnChange());
            resultSelect.setNullValid(masterEvent.isResultFromCriteriaAvailable());
            add(resultSelect);

            List<EventStatus> eventStatuses = eventStatusService.getActiveStatuses();
            DropDownChoice workflowStateSelect = new DropDownChoice<EventStatus>("eventStatus", new PropertyModel<EventStatus>(event, "eventStatus"), eventStatuses, new ListableLabelChoiceRenderer<EventStatus>());
            workflowStateSelect.add(new UpdateComponentOnChange());
            workflowStateSelect.setNullValid(true);
            add(workflowStateSelect);

            add(new CheckBox("printable", new PropertyModel<Boolean>(event, "printable")).add(new UpdateComponentOnChange()));

            EventForm form = event.getObject().getEventForm();
            add(new EventFormEditPanel("eventFormPanel", event, new PropertyModel<List<AbstractEvent.SectionResults>>(EventPage.this, "sectionResults"), isActionButtonsVisible()).setVisible(form!=null && form.getAvailableSections().size()>0));
            add(new AttachmentsPanel("attachmentsPanel", new PropertyModel<List<FileAttachment>>(EventPage.this, "fileAttachments")));

            WebMarkupContainer gpsContainer = new WebMarkupContainer("gpsContainer");

            add(gpsContainer);

            latitude = new GpsTextField<BigDecimal>("latitude", ProxyModel.of(event, on(Event.class).getGpsLocation().getLatitude()));
            longitude = new GpsTextField<BigDecimal>("longitude", ProxyModel.of(event, on(Event.class).getGpsLocation().getLongitude()));

            if (event.getObject().getEventType().isThingEventType() && hasDefaultVal) {
                latitude.add(new SimpleAttributeModifier("value", (null == latVal)? "" : latVal ));
                longitude.add(new SimpleAttributeModifier("value", (null == longVal) ? "" : longVal));
            }


            gpsContainer.add(latitude);
            gpsContainer.add(longitude);

            add(gpsContainer);

            gpsContainer.setOutputMarkupId(true);
            gpsContainer.setVisible(event.getObject().getGpsLocation() !=null);
            gpsContainer.setVisible(FieldIDSession.get().getTenant().getSettings().isGpsCapture());

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


            if (targetAlreadyArchived(event.getObject())) {
                error(getString("error.asset_has_been_archived"));
                return;
            }

            boolean flag = getAssetOwnerUpdate();

            event.getObject().setSectionResults(sectionResults);
            onPreSave(event.getObject());
            onPreSave(event.getObject());
            saveAssignedToIfNecessary();
            saveEventBookIfNecessary();
            if (doPostSubmitValidation()) {
                AbstractEvent savedEvent = doSave();
                FieldIDSession.get().info(new FIDLabelModel("message.eventsaved").getObject());
                if(savedEvent.getType().isActionEventType()) {
                    if( ((Event)savedEvent).getTriggerEvent().getType().isThingEventType() )
                        setResponsePage(ThingEventSummaryPage.class, PageParametersBuilder.id(savedEvent.getId()));
                    else
                        setResponsePage(PlaceEventSummaryPage.class, PageParametersBuilder.id(savedEvent.getId()));
                } else if (savedEvent.getType().isPlaceEventType()) {
                    setResponsePage(PlaceEventSummaryPage.class, PageParametersBuilder.id(savedEvent.getId()));
                } else if (savedEvent.getType().isProcedureAuditEventType()) {
                    setResponsePage(ProcedureAuditEventSummaryPage.class, PageParametersBuilder.id(savedEvent.getId()));
                } else {
                    setResponsePage(ThingEventSummaryPage.class, PageParametersBuilder.id(savedEvent.getId()));
                }
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

    protected abstract Component createTargetDetailsPanel(IModel<T> model);
    protected abstract Component createPostEventPanel(IModel<T> event);
    protected abstract boolean supportsProofTests();
    protected abstract ProofTestEditPanel createProofTestEditPanel(String id);
    protected abstract Component createCancelLink(String id);
    protected abstract boolean targetAlreadyArchived(T event);

    protected Link createDeleteLink(String linkId) {
        return new Link(linkId) {
            {
                add(new JavaScriptAlertConfirmBehavior(new FIDLabelModel("label.confirm_event_delete")));
                if (event.getObject().isNew()) {
                    setVisible(false);
                } else if (event.getObject().getWorkflowState().equals(WorkflowState.OPEN)) {
                    setVisible(false);
                } else {
                    setVisible(true);
                }

            }
            @Override
            public void onClick() {
                retireEvent(event.getObject());
                FieldIDSession.get().info(getString("message.eventdeleted"));
                gotoSummaryPage(event.getObject());
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
        if (assignedTo != null || event.getObject().hasAssignToUpdate()) {
            event.getObject().setAssignedTo(AssignedToUpdate.assignAssetToUser(assignedTo));
        }
    }

    protected abstract AbstractEvent doSave();
    protected abstract void retireEvent(T event);
    protected abstract void gotoSummaryPage(T event);
    protected void onPreSave(T event) { }

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
        response.renderCSSReference("style/legacy/newCss/event/event_base.css");
        response.renderCSSReference("style/legacy/newCss/event/event_schedule.css");
        response.renderCSSReference("style/legacy/newCss/component/buttons.css");
    }

    @Override
    protected boolean useLegacyCss() {
        return false;
    }

    protected void doAutoSchedule() { }

    public EventResult getEventResult() {
        return eventResult;
    }

    public void setEventResult(EventResult eventResult) {
        this.eventResult = eventResult;
    }

    protected boolean isScheduleVisible() {
        return (event.getObject().isNew() || !event.getObject().isCompleted());
    }


    public boolean isEventBookVisible() {
        return true;
    }

    public boolean isJobsContainerVisible() {
        return true;
    }

    public boolean isOwnerSectionVisible() {
        return true;
    }

    public boolean isActionButtonsVisible() { return true; }

}
