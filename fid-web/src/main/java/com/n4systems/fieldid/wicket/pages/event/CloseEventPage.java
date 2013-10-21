package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.ejb.impl.EventScheduleManagerImpl;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.event.EventStatusService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.ReportPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunLastReportPage;
import com.n4systems.model.*;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CloseEventPage extends FieldIDFrontEndPage {

    private @SpringBean EventStatusService eventStatusService;
    private @SpringBean UserService userService;
    private @SpringBean PersistenceService persistenceService;
    private  @SpringBean EventScheduleService eventScheduleService;

    protected IModel<Event> openEventModel;
    private FieldIDFrontEndPage returnPage;

    public CloseEventPage(PageParameters params) {
        super(params);
        Long id = params.get("uniqueID").toLong();
        openEventModel = new EntityModel<Event>(Event.class, id);
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new Label("event", PropertyModel.of(openEventModel, "type.name")));
        add(new ResolveForm("form"));
    }

    public CloseEventPage(PageParameters params, FieldIDFrontEndPage returnPage) {
        this(params);
        this.returnPage = returnPage;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/event/resolve.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

    @Override
    protected boolean useLegacyCss() {
        return false;
    }

    class ResolveForm extends Form {

        private EventStatus eventStatus;
        private String comment;
        private User resolvedBy = getCurrentUser();

        public ResolveForm(String id) {
            super(id);
            resolvedBy = getCurrentUser();
            List<EventStatus> activeStatuses = getActiveStatuses();
            add(new Label("due", new PropertyModel<String>(openEventModel, "dueDate")));
            add(new Label("state", new PropertyModel<String>(openEventModel, "workflowState")));
            add( new FidDropDownChoice<EventStatus>("status",
                    new PropertyModel<EventStatus>(this, "eventStatus"),
                    activeStatuses,
                    new ChoiceRenderer<EventStatus>("name")).setNullValid(false).setRequired(true));

            add( new FidDropDownChoice<User>("resolver",
                    new PropertyModel<User>(this, "resolvedBy"),
                    getUsers(),
                    new ChoiceRenderer<User>("assignToDisplayName")).setNullValid(false).setRequired(true));

            add(new TextArea<String>("comment", new PropertyModel<String>(this, "comment")));

            add(new Button("closeButton"));

            add(new Link<Void>("cancelLink") {
                @Override
                public void onClick() {
                    if (returnPage!=null) {
                        setResponsePage(returnPage);
                    } else {
                        setResponsePage(RunLastReportPage.class);
                    }
                }
            });

            if (activeStatuses.size() > 0) {
                eventStatus = activeStatuses.get(0);
            }
        }

        @Override
        protected void onSubmit() {
            Event openEvent = openEventModel.getObject();
            Asset asset = openEvent.getAsset();
            openEvent.setEventResult(EventResult.VOID);
            openEvent.setWorkflowState(WorkflowState.CLOSED);
            openEvent.setDate(new Date());
            openEvent.setPerformedBy(resolvedBy);
            openEvent.setEventStatus(eventStatus);
            openEvent.setComments(comment);
            openEvent.setOwner(asset.getOwner());
            openEvent.setAdvancedLocation(asset.getAdvancedLocation());
            openEvent.setAssignedTo(AssignedToUpdate.assignAssetToUser(asset.getAssignedUser()));

            persistenceService.update(openEvent);
            FieldIDSession.get().info(getString("message.event_closed"));

            updateRecurringAssetTypeEvent(openEvent);

            if (returnPage!=null) {
                setResponsePage(returnPage);
            } else {
                setResponsePage(ReportPage.class);
            }
        }


    }

    private void updateRecurringAssetTypeEvent(Event event) {
        RecurringAssetTypeEvent recurringEvent = event.getRecurringEvent();

        List<Event> openEvents = null;
        Event uevent = event;

        if (null != recurringEvent && recurringEvent.getAutoAssign()) {

            openEvents = eventScheduleService.getAvailableSchedulesFor(event.getAsset());

            if (null != openEvents && openEvents.size() > 0) {

                Event nextSched = null;

                if (recurringEvent.getRecurrence().getType() == RecurrenceType.DAILY) {

                    for (Event sched : openEvents) {

                        GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
                        cal.setTime(sched.getDueDate());

                        GregorianCalendar ical = (GregorianCalendar) Calendar.getInstance();
                        ical.setTime(uevent.getDueDate());

                        boolean sameDay = cal.get(Calendar.YEAR) == ical.get(Calendar.YEAR) &&
                                cal.get(Calendar.DAY_OF_YEAR) == ical.get(Calendar.DAY_OF_YEAR);

                        boolean sameHour = cal.get(Calendar.HOUR_OF_DAY) == ical.get(Calendar.HOUR_OF_DAY);


                        if (sched.getDueDate().after(uevent.getDueDate()) && !sameDay && sameHour) {

                            nextSched = sched;
                            nextSched.setAssignee(event.getPerformedBy());
                            break;

                        }

                    }  // end for

                } else {
                    nextSched = openEvents.get(0);
                    nextSched.setAssignee(event.getPerformedBy());

                } // if DAILY

                if (null != nextSched) {
                    uevent = persistenceService.update(nextSched);
                }

            }  // if open events

        } // if recurring event



    }


    private List<User> getUsers() {
        return userService.getUsers(false, false);
    }

    private List<EventStatus> getActiveStatuses() {
        return eventStatusService.getActiveStatuses();
    }


}
