package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventCreationService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.DisableButtonBeforeSubmit;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.Comment;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.IdentifierLabel;
import com.n4systems.fieldid.wicket.components.event.EventFormEditPanel;
import com.n4systems.fieldid.wicket.components.event.attributes.AttributesEditPanel;
import com.n4systems.fieldid.wicket.components.event.book.NewOrExistingEventBook;
import com.n4systems.fieldid.wicket.components.event.prooftest.ProofTestEditPanel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.fileupload.AttachmentsPanel;
import com.n4systems.fieldid.wicket.components.location.LocationPicker;
import com.n4systems.fieldid.wicket.components.org.OrgPicker;
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
import com.n4systems.fieldid.wicket.model.user.GroupedUsersForTenantModel;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.*;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class EventPage extends FieldIDFrontEndPage {
    
    @SpringBean protected EventCreationService eventCreationService;
    @SpringBean protected PersistenceService persistenceService;

    protected IModel<? extends AbstractEvent> event;

    private List<Event> schedules = new ArrayList<Event>();
    private Event scheduleToAdd;
    private List<AbstractEvent.SectionResults> sectionResults;

    @Override
    protected void onInitialize() {
        super.onInitialize();
        sectionResults = event.getObject().getSectionResults();
        scheduleToAdd = createNewOpenEvent();
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new OuterEventForm("outerEventForm"));
    }

    private Event createNewOpenEvent() {
        Event openEvent = new Event();
        openEvent.setAsset(event.getObject().getAsset());
        return openEvent;
    }

    class OuterEventForm extends Form {

        private LocationPicker locationPicker;

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
            add(groupedUserPicker = new GroupedUserPicker("assignedTo", new PropertyModel<User>(event, "assignee"), new GroupedUsersForTenantModel()));
            groupedUserPicker.setNullValid(true);
            groupedUserPicker.setVisible(event.getObject().getType().isAssignedToAvailable());

            add(new OrgPicker("orgPicker", new PropertyModel<BaseOrg>(event, "owner")) {
                @Override
                protected void onOrgSelected(AjaxRequestTarget target) {
                    doAutoSchedule();
                    locationPicker.setOwner((BaseOrg) getDefaultModel().getObject());
                }
            });

            final WebMarkupContainer schedulesContainer = new WebMarkupContainer("schedulesContainer");
            schedulesContainer.setOutputMarkupId(true);
            schedulesContainer.setVisible(event.getObject().isNew());
            schedulesContainer.add(new ListView<EventSchedule>("schedules", new PropertyModel<List<EventSchedule>>(EventPage.this, "schedules")) {
                @Override
                protected void populateItem(final ListItem<EventSchedule> item) {
                    item.add(new Label("addScheduleDate", new DayDisplayModel(new PropertyModel<Date>(item.getModel(), "nextDate"))));
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

            SchedulePicker schedulePicker = new SchedulePicker("schedulePicker", new FIDLabelModel("label.add_a_schedule"), new PropertyModel<Event>(EventPage.this, "scheduleToAdd"), new EventTypesForAssetTypeModel(new PropertyModel<AssetType>(event, "asset.type")), new EventJobsForTenantModel(), 0, 0) {
                @Override
                protected void onPickComplete(AjaxRequestTarget target) {
                    schedules.add(scheduleToAdd);
                    scheduleToAdd = createNewOpenEvent();
                    target.add(schedulesContainer);
                }
            };
            schedulePicker.setVisible(event.getObject().isNew());
            add(schedulePicker);

            add(new Label("eventTypeName", new PropertyModel<String>(event, "type.name")));
            
            WebMarkupContainer proofTestContainer = new WebMarkupContainer("proofTestContainer");
            proofTestContainer.add(new ProofTestEditPanel("proofTest", new PropertyModel<ProofTestInfo>(event, "proofTestInfo")));
            add(proofTestContainer);

            PropertyModel<User> performedByModel = new PropertyModel<User>(event, "performedBy");
            DropDownChoice<User> performedBy = new DropDownChoice<User>("performedBy", performedByModel, new ExaminersModel(performedByModel), new ListableChoiceRenderer<User>());
            DateTimePicker datePerformedPicker = new DateTimePicker("datePerformed", new UserToUTCDateModel(new PropertyModel<Date>(event, "date")), true).withNoAllDayCheckbox();
            DateTimePicker dateScheduledPicker = new DateTimePicker("dateScheduled", new PropertyModel<Date>(event, "nextDate"), true).withNoAllDayCheckbox();
            NewOrExistingEventBook newOrExistingEventBook = new NewOrExistingEventBook("newOrExistingEventBook", new PropertyModel<EventBook>(event, "book"));

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
            
            add(new Comment("comments", new PropertyModel<String>(event, "comments")));

            DropDownChoice assetStatus = new DropDownChoice<AssetStatus>("assetStatus", new PropertyModel<AssetStatus>(event, "assetStatus"), new AssetStatusesForTenantModel(), new ListableChoiceRenderer<AssetStatus>());
            assetStatus.add(new UpdateComponentOnChange());
            assetStatus.setNullValid(true);
            add(assetStatus);
            
            if (event.getObject() instanceof Event) {
                Event masterEvent = (Event) event.getObject();
                DropDownChoice resultSelect = new DropDownChoice<Status>("result", new PropertyModel<Status>(event, "status"), Status.getValidEventStates(), new ListableLabelChoiceRenderer<Status>());
                resultSelect.add(new UpdateComponentOnChange());
                resultSelect.setNullValid(masterEvent.isResultFromCriteriaAvailable());
                add(resultSelect);
            }

            add(new CheckBox("printable", new PropertyModel<Boolean>(event, "printable")).add(new UpdateComponentOnChange()));

            add(new EventFormEditPanel("eventFormPanel", new PropertyModel<List<AbstractEvent.SectionResults>>(EventPage.this, "sectionResults")).setVisible(event.getObject().getEventForm() != null));
            add(new AttachmentsPanel("attachmentsPanel", new PropertyModel<List<FileAttachment>>(event, "attachments")));

            Button saveButton = new Button("saveButton");
            saveButton.add(new DisableButtonBeforeSubmit());
            add(saveButton);
            add(new BookmarkablePageLink<Void>("cancelLink", DashboardPage.class));
        }

        @Override
        protected void onSubmit() {
            event.getObject().setSectionResults(sectionResults);
            if (doPostSubmitValidation()) {
                AbstractEvent savedEvent = doSave();
                FieldIDSession.get().storeInfoMessageForStruts(getString("message.eventsaved"));
                throw new RedirectToUrlException("/event.action?uniqueID="+savedEvent.getId());
            }
        }
    }

    protected void saveEventBookIfNecessary() {
        EventBook book = ((Event) event.getObject()).getBook();
        if (book != null && book.getId() == null) {
            book.setTenant(getCurrentUser().getTenant());
            book.setOwner(getCurrentUser().getOwner());
            persistenceService.save(book);
        }
    }

    protected abstract AbstractEvent doSave();

    protected List<EventScheduleBundle> createEventScheduleBundles() {
        List<EventScheduleBundle> scheduleBundles = new ArrayList<EventScheduleBundle>();

        for (Event sched : schedules) {
            EventScheduleBundle bundle = new EventScheduleBundle(sched.getAsset(), sched.getEventType(), sched.getProject(), sched.getNextDate());
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
        AssetTypeSchedule schedule = event.getObject().getAsset().getType().getSchedule(event.getObject().getType(), ((Event) event.getObject()).getOwner());
        schedules.clear();
        if (schedule != null) {
            Event eventSchedule = new Event();
            eventSchedule.setAsset(event.getObject().getAsset());
            eventSchedule.setType(event.getObject().getType());
            eventSchedule.setNextDate(schedule.getNextDate(((Event) event.getObject()).getDate()));
            schedules.add(eventSchedule);
        }
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new PropertyModel<String>(this, "titleText"));
    }

    public String getTitleText() {
        return event.getObject().getType().getName() + " " + getString("label.on") + " " + event.getObject().getAsset().getIdentifier();
    }

}
