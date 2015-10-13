package com.n4systems.fieldid.wicket.pages.massevent;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.EventStatusService;
import com.n4systems.fieldid.wicket.behavior.ConfirmNavigationBehavior;
import com.n4systems.fieldid.wicket.behavior.DisableButtonBeforeSubmit;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.behavior.validation.DisableNavigationConfirmationBehavior;
import com.n4systems.fieldid.wicket.behavior.validation.RequiredCriteriaValidator;
import com.n4systems.fieldid.wicket.components.*;
import com.n4systems.fieldid.wicket.components.event.CriteriaSectionEditPanel;
import com.n4systems.fieldid.wicket.components.event.EventFormEditPanel;
import com.n4systems.fieldid.wicket.components.event.book.NewOrExistingEventBook;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.components.user.GroupedUserPicker;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.fieldid.wicket.model.user.GroupedVisibleUsersModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.*;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiEventPage<T extends Event> extends FieldIDTemplatePage {

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
    private SchedulePicker schedulePicker;

    protected EventResult eventResult;
    protected WebMarkupContainer ownerSection;

    protected WebMarkupContainer schedulesContainer;

    private Boolean assetOwnerUpdate = true;
    private Boolean locationUpdate = true;
    private Boolean autoSchedule;

    private MassEventOrigin massEventOrigin;

    private OrgLocationPicker ownerPicker;
    private OrgLocationPicker locationPicker;
    private NewOrExistingEventBook newOrExistingEventBook;

    protected enum MassEventOrigin { START_EVENT, SEARCH, REPORTING };

    @SuppressWarnings("unchecked")
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

        OuterEventForm form;
        add(form = new OuterEventForm("outerEventForm"){

            private static final long serialVersionUID = 1L;

            @Override
            protected void onValidate() {
                super.onValidate();
            }
        });

        if (event.getObject().isAction()) {
            form.add(new AttributeAppender("class", "event-form-column-left"));
        }

        WebMarkupContainer actionsColumn;
        form.add(actionsColumn = new WebMarkupContainer("actionsColumn"));
        actionsColumn.setVisible(event.getObject().getType().isActionEventType());

        actionsColumn.add(new Label("priority", new PropertyModel<String>(event, "priority.name")));
        actionsColumn.add(new Label("notes", new PropertyModel<String>(event, "notes")));

        if (event.getObject().getDueDate() != null) {
            actionsColumn.add(new TimeAgoLabel("dueDate", new PropertyModel<>(event, "dueDate"),getCurrentUser().getTimeZone()));
        } else {
            actionsColumn.add(new Label("dueDate"));
        }
        actionsColumn.add(new Label("assignee", new PropertyModel<String>(event, "assignedUserOrGroup.assignToDisplayName")));

        actionsColumn.add(new ListView<CriteriaResultImage>("criteriaResultImage", new PropertyModel<>(event, "sourceCriteriaResult.criteriaImages")) {

            @Override
            protected void populateItem(ListItem<CriteriaResultImage> item) {
                CriteriaResultImage image = item.getModelObject();
                item.add(new ExternalImage("image", s3Service.getCriteriaResultImageThumbnailURL(image).toString()));
            }
        });

        add(new ConfirmNavigationBehavior(new FIDLabelModel("message.confirm_navigation")));
    }

    protected abstract SchedulePicker<T> createSchedulePicker();

    protected void onSchedulePickComplete(AjaxRequestTarget target) {
        schedules.add(scheduleToAdd);
        scheduleToAdd = createNewOpenEvent();
        // CAVEAT : shouldn't use enclosures for ajax component - results in javascript noise if component not visible.
        // see http://jawher.net/2009/09/17/wicket-enclosures-and-ajax-no-no/
        // use InlineEnclosure instead.
        target.add(schedulesContainer);
    }

    protected abstract T createNewOpenEvent();

    public void setAssetOwnerUpdate(Boolean assetOwnerUpdate) {
        this.assetOwnerUpdate = assetOwnerUpdate;
    }

    public Boolean getAssetOwnerUpdate() {

        if (null == assetOwnerUpdate) {
            return false;
        }

        return assetOwnerUpdate;
    }

    public void setLocationUpdate(Boolean locationUpdate) {
        this.locationUpdate = locationUpdate;
    }

    public Boolean getLocationUpdate() {

        if (null == locationUpdate) {
            return false;
        }

        return locationUpdate;
    }

    public void setAutoSchedule(Boolean autoSchedule) {
        this.autoSchedule = autoSchedule;
    }

    public Boolean getAutoSchedule() {

        if (null == autoSchedule) {
            return false;
        }

        return autoSchedule;
    }

    public void setMassEventOrigin(MassEventOrigin massEventOrigin) {
        this.massEventOrigin = massEventOrigin;
    }

    class OuterEventForm extends Form {

        public OuterEventForm(String id) {
            super(id);

            add(new Label("eventTypeName", new PropertyModel<String>(event, "type.name")));

            //Owner
            add(createOwnerSection(event));

            //Event Type Details (Performed By and Date Performed)
            createEventTypeDetailsSection(event, this);

            //Event Form
            createEventFormSection(event, this);

            //Result
            createResultSection(event, this);

            //Post Event Information
            createPostEventInformationSection(event, this);

            //Auto Schedules
            createAutoScheudlesSection(event, this);

            //Event Schedules
            createScheduleContainer(event, this);

            //Continue/Back To Step 1 links

            Button saveButton = new Button("saveButton");
            saveButton.add(new DisableNavigationConfirmationBehavior());
            saveButton.add(new DisableButtonBeforeSubmit());
            add(saveButton);
            add(createCancelLink("cancelLink"));
        }

        @Override
        protected void onSubmit() {

            event.getObject().setSectionResults(sectionResults);
            onPreSave(event.getObject());
            onPreSave(event.getObject());
            saveAssignedToIfNecessary();
            saveEventBookIfNecessary();

            List<ThingEvent> savedEvent = doSave();
            setResponsePage(new CompletedMassEventPage(savedEvent, massEventOrigin));
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onValidate() {
            super.onValidate();

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

            if (event instanceof Event) {
                Event masterEvent = event.getObject();
                if (masterEvent.getBook() != null && masterEvent.getBook().getId() == null && StringUtils.isBlank(masterEvent.getBook().getName())) {
                    error(new FIDLabelModel("error.event_book_title_required").getObject());
                }

                if (masterEvent.getOwner() == null) {
                    error(new FIDLabelModel("error.event_book_title_required").getObject());
                }
            }

            List<AbstractEvent.SectionResults> results =  event.getObject().getSectionResults();

            RequiredCriteriaValidator.validate(results).stream().forEach(this::error);
        }
    }

    protected Behavior createUpdateAutoschedulesOnChangeBehavior() {
        return new UpdateComponentOnChange() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(schedulesContainer);
            }
        };
    }

    protected abstract Component createPostEventPanel(IModel<T> event);
    protected abstract Component createCancelLink(String id);
    protected abstract boolean targetAlreadyArchived(T event);

    @SuppressWarnings("unchecked")
    protected Component createOwnerSection(IModel<T> event) {
        ownerSection = new WebMarkupContainer("ownerSection");

        GroupedUserPicker groupedUserPicker;
        ownerSection.add(groupedUserPicker = new GroupedUserPicker("assignedTo", new PropertyModel<>(MultiEventPage.this, "assignedTo"), new GroupedVisibleUsersModel()));
        groupedUserPicker.setVisible(event.getObject().getType().isAssignedToAvailable());

        final PropertyModel<BaseOrg> ownerModel = new PropertyModel(event,"owner");

        //Owner Picker
        ownerPicker = new OrgLocationPicker("orgPicker", ownerModel) {
            @Override
            protected void onChanged(AjaxRequestTarget target) {
                if (getTextString() != null && getTextString().equals("")) {
                    locationPicker.setLocationOwner(null);
                } else {
                    locationPicker.setLocationOwner(getOwner());
                }
                target.add(schedulesContainer);
            }
        }.withAutoUpdate();

        ownerPicker.setEnabled(!assetOwnerUpdate);
        ownerPicker.setIconVisible(!assetOwnerUpdate);
        ownerPicker.setTextEnabled(!assetOwnerUpdate);
        ownerPicker.setEmptyString();
        ownerPicker.setOutputMarkupId(true);
        ownerSection.add(ownerPicker);

        // checkbox
        AjaxCheckBox ownerCheckBox = new AjaxCheckBox("assetOwnerUpdate", new PropertyModel<>(MultiEventPage.this, "assetOwnerUpdate")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                ownerPicker.setEnabled(!assetOwnerUpdate);
                ownerPicker.setIconVisible(!assetOwnerUpdate);
                ownerPicker.setTextEnabled(!assetOwnerUpdate);
                if(assetOwnerUpdate) {
                    ownerPicker.setEmptyString();
                }
                target.add(ownerPicker);
            }
        };

        ownerSection.add(ownerCheckBox);

        //Location Picker
        final PropertyModel<Location> locationModel = new PropertyModel<>(event, "advancedLocation");
        final PropertyModel<PredefinedLocation> predefinedLocationModel = new PropertyModel<>(event, "advancedLocation.predefinedLocation");

        BaseOrg temp = ownerModel.getObject();

        locationPicker = new OrgLocationPicker("locationPicker", Model.of(temp), predefinedLocationModel){
            @Override
            public String getWatermarkText() {
                return new FIDLabelModel("message.locationpicker_watermark").getObject();
            }
        }.withLocations();
        locationPicker.setEnabled(!locationUpdate);
        locationPicker.setIconVisible(!locationUpdate);
        locationPicker.setTextEnabled(!locationUpdate);
        locationPicker.setOutputMarkupId(true);
        ownerSection.add(locationPicker);

        final TextField<String> freeformLocation = new TextField<>("freeformLocation", new PropertyModel<>(locationModel, "freeformLocation"));
        freeformLocation.setEnabled(!locationUpdate);
        freeformLocation.setOutputMarkupId(true);
        ownerSection.add(freeformLocation);

        AjaxCheckBox locationCheckBox = new AjaxCheckBox("locationUpdate", new PropertyModel<>(MultiEventPage.this, "locationUpdate")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                locationPicker.setEnabled(!locationUpdate);
                locationPicker.setIconVisible(!locationUpdate);
                locationPicker.setTextEnabled(!locationUpdate);
                freeformLocation.setEnabled(!locationUpdate);
                target.add(locationPicker, freeformLocation);

            }
        };
        ownerSection.add(locationCheckBox);

        return ownerSection;
    }

    protected void createScheduleContainer(IModel<T> event, OuterEventForm form) {
        schedulesContainer = new WebMarkupContainer("schedulesContainer");
        schedulesContainer.setOutputMarkupPlaceholderTag(true);
        schedulesContainer.setVisible(isScheduleVisible());
        schedulesContainer.add(new ListView<Event>("schedules", new PropertyModel<List<Event>>(MultiEventPage.this, "schedules")) {
            @Override
            protected void populateItem(final ListItem<Event> item) {
                item.add(new Label("addScheduleDate", new DayDisplayModel(new PropertyModel<>(item.getModel(), "dueDate"))));
                item.add(new Label("addScheduleLabel", new PropertyModel<>(item.getModel(), "eventType.name")));
                item.add(new FlatLabel("addScheduleJob", new PropertyModel<>(item.getModel(), "project.name")));
                item.add(new AjaxLink<Void>("removeLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        schedules.remove(item.getIndex());
                        target.add(schedulesContainer);
                    }
                });
            }
        });
        form.add(schedulesContainer);

        final boolean isNew = event.getObject().isNew();
        final boolean isCompleted = event.getObject().isCompleted();

        schedulesContainer.add(new AjaxLink("openSchedulePickerLink") {
            { setVisible(isNew || !isCompleted); }
            @Override
            public void onClick(AjaxRequestTarget target) {
                schedulePicker.show(target);
            }
        });

    }

    protected void createEventTypeDetailsSection(IModel<T> event, OuterEventForm form) {
        PropertyModel<User> performedByModel = new PropertyModel<>(event, "performedBy");
        //We should be using new ListableChoiceRenderer<User>() here but there is seems to be a hibernate proxy problem :(
        DropDownChoice<User> performedBy = new FidDropDownChoice<>("performedBy", performedByModel, new ExaminersModel(performedByModel), new IChoiceRenderer<User>() {
            @Override
            public Object getDisplayValue(User object) {
                return object.getDisplayName();
            }

            @Override
            public String getIdValue(User object, int index) {
                return object.getID() + "";
            }
        });

        DateTimePicker datePerformedPicker = new DateTimePicker("datePerformed", new UserToUTCDateModel(new PropertyModel<>(event, "date")), true).withNoAllDayCheckbox();
        datePerformedPicker.addToDateField(createUpdateAutoschedulesOnChangeBehavior());

        form.add(datePerformedPicker);
        form.add(performedBy);
    }

    protected void createEventFormSection(IModel<T> event, OuterEventForm form2) {
        EventForm form = event.getObject().getEventForm();
        form2.add(new EventFormEditPanel("eventFormPanel", event, new PropertyModel<>(MultiEventPage.this, "sectionResults"), isActionButtonsVisible()){
            @Override
            protected Panel getCriteriaSectionPanel(Class<? extends AbstractEvent> eventClass, PropertyModel<List<CriteriaResult>> results) {
                return new CriteriaSectionEditPanel("criteriaPanel", eventClass, results, isActionButtonsVisible(), isAttachmentAndActionVisible());
            }
        }.setVisible(form != null && form.getAvailableSections().size() > 0));
    }

    protected void createResultSection(IModel<T> event, OuterEventForm form) {
        Event masterEvent = event.getObject();
        DropDownChoice resultSelect = new FidDropDownChoice<>("eventResult", new PropertyModel<>(MultiEventPage.this, "eventResult"), EventResult.getValidEventResults(), new ListableLabelChoiceRenderer<>());
        resultSelect.add(new UpdateComponentOnChange());
        resultSelect.setNullValid(masterEvent.isResultFromCriteriaAvailable());
        form.add(resultSelect);

        List<EventStatus> eventStatuses = eventStatusService.getActiveStatuses();
        DropDownChoice workflowStateSelect = new FidDropDownChoice<>("eventStatus", new PropertyModel<>(event, "eventStatus"), eventStatuses, new ListableLabelChoiceRenderer<>());
        workflowStateSelect.add(new UpdateComponentOnChange());
        workflowStateSelect.setNullValid(true);
        form.add(workflowStateSelect);
    }

    protected void createPostEventInformationSection(IModel<T> event, OuterEventForm form) {
        form.add(new Comment("comments", new PropertyModel<>(event, "comments")).addMaxLengthValidation(2500));

        form.add(new CheckBox("printable", new PropertyModel<>(event, "printable")).add(new UpdateComponentOnChange()));

        form.add(createPostEventPanel(event));
    }

    protected void createAutoScheudlesSection(IModel<T> event, OuterEventForm form) {

        CheckBox autoScheduleCheckBox = new CheckBox("autoSchedule", new PropertyModel<>(MultiEventPage.this, "autoSchedule"));
        form.add(autoScheduleCheckBox);

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

    protected abstract List<ThingEvent> doSave();
    protected void onPreSave(T event) { }

    public EventResult getEventResult() {
        return eventResult;
    }

    public void setEventResult(EventResult eventResult) {
        this.eventResult = eventResult;
    }

    protected boolean isScheduleVisible() {
        return (event.getObject().isNew() || !event.getObject().isCompleted());
    }

    public boolean isActionButtonsVisible() { return true; }

    public boolean isAttachmentAndActionVisible() {
        return true;
    }

}
