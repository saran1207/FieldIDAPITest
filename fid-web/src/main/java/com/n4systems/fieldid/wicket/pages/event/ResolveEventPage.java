package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventStatusService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.model.Event;
import com.n4systems.model.EventStatus;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class ResolveEventPage extends FieldIDFrontEndPage {

    private @SpringBean EventStatusService eventStatusService;
    private @SpringBean UserService userService;
    private @SpringBean PersistenceService persistenceService;

    protected IModel<Event> openEventModel;

    public ResolveEventPage(Long eventId) {
        openEventModel = new EntityModel<Event>(Event.class, eventId);
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new Label("event", PropertyModel.of(openEventModel, "type.name")));
        add(new ResolveForm("form"));
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

        private EventStatus status;
        private String comment;
        private User resolvedBy;

        public ResolveForm(String id) {
            super(id);
            resolvedBy = getCurrentUser();
            add(new Label("due", new PropertyModel<String>(openEventModel, "nextDate")));
            add(new Label("state", new PropertyModel<String>(openEventModel, "eventState")));
            add( new DropDownChoice<EventStatus>("status",
                    new PropertyModel<EventStatus>(this, "status"),
                    getEventStatus(),
                    new ChoiceRenderer<EventStatus>("name")).add(new JChosenBehavior()));

            add( new DropDownChoice<User>("resolver",
                    new PropertyModel<User>(this, "resolvedBy"),
                    getUsers(),
                    new ChoiceRenderer<User>("displayName")).add(new JChosenBehavior()));

            add(new TextArea<String>("comment", new PropertyModel<String>(this, "comment")));

            add(new Button("closeButton"));
            add(new BookmarkablePageLink<Void>("cancelLink", ReportPage.class));
        }

        @Override
        protected void onSubmit() {
            Event openEvent = openEventModel.getObject();
            openEvent.setEventState(Event.EventState.CLOSED);
            openEvent.setDate(new Date());
            openEvent.setPerformedBy(resolvedBy);
            openEvent.setEventStatus(status);
            openEvent.setComments(comment);
            persistenceService.update(openEvent);
            FieldIDSession.get().info(getString("message.event_closed"));
            setResponsePage(ReportPage.class);
        }
    }

    private List<User> getUsers() {
        return userService.getUsers(false, false);
    }

    private List<EventStatus> getEventStatus() {
        return eventStatusService.getActiveStatuses();
    }


}
