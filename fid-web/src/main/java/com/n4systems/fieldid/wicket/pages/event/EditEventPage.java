package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.fieldid.service.event.EventCreationService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.*;
import com.n4systems.fieldid.wicket.components.event.EventFormEditPanel;
import com.n4systems.fieldid.wicket.components.event.attributes.AttributesEditPanel;
import com.n4systems.fieldid.wicket.components.event.book.NewOrExistingEventBook;
import com.n4systems.fieldid.wicket.components.event.prooftest.ProofTestEditPanel;
import com.n4systems.fieldid.wicket.components.event.schedule.EventSchedulePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.fileupload.AttachmentsPanel;
import com.n4systems.fieldid.wicket.components.location.LocationPicker;
import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import com.n4systems.fieldid.wicket.model.assetstatus.AssetStatusesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;

public class EditEventPage extends FieldIDFrontEndPage {
    
    @SpringBean
    private EventService eventService;

    @SpringBean
    private EventCreationService eventCreationService;

    private AbstractEvent event;

    private List<EventSchedule> schedules = new ArrayList<EventSchedule>();
    private EventSchedule scheduleToAdd = new EventSchedule();
    private EventSchedule selectedSchedule;

    public EditEventPage() {
        event = eventService.createNewMasterEvent(16091209L, 204L);
        doAutoSchedule();
        
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new OuterEventForm("outerEventForm"));
    }
    
    class OuterEventForm extends Form {

        public OuterEventForm(String id) {
            super(id);
            add(new Label("assetTypeName", new PropertyModel<String>(event, "asset.type.name")));

            add(new Label("description", new PropertyModel<String>(event, "asset.description")));
            add(new IdentifierLabel("identifierLabel", new PropertyModel<AssetType>(event, "asset.type")));
            NonWicketLink assetLink = new NonWicketLink("assetLink", "asset.action?uniqueID="+event.getAsset().getId());
            assetLink.add(new Label("identifier", new PropertyModel<Object>(event, "asset.identifier")));
            add(assetLink);

            add(new Label("rfidNumber", new PropertyModel<String>(event, "asset.rfidNumber")));
            add(new Label("referenceNumber", new PropertyModel<String>(event, "asset.customerRefNumber")));

            add(new OrgPicker("orgPicker", new PropertyModel<BaseOrg>(event, "owner")) {
                @Override
                protected void onOrgSelected(AjaxRequestTarget target) {
                    doAutoSchedule();
                }
            });

            final WebMarkupContainer schedulesContainer = new WebMarkupContainer("schedulesContainer");
            schedulesContainer.setOutputMarkupId(true);
            schedulesContainer.add(new ListView<EventSchedule>("schedules", new PropertyModel<List<EventSchedule>>(EditEventPage.this, "schedules")) {
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

            add(new SchedulePicker("schedulePicker", new FIDLabelModel("label.add_a_schedule"), new PropertyModel<EventSchedule>(EditEventPage.this, "scheduleToAdd"), new EventTypesForAssetTypeModel(new PropertyModel<AssetType>(event, "asset.type")), new EventJobsForTenantModel(), 0, 0) {
                @Override
                protected void onPickComplete(AjaxRequestTarget target) {
                    schedules.add(scheduleToAdd);
                    scheduleToAdd = new EventSchedule();
                    target.add(schedulesContainer);
                }
            });

            add(new Label("eventTypeName", new PropertyModel<String>(event, "type.name")));
            
            WebMarkupContainer proofTestContainer = new WebMarkupContainer("proofTestContainer");
            proofTestContainer.add(new ProofTestEditPanel("proofTest", new PropertyModel<ProofTestInfo>(event, "proofTestInfo")));
            add(proofTestContainer);

            PropertyModel<User> performedByModel = new PropertyModel<User>(event, "performedBy");
            DropDownChoice<User> performedBy = new DropDownChoice<User>("performedBy", performedByModel, new ExaminersModel(performedByModel), new ListableChoiceRenderer<User>());
            DateTimePicker datePicker = new DateTimePicker("datePerformed", new UserToUTCDateModel(new PropertyModel<Date>(event, "date")), true);
            NewOrExistingEventBook newOrExistingEventBook = new NewOrExistingEventBook("newOrExistingEventBook", new PropertyModel<EventBook>(event, "book"));
            
            add(new EventSchedulePicker("schedule", new PropertyModel<EventSchedule>(EditEventPage.this, "selectedSchedule"), new PropertyModel<Asset>(event, "asset")));

            AttributesEditPanel attributesEditPanel = new AttributesEditPanel("eventAttributes", new Model<AbstractEvent>(event));

            add(newOrExistingEventBook);
            add(attributesEditPanel);
            add(datePicker);
            add(performedBy);

            add(new LocationPicker("locationPicker", new PropertyModel<Location>(event, "advancedLocation")).withRelativePosition());
            
            add(new Comment("comments", new PropertyModel<String>(event, "comments")));

            DropDownChoice assetStatus = new DropDownChoice<AssetStatus>("assetStatus", new PropertyModel<AssetStatus>(event, "assetStatus"), new AssetStatusesForTenantModel(), new ListableChoiceRenderer<AssetStatus>());
            assetStatus.add(new UpdateComponentOnChange());
            assetStatus.setNullValid(true);
            add(assetStatus);
            
            if (event instanceof Event) {
                Event masterEvent = (Event) event;
                DropDownChoice resultSelect = new DropDownChoice<Status>("result", new PropertyModel<Status>(event, "status"), Status.getValidEventStates(), new ListableLabelChoiceRenderer<Status>());
                resultSelect.add(new UpdateComponentOnChange());
                resultSelect.setNullValid(masterEvent.isResultFromCriteriaAvailable());
                add(resultSelect);
            }

            add(new CheckBox("printable", new PropertyModel<Boolean>(event, "printable")).add(new UpdateComponentOnChange()));

            add(new EventFormEditPanel("eventFormPanel", new PropertyModel<List<AbstractEvent.SectionResults>>(event, "sectionResults")));
            add(new AttachmentsPanel("attachmentsPanel", new PropertyModel<List<FileAttachment>>(event, "attachments")));
            
            add(new Button("saveButton"));
            add(new BookmarkablePageLink<Void>("cancelLink", DashboardPage.class));
        }

        @Override
        protected void onSubmit() {
            if (doPostSubmitValidation()) {
                Long scheduleId = selectedSchedule == null ? 0L : selectedSchedule.getId();
                event.storeTransientCriteriaResults();
                eventCreationService.createEventWithSchedules((Event)event, scheduleId, null, Collections.<FileAttachment>emptyList(), createEventScheduleBundles());
                info("Successfully created event");
            }
        }
    }

    private List<EventScheduleBundle> createEventScheduleBundles() {
        List<EventScheduleBundle> scheduleBundles = new ArrayList<EventScheduleBundle>();

        for (EventSchedule sched : schedules) {
            EventScheduleBundle bundle = new EventScheduleBundle(sched.getAsset(), sched.getEventType(), sched.getProject(), sched.getNextDate());
            scheduleBundles.add(bundle );
        }

        return scheduleBundles;
    }

    private boolean doPostSubmitValidation() {
        if (event instanceof Event) {
            Event masterEvent = (Event) event;
            if (masterEvent.getBook() != null && masterEvent.getBook().getId() == null && StringUtils.isBlank(masterEvent.getBook().getName())) {
                error(new FIDLabelModel("error.event_book_title_required").getObject());
                return false;
            }

            if (masterEvent.getOwner() == null) {
                error(new FIDLabelModel("error.event_book_title_required").getObject());
                return false;
            }
        }

        if (event.containsUnfilledScoreCriteria()) {
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

    private void doAutoSchedule() {
        AssetTypeSchedule schedule = event.getAsset().getType().getSchedule(event.getType(), ((Event) event).getOwner());
        schedules.clear();
        EventSchedule eventSchedule = new EventSchedule();
        eventSchedule.setAsset(event.getAsset());
        eventSchedule.setEventType(event.getType());
        eventSchedule.setNextDate(schedule.getNextDate(((Event) event).getDate()));
        schedules.add(eventSchedule);
    }

}
